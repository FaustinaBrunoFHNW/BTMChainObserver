package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;

//TODO als Singelton Paater implementieren
//TODO JAVADOC

/**
 * In dieser Klasse wird die Blockchain aufgesetzt
 * <p>
 * Teile dieser Klasse wurden aus dem Tutorial gezogen
 * https://kauri.io/connecting-to-an-ethereum-client-with-java-eclipse-and-web3j/b9eb647c47a546bc95693acc0be72546/a
 * Video Tutorial https://www.youtube.com/watch?v=kJ905hVbQ_E
 */
public class ChainSetUp {

    private static ChainSetUp instance;

    private String privateKey;
    private String certifierAddress;
    private static BigInteger REGISTRATION_FEE = BigInteger.valueOf(1000000000000000000L);
    private Web3j web3j;
    private SimpleCertifier simpleCertifier;
    private SimpleRegistry simpleRegistry;
    private TransactionManager transactionManager;
    private byte[] hash;
    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
    private static Logger log = LoggerFactory.getLogger(ChainSetUp.class);

    //TODO naming
    //TODO JAVADOC
    /**
     * Dies wird ausgeführt wenn die Blockchain schon mal gestartet wurde
     *
     */
    public ChainSetUp() {
        log.info("connecting");

        this.certifierAddress = jsonDefaultSettingsHandler.getDefaultSettings().getCertifierAddress();
        web3j = Web3j.build(new HttpService(jsonDefaultSettingsHandler.getDefaultSettings().getConnectionAddress()));

        this.privateKey = jsonDefaultSettingsHandler.getMasterKey();
        this.transactionManager = new RawTransactionManager(web3j, this.getCredentialsFromPrivateKey());

        String str_hash = "6d3815e6a4f3c7fcec92b83d73dda2754a69c601f07723ec5a2274bd6e81e155";

        //Umwandeln in Bytearray Hexadecimal
        hash = new BigInteger(str_hash, 16).toByteArray();
        getInfo(web3j);
    }

    /**
     * Wird ausgeführt wenn man das Programm startet und nach dem bei einer Blockchain das setUp schonmal gelaufen ist
     *
     * @throws Exception
     */
    public void setUpAfterChainStart() throws Exception {
        this.getInfo(web3j);
        this.loadSimpleRegistry();
        this.loadCertifier(certifierAddress);

    }

    //TODO evtl in Genesis Block auslagern
    //TODO Javadoc

    /**
     * Wird ausgeführt bei einer komplett neuer Blockchain (beim Neustart einer Blockchain)
     *
     * @throws Exception
     */
    public void initChain() throws Exception {

        this.log.info("Simpleregistry und Certifier werden aufgesetzt.");
        this.loadSimpleRegistry();
        this.setUpCertifier();
    }


    /**
     * Erstellt die Simple Registry
     * und speichert den Certifier Owner
     *
     * @return boolean Wert ob das laden der Simpleregistry funktioniert hat
     */
    private void loadSimpleRegistry() {
        simpleRegistry = SimpleRegistry
                .load("0x0000000000000000000000000000000000001337", web3j, getCredentialsFromPrivateKey(),
                        new DefaultGasProvider());
        try {
           log.info("Fee of Registry: " + simpleRegistry.fee().send());
            //TODO owner speichern in Variable
           log.info("Besitzer von Registry: " + simpleRegistry.owner().send());
        } catch (Exception e) {
            log.warn("Simple Registry konnte nicht geladen werden");
            e.printStackTrace();
        }


    }

    //TODO JAVA DOC
    //TODO return wert

    /**
     * Hier wird der certifier deployed
     * Certifier Adresse wird ausgegeben und gespeichert
     *
     */
    private void setUpCertifier() {

        if (deployCertifier(transactionManager) && registerCertifier()) {

            log.info("Deployen des Certifier hat funktioniert");
            log.info("Das Registrieren des Certifiers hat funktioniert");
            log.info("Certifying hat funktioniert");
            log.info("Addresse vom Certifier: " + simpleCertifier.getContractAddress());
            this.certifierAddress = simpleCertifier.getContractAddress();

        }
        log.warn("Es gab ein Probelm beim Setup und Deployen und Registrieren des certifier ");

    }

    /**
     * Erstellt die credentials für den Private Key und gibt die wieder
     *
     * @return Credentials vom Private Key den man mitgeibt
     */
    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(this.privateKey);
    }

    //TODO JAVADOC

    /**
     * @param transactionManager
     * @return
     */
    private boolean deployCertifier(TransactionManager transactionManager) {

        try {
            log.info("Deployment vom Certifier");
            simpleCertifier = SimpleCertifier.deploy(web3j, transactionManager, new DefaultGasProvider()).send();
            log.info("Deployment vom Certifier erfolgreich");
            return true;
        } catch (Exception e) {
            log.warn("Deployen vom Certifier hat nicht funktioniert.");
            e.printStackTrace();
        }
        return false;

    }

    //TODO JAVA DOC

    /**
     * @return
     */
    private boolean registerCertifier() {

        try {
            log.info("Registrieren vom Certifier");
            TransactionReceipt receipt1 = simpleRegistry.reserve(hash, REGISTRATION_FEE).send();
            log.info("TX-Hash für reserve: " + receipt1.getTransactionHash());
            TransactionReceipt receipt2 = simpleRegistry.setAddress(hash, "A", simpleCertifier.getContractAddress())
                    .send();
            log.info("Tx-Hash für setAddress: " + receipt2.getTransactionHash());
            log.info("Registrierung vom Certifier erfolgreich");
            return true;
        } catch (Exception e) {
            log.warn("Registrierung vom Certifier hat nicht funktioniert.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gibt Infos der Blockchain aus
     *
     * @param web3j
     */
    private void getInfo(Web3j web3j) {
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();

            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();

            EthGasPrice gasPrice = web3j.ethGasPrice().send();

            log.info("Client Version " + clientVersion.getWeb3ClientVersion());
            log.info("Block Nummer " + blockNumber.getBlockNumber());
            log.info("Gas Preis " + gasPrice.getGasPrice());

        } catch (IOException e) {
            throw new RuntimeException("Error whilst sending json-rpc requests", e);
        }
    }

    //TODO JAVA DOC

    /**
     * @param add
     */
    public void loadCertifier(String add) {

        simpleCertifier = SimpleCertifier.load(add, web3j, getCredentialsFromPrivateKey(), new DefaultGasProvider());
        log.info("Laden vom Certifier erfolgreich");
    }

    //GETTER UND SETTER

    public Web3j getWeb3j() {
        return web3j;
    }

    public SimpleCertifier getSimpleCertifier() {
        return simpleCertifier;
    }

    public void setSimpleCertifier(SimpleCertifier simpleCertifier) {
        this.simpleCertifier = simpleCertifier;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public SimpleRegistry getSimpleRegistry() {
        return simpleRegistry;
    }

    public void setSimpleRegistry(SimpleRegistry simpleRegistry) {
        this.simpleRegistry = simpleRegistry;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public String getCertifierAddress() {
        return certifierAddress;
    }

    public void setCertifierAddress(String certifierAddress) {
        this.certifierAddress = certifierAddress;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}

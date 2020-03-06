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
 *
 * @Author Faustina Bruno, Serge-Jurij-Maikoff
 */
public class ChainSetup {

    public static ChainSetup instance;

    private String privateKey;
    private String certifierAddress;
    private static BigInteger REGISTRATION_FEE = BigInteger.valueOf(1000000000000000000L);
    private Web3j web3j;
    private SimpleCertifier simpleCertifier;
    private SimpleRegistry simpleRegistry;
    private TransactionManager transactionManager;
    private byte[] hash;
    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
    private Logger log = LoggerFactory.getLogger(ChainSetup.class);

    public static ChainSetup getInstance() {
        if (ChainSetup.instance == null) {
            ChainSetup.instance = new ChainSetup();
        }
        return ChainSetup.instance;
    }

    //TODO naming
    //TODO JAVADOC

    /**
     * Dies wird ausgeführt wenn die Blockchain schon mal gestartet wurde
     */
    private ChainSetup() {
        this.log.info("connecting");

        this.certifierAddress = this.jsonDefaultSettingsHandler.getDefaultSettings().getCertifierAddress();
        this.web3j = Web3j.build(new HttpService(this.jsonDefaultSettingsHandler.getDefaultSettings().getConnectionAddress()));

        this.privateKey = this.jsonDefaultSettingsHandler.getMasterKey();
        this.transactionManager = new RawTransactionManager(this.web3j, this.getCredentialsFromPrivateKey());

        String str_hash = "6d3815e6a4f3c7fcec92b83d73dda2754a69c601f07723ec5a2274bd6e81e155";

        //Umwandeln in Bytearray Hexadecimal
        this.hash = new BigInteger(str_hash, 16).toByteArray();
        this.getInfo(this.web3j);
    }

    /**
     * Wird ausgeführt wenn man das Programm startet und nach dem bei einer Blockchain das setUp schonmal gelaufen ist
     *
     * @throws Exception
     */
    public void setUpAfterChainStart() throws Exception {
        this.getInfo(this.web3j);
        this.loadSimpleRegistry();
        this.loadCertifier(this.certifierAddress);

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
        this.registerSelf();
    }

    /**
     * Erstellt die Simple Registry
     * und speichert den Certifier Owner
     *
     * @return boolean Wert ob das laden der Simpleregistry funktioniert hat
     */
    private void loadSimpleRegistry() {
        this.simpleRegistry = SimpleRegistry
                .load(this.jsonDefaultSettingsHandler.getDefaultSettings().getNameRegistryAddress(),
                        this.web3j, this.getCredentialsFromPrivateKey(),
                        new DefaultGasProvider());
        try {
            this.log.info("Fee of Registry: " + this.simpleRegistry.fee().send());
            //TODO owner speichern in Variable
            this.log.info("Besitzer von Registry: " + this.simpleRegistry.owner().send());
        } catch (Exception e) {
            this.log.error("Simple Registry konnte nicht geladen werden");
            e.printStackTrace();
        }

    }

    //TODO JAVA DOC
    //TODO return wert

    /**
     * Hier wird der certifier deployed
     * Certifier Adresse wird ausgegeben und gespeichert
     */
    private void setUpCertifier() {

        if (this.deployCertifier(this.transactionManager) && this.registerCertifier()) {

            this.log.info("Deployen des Certifier hat funktioniert");
            this.log.info("Das Registrieren des Certifiers hat funktioniert");
            this.log.info("Certifying hat funktioniert");
            this.log.info("Addresse vom Certifier: " + this.simpleCertifier.getContractAddress());
            this.certifierAddress = this.simpleCertifier.getContractAddress();
            this.jsonDefaultSettingsHandler.getDefaultSettings().setCertifierAddress(this.certifierAddress);
            this.jsonDefaultSettingsHandler.writeDefaultSettings();

        }
        this.log.error("Es gab ein Problem beim Setup und Deployen und Registrieren des Certifier ");

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
            this.log.info("Deployment vom Certifier");
            this.simpleCertifier = SimpleCertifier.deploy(this.web3j, transactionManager, new DefaultGasProvider()).send();
            this.log.info("Deployment vom Certifier erfolgreich");
            return true;
        } catch (Exception e) {
            this.log.error("Deployen vom Certifier hat nicht funktioniert.");
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
            this.log.info("Registrieren vom Certifier");
            TransactionReceipt receipt1 = this.simpleRegistry.reserve(this.hash, REGISTRATION_FEE).send();
            this.log.info("TX-Hash für reserve: " + receipt1.getTransactionHash());
            TransactionReceipt receipt2 = this.simpleRegistry.setAddress(this.hash, "A", this.simpleCertifier.getContractAddress())
                    .send();
            this.log.info("Tx-Hash für setAddress: " + receipt2.getTransactionHash());
            this.log.info("Registrierung vom Certifier erfolgreich");
            return true;
        } catch (Exception e) {
            this.log.error("Registrierung vom Certifier hat nicht funktioniert.");
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

            this.log.info("Client Version " + clientVersion.getWeb3ClientVersion());
            this.log.info("Block Nummer " + blockNumber.getBlockNumber());
            this.log.info("Gas Preis " + gasPrice.getGasPrice());

        } catch (IOException e) {
            //TODO log error ausfüllen
            this.log.error("");
            //TODO Exception auf Deutsch
            throw new RuntimeException("Error whilst sending json-rpc requests", e);
        }
    }

    //TODO JAVA DOC + public

    /**
     * Hier wird der certifier geladen
     * @param add Adresse des certifier Accounts
     */
    public void loadCertifier(String add) {

        this.simpleCertifier = SimpleCertifier.load(add, this.web3j, this.getCredentialsFromPrivateKey(), new DefaultGasProvider());
        this.log.info("Laden vom Certifier erfolgreich");
    }

    //TODO JAVADOC + public
    /**
     *
     */
    public void registerSelf(){
        try {
            this.simpleCertifier.certify(this.getCredentialsFromPrivateKey().getAddress()).send();
            //TODO Logger
            this.log.info("");
        } catch (Exception e) {
            this.log.error("Zertifizierung des MasterKeys war nicht erfolgreich");
            e.printStackTrace();
        }
    }

    //GETTER UND SETTER

    public Web3j getWeb3j() {
        return this.web3j;
    }

    public SimpleCertifier getSimpleCertifier() {
        return this.simpleCertifier;
    }

    public void setSimpleCertifier(SimpleCertifier simpleCertifier) {
        this.simpleCertifier = simpleCertifier;
    }

    public TransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public SimpleRegistry getSimpleRegistry() {
        return this.simpleRegistry;
    }

    public void setSimpleRegistry(SimpleRegistry simpleRegistry) {
        this.simpleRegistry = simpleRegistry;
    }

    public byte[] getHash() {
        return this.hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public String getCertifierAddress() {
        return this.certifierAddress;
    }

    public void setCertifierAddress(String certifierAddress) {
        this.certifierAddress = certifierAddress;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}

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


/**
 * In dieser Klasse wird die Blockchain aufgesetzt
 *
 * @Author Faustina Bruno, Serge Jurij Maikoff
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



    /**
     * Dies wird ausgeführt wenn die Blockchain schon mal gestartet wurde
     */
    private ChainSetup() {
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
        this.privateKey = jsonDefaultSettingsHandler.getMasterKey();
        this.getInfo(web3j);
        this.loadSimpleRegistry();
        this.loadCertifier(certifierAddress);
    }


    /**
     * Wird ausgeführt wenn die Blockchain neu gestartet wurde (beim Neustart einer Blockchain)
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
    void loadSimpleRegistry() {
        simpleRegistry = SimpleRegistry
                .load(jsonDefaultSettingsHandler.getDefaultSettings().getNameRegistryAddress(), web3j, getCredentialsFromPrivateKey(),
                        new DefaultGasProvider());
        try {
            log.info("Fee of Registry: " + simpleRegistry.fee().send());
            log.info("Besitzer von Registry: " + simpleRegistry.owner().send());
        } catch (Exception e) {
            log.error("Simple Registry konnte nicht geladen werden");
            e.printStackTrace();
        }

    }

    /**
     * Hier wird der certifier deployed
     * Certifier Adresse wird ausgegeben und gespeichert
     */
    public void setUpCertifier() {

        if (deployCertifier(transactionManager) && registerCertifier()) {

            log.info("Deployen des Certifier hat funktioniert");
            log.info("Das Registrieren des Certifiers hat funktioniert");
            log.info("Certifying hat funktioniert");
            log.info("Addresse vom Certifier: " + simpleCertifier.getContractAddress());
            this.certifierAddress = simpleCertifier.getContractAddress();
            this.jsonDefaultSettingsHandler.getDefaultSettings().setCertifierAddress(certifierAddress);
            this.jsonDefaultSettingsHandler.writeDefaultSettings();

        }else {
            log.error("Es gab ein Problem beim Setup und Deployen und Registrieren des Certifier ");
        }
    }

    /**
     * Erstellt die credentials für den Private Key und gibt die wieder
     *
     * @return Credentials vom Private Key den man mitgeibt
     */
    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(this.privateKey);
    }


    /**
     * Hier wird der Certifier deployed
     * @param transactionManager Account für die Depoyement Transaktion
     * @return ein booelan Wert ob das Deployment erfolgreich war oder nicht
     */
    public boolean deployCertifier(TransactionManager transactionManager) {

        try {
            log.info("Deployment vom Certifier");
            simpleCertifier = SimpleCertifier.deploy(web3j, transactionManager, new DefaultGasProvider()).send();
            log.info("Deployment vom Certifier erfolgreich");
            return true;
        } catch (Exception e) {
            log.error("Deployen vom Certifier hat nicht funktioniert.");
            e.printStackTrace();
        }
        return false;

    }


    /**
     * In dieser Methode wird der certifier in die SimpleRegestry Registriert
     * @return boolean Wert ob die Registrierung funktioniert hat
     */
    public boolean registerCertifier() {

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
            log.error("Registrierung vom Certifier hat nicht funktioniert.");
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
            this.log.error("Es gab ein Fehler beim holen der Web3J Informationen");
            throw new RuntimeException("Error während dem Senden einer json-rpc Requests", e);
        }
    }


    /**
     * Hier wird der certifier geladen
     * @param add Adresse des certifier Accounts
     */
    public void loadCertifier(String add) {

        simpleCertifier = SimpleCertifier.load(add, web3j, getCredentialsFromPrivateKey(), new DefaultGasProvider());

        log.info("Laden vom Certifier erfolgreich");
    }

    /**
     * In dieser Methode wird der SimpleCertifier zertifiziert
     */
    public void registerSelf(){
        try {
            simpleCertifier.certify(getCredentialsFromPrivateKey().getAddress()).send();
            log.info("SimpleCertifier wurde zertifiziert");
        } catch (Exception e) {
            log.error("Zertifizierung des Cimplecertifier war nicht erfolgreich");
            e.printStackTrace();
        }
    }

    //********************************GETTER UND SETTER**********************************

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

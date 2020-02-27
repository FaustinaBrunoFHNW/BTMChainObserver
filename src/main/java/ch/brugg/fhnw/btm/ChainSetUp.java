package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
import ch.brugg.fhnw.btm.loader.AccountLoader;
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
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


//TODO als Singelton Paater implementieren
//TODO JAVADOC
/**
 * In dieser Klasse wird die Blockchain aufgesetzt
 *
 * Teile dieser Klasse wurden aus dem Tutorial gezogen
 *  https://kauri.io/connecting-to-an-ethereum-client-with-java-eclipse-and-web3j/b9eb647c47a546bc95693acc0be72546/a
 *  Video Tutorial https://www.youtube.com/watch?v=kJ905hVbQ_E
 */
public class ChainSetUp {




    private static ChainSetUp instance;

    private final String privateKey;
    private String certifierAdd;
    private static BigInteger REGISTRATION_FEE = BigInteger.valueOf(1000000000000000000L);
    private Web3j web3j;
    private SimpleCertifier simpleCertifier;
    private SimpleRegistry simpleRegistry;
    private TransactionManager transactionManager;
    private byte[] hash;
    private static Logger log = LoggerFactory.getLogger(ChainSetUp.class);

    //TODO naming
    //TODO JAVADOC

    /**
     * Dies wird ausgeführt wenn die Blockchain schon mal gestartet wurde
     *
     * @param privateKey
     * @param certifierAdd
     */
    private ChainSetUp(String privateKey, String certifierAdd) {
        this.certifierAdd = certifierAdd;
        log.info("connecting");
        this.privateKey = privateKey;
        //Verbindungsaufbau zur Chain
        web3j = Web3j.build(new HttpService("http://jurijnas.myqnapcloud.com:8545/"));

        //TODO in Test auslagern ??
        //TM von dem Account mit dem PK
        this.transactionManager = new RawTransactionManager(web3j, this.getCredentialsFromPrivateKey());

        //sha3(service_transaction_checker) = 0x6d3815e6a4f3c7fcec92b83d73dda2754a69c601f07723ec5a2274bd6e81e155
        String str_hash = "6d3815e6a4f3c7fcec92b83d73dda2754a69c601f07723ec5a2274bd6e81e155";

        //Umwandeln in Bytearray Hexadecimal
        hash = new BigInteger(str_hash, 16).toByteArray();
        getInfo(web3j);
    }


    /**
     * Instanziert eine Instanz der Klasse falls es noch keine gibt und gibt
     * die existierende oder eben die neue zurück
     * @return die einmalige Instanz der Klasse
     */
    public static ChainSetUp getInstance(String privateKey, String certifierAdd) {

        if (ChainSetUp.instance == null) {
            ChainSetUp.instance = new ChainSetUp (privateKey,certifierAdd);
        }
        return ChainSetUp.instance;
    }



    //TODO naming

    /**
     * Wird ausgeführt wenn man das Programm startet und nach dem bei einer Blockchain das setUp schonmal gelaufen ist
     *
     * @throws Exception
     */
    public void setUpAfterChainStart() throws Exception {

        //Allgemeine Abfrage zur Chain ob alles funktioniert etc
        this.getInfo(web3j);

        this.loadSimpleRegistry();
        this.loadCertifier(certifierAdd);

    }

    //TODO evtl in Genesis Block auslagern
    //TODO Javadoc

    /**
     * Wird ausgeführt bei einer komplett neuer Blockchain (beim Neustart einer Blockchain)
     *
     * @throws Exception
     */
    public void setUpNewChainStart() throws Exception {

        this.log.info("Simpleregistry und certifier werden aufgesetzt.");
        //Allgemeine Abfrage zur Chain ob alles funktioniert etc
        this.getInfo(web3j);

        this.loadSimpleRegistry();
        this.setUpCertifier();
    }

    //TODO JAvaDoc
    //TODO return Wert ausbauen

    /**
     * Erstellt die Simple Registry
     * und speichert den Certifier Owner
     *
     * @return
     */
    private boolean loadSimpleRegistry() {

        //TODO Key in Konstante auslagern
        simpleRegistry = SimpleRegistry
                .load("0x0000000000000000000000000000000000001337", web3j, getCredentialsFromPrivateKey(),
                        new DefaultGasProvider());
        try {
            log.info("Fee of Registry: " + simpleRegistry.fee().send());
            //TODO owner speichern in Variable
            log.info("Owner of Registry: " + simpleRegistry.owner().send());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    //TODO JAVA DOC
    //TODO return wert

    /**
     * Hier wird der certifier deployed
     * Certifier Adresse wird ausgegeben und gespeichert
     *
     * @return
     */
    private boolean setUpCertifier() {

        if (deployCertifier(transactionManager) && registerCertifier()) {

            log.info("Deployen des Certifier hat funktioniert");
            log.info("Das Registrieren des Certifiers hat funktioniert");
            log.info("Certifying hat funktioniert");
            //TODO
            log.info("Addresse vom Certifier: " + simpleCertifier.getContractAddress());

            return true;
        }
        log.info("Es gab ein Probelm beim Setup und Deployen und Registrieren des certifier ");
        return false;
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
            log.info("Deploying Certifier");
            simpleCertifier = SimpleCertifier.deploy(web3j, transactionManager, new DefaultGasProvider()).send();
            log.info("Deployment of Certifier done");
            return true;
        } catch (Exception e) {
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
            log.info("Registering Certifier");
            TransactionReceipt receipt1 = simpleRegistry.reserve(hash, REGISTRATION_FEE).send();
            log.info("TX-Hash for reserve: " + receipt1.getTransactionHash());
            TransactionReceipt receipt2 = simpleRegistry.setAddress(hash, "A", simpleCertifier.getContractAddress())
                    .send();
            log.info("Tx-Hash for setAddress: " + receipt2.getTransactionHash());
            log.info("Done registering Certifier");
            return true;
        } catch (Exception e) {
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

            log.info("Client version " + clientVersion.getWeb3ClientVersion());
            log.info("Block number " + blockNumber.getBlockNumber());
            log.info("Gas price " + gasPrice.getGasPrice());

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
}

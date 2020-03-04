package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.loader.FileLoader;
import org.web3j.protocol.Web3j;

public class Main {

    public static void main(String[] args) throws Exception {

        //TODO revoke all acounts die certifyed sind

        //TODO aus File auslesen
        String PRIVATE_KEY = "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7";
        //TODO aus erstem hochfahren rauslesen
        String CERTIFIER_ADD = "0xee35211c4d9126d520bbfeaf3cfee5fe7b86f221";
        FileLoader fileLoader =  FileLoader.getInstance();

        System.out.println("Anzahl Accounts: " + fileLoader.getAccountArrayList().size());

        //TODO Singleton parameter übergeben
        ChainSetUp chainSetUp =  ChainSetUp.getInstance();
        chainSetUp.setCertifierAdd(CERTIFIER_ADD);
        chainSetUp.setPrivateKey(PRIVATE_KEY);
        chainSetUp.setUpAfterChainStart();
        //       chainSetUp.initChain();
        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

        Web3j web3j = chainSetUp.getWeb3j();

        //TODO load all accounts from list
        fileLoader.loadAll();
        chainInteractions.certifyAccountList(fileLoader.getAccountArrayList());
        chainInteractions.revokeAccountList(fileLoader.getDeleteAccountList());

        System.out.println("Anzahl zertifizierte Accounts: " + fileLoader.getAccountArrayList().size());
        System.out.println("Anzahl gesperrte Accounts: " + fileLoader.getRevokedAccountArrayList().size());
        System.out.println("Anzahl gelöschte Accounts: " + fileLoader.getDeleteAccountList().size());

        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j, chainInteractions);
        subscriptionTX.run(fileLoader.getDefaultSettings().getIntervalResetCounter());
    }


    //TODO für CML init Methode schreiben

    /**
     * Auführbare Methode fürs Aufsetzten der Blockchain
     * Hier wird der certifyer deployed
     * Die certify Adresse Gebrinted
     * Zertifizieren des JavaProgramm Accounts
     * @throws Exception
     */
    public static void init() throws Exception {

        FileLoader fileLoader =  FileLoader.getInstance();

        System.out.println("Anzahl Accounts: " + fileLoader.getAccountArrayList().size());

        //TODO Settings in DfaultSettings File speichern
        //TODO Singleton parameter übergeben
        ChainSetUp chainSetUp =  ChainSetUp.getInstance();
        chainSetUp.initChain();

        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

        Web3j web3j = chainSetUp.getWeb3j();

        fileLoader.loadAccounts();
        fileLoader.setCertifierAddress(chainSetUp.getCertifierAdd());
        //TODO Register ADD speichern
        chainInteractions.certifyAccountList(fileLoader.getAccountArrayList());
        chainInteractions.revokeAccountList(fileLoader.getDeleteAccountList());


        System.out.println("Anzahl zertifizierte Accounts: " + fileLoader.getAccountArrayList().size());
        System.out.println("Anzahl gesperrte Accounts: " + fileLoader.getRevokedAccountArrayList().size());
        System.out.println("Anzahl gelöschte Accounts: " + fileLoader.getDeleteAccountList().size());
        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j, chainInteractions);
        subscriptionTX.run(fileLoader.getDefaultSettings().getIntervalResetCounter());

    }

    //TODO für CML run Methode schreiiben

    /**
     * Ausführbare Methode über CMD für das laufen lassen des Programms
     * Revoken aller Accounts
     * Alle Accounts aus dem File einlesen
     * Alle Accounts Zertifizieren
     * Intervall starten
     * @throws Exception
     */
    public static void run() throws Exception {}

    //TODO für CML stop Methode schreiiben

    /**
     * Ausführbare Methode fürs stoppen aller Vorgänge und das Revoken aller Accounts
     * @throws Exception
     */
    public static void stop() throws Exception {}

}

package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.web3j.protocol.Web3j;

public class Main {

    public static void main(String[] args) throws Exception {

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        jsonDefaultSettingsHandler.loadDefaultSettings();

        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();

        String PRIVATE_KEY = jsonDefaultSettingsHandler.getMasterKey();
        System.out.println(PRIVATE_KEY);


        jsonDefaultSettingsHandler.writeDefaultSettings();
        System.out.println("Writing Accounts");
        jsonAccountHandler.writeAccountList();



        System.out.println("Anzahl Accounts: " + jsonAccountHandler.getAccountList().size());
        jsonDefaultSettingsHandler.loadDefaultSettings();


        ChainSetUp chainSetUp =  new ChainSetUp();

        chainSetUp.setUpAfterChainStart();
        //       chainSetUp.initChain();
        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

        Web3j web3j = chainSetUp.getWeb3j();

        /*
        //TODO load all accounts from list
        accountLoader.loadAll();
        chainInteractions.certifyAccountList(accountLoader.getAccountArrayList());
        chainInteractions.revokeAccountList(accountLoader.getDeleteAccountList());

        System.out.println("Anzahl zertifizierte Accounts: " + accountLoader.getAccountArrayList().size());
        System.out.println("Anzahl gesperrte Accounts: " + accountLoader.getRevokedAccountArrayList().size());
        System.out.println("Anzahl gelöschte Accounts: " + accountLoader.getDeleteAccountList().size());

        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j, chainInteractions);
        subscriptionTX.run(defaultSettingsLoader.getDefaultSettings().getResetIntervall());

*/

    }


    //TODO für CML init Methode schreiben

    /**
     * Auführbare Methode fürs Aufsetzten der Blockchain
     * Hier wird der certifyer deployed
     * Die certify Adresse Gebrinted
     * Zertifizieren des JavaProgramm Accounts
     * @throws Exception
     */
    /*
    public static void init() throws Exception {

        AccountLoader accountLoader =  AccountLoader.getInstance();

        DefaultSettingsLoader defaultSettingsLoader = DefaultSettingsLoader.getInstance();
        System.out.println("Anzahl Accounts: " + accountLoader.getAccountArrayList().size());


        defaultSettingsLoader.loadDefaultSettings();

        //TODO Settings in DfaultSettings File speichern
        //TODO Singleton parameter übergeben
        ChainSetUp chainSetUp =  ChainSetUp.getInstance();
        chainSetUp.initChain();

        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

        Web3j web3j = chainSetUp.getWeb3j();

        accountLoader.loadAccounts();
        defaultSettingsLoader.setCertifierAddress(chainSetUp.getCertifierAddress());
        //TODO Register ADD speichern
        chainInteractions.certifyAccountList(accountLoader.getAccountArrayList());
        chainInteractions.revokeAccountList(accountLoader.getDeleteAccountList());


        System.out.println("Anzahl zertifizierte Accounts: " + accountLoader.getAccountArrayList().size());
        System.out.println("Anzahl gesperrte Accounts: " + accountLoader.getRevokedAccountArrayList().size());
        System.out.println("Anzahl gelöschte Accounts: " + accountLoader.getDeleteAccountList().size());
        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j, chainInteractions);
        subscriptionTX.run(defaultSettingsLoader.getDefaultSettings().getResetIntervall());

    }

     */

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

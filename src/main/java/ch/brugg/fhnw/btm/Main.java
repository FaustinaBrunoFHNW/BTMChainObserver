package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.dosAlgorithm.DoSAlgorithm;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        jsonDefaultSettingsHandler.loadDefaultSettings();

        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();

        jsonDefaultSettingsHandler.writeDefaultSettings();
        log.info("Schreiben von Accounts Accounts");
        jsonAccountHandler.writeAccountList();



        log.info("Anzahl Accounts: " + jsonAccountHandler.getAccountList().size());
        jsonDefaultSettingsHandler.loadDefaultSettings();




        ChainSetup.getInstance().setUpAfterChainStart();
        //       chainSetUp.initChain();
        ChainInteractions chainInteractions = new ChainInteractions(ChainSetup.getInstance());

        Web3j web3j = ChainSetup.getInstance().getWeb3j();

        //TODO load all accounts from list
        jsonAccountHandler.loadAccounts();
        chainInteractions.certifyAccountList(jsonAccountHandler.getAccountList());

        System.out.println("Anzahl zertifizierte Accounts: " + jsonAccountHandler.getAccountList().size());
        System.out.println("Anzahl gesperrte Accounts: " + jsonAccountHandler.getRevokedAccounts());
        System.out.println("Anzahl gelöschte Accounts: " + jsonAccountHandler.getDeletedAccounts());

        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());





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

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        ChainSetup chainSetUup =  ChainSetup.getInstance();
        chainSetUup.initChain();

        ChainInteractions chainInteractions = new ChainInteractions(chainSetUup);
        SubscriptionTX subscriptionTX = new SubscriptionTX( chainInteractions);
        subscriptionTX.run(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());

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

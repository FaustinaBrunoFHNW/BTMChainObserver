package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO evtl Klasse unbennennen

/**
 * In dieser Klasse befinden sich die 3 Befehle die der Benutzer über die Command Line aufrufen kann
 *
 * @Author Faustina Bruno, Serhe-Jurij Maikoff
 */
public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    //TODO diese methode entfernen
    public static void main(String[] args) throws Exception {

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        jsonDefaultSettingsHandler.loadDefaultSettings();

        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();

        jsonDefaultSettingsHandler.writeDefaultSettings();
        log.info("Schreiben von Accounts Accounts");
        jsonAccountHandler.writeAccountList();

        log.info("Anzahl Accounts: " + jsonAccountHandler.getJsonAccountList().size());
        jsonDefaultSettingsHandler.loadDefaultSettings();

        ChainSetup.getInstance().setUpAfterChainStart();
        //       chainSetUp.initChain();
        ChainInteractions chainInteractions = new ChainInteractions(ChainSetup.getInstance());

        //TODO load all accounts from list
        jsonAccountHandler.loadAccounts();
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());

        System.out.println("Anzahl zertifizierte Accounts: " + jsonAccountHandler.getJsonAccountList().size());
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
     *
     * @throws Exception
     */
    public static void init() throws Exception {

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        ChainSetup chainSetUup = ChainSetup.getInstance();
        chainSetUup.initChain();

        ChainInteractions chainInteractions = new ChainInteractions(chainSetUup);
        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());

    }

    //TODO für CML run Methode schreiiben

    /**
     * Ausführbare Methode über CMD für das laufen lassen des Programms
     * Default Settings laden
     * Alle Accounts aus dem File einlesen
     * Alle Accounts Zertifizieren
     * Intervall starten
     *
     * @throws Exception
     */
    public static void run() throws Exception {
        //Default Settings laden
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        //Accounts laden gegebenfalls löschen
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();

        log.info("Anzahl Accounts: " + jsonAccountHandler.getJsonAccountList().size());

        ChainSetup.getInstance().setUpAfterChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(ChainSetup.getInstance());
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());

        System.out.println("Anzahl zertifizierte Accounts: " + jsonAccountHandler.getJsonAccountList().size());
        System.out.println("Anzahl gesperrte Accounts: " + jsonAccountHandler.getRevokedAccounts());
        System.out.println("Anzahl gelöschte Accounts: " + jsonAccountHandler.getDeletedAccounts());

        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());

    }

    //TODO für CML stop Methode schreiiben

    /**
     * Ausführbare Methode fürs stoppen aller Vorgänge und das Revoken aller Accounts
     *
     * @throws Exception
     */
    public static void stop() throws Exception {
        //Default Settings laden
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        //Accaounts laden gegebenfalls löschen
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();

        log.info("Anzahl Accounts: " + jsonAccountHandler.getJsonAccountList().size());

        ChainSetup.getInstance().setUpAfterChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(ChainSetup.getInstance());
        chainInteractions.revokeAccountList((jsonAccountHandler.getJsonAccountList()));
        log.info("Anzahl Accounts: " + jsonAccountHandler.getJsonAccountList().size());
        //TODO Chain Stoppen
        //TODO Subscription/Filter stoppen
        //TODO interval stoppen
    }

}

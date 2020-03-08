package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO anderes JAvadoc
/**
 * In dieser Klasse befinden sich die 3 Befehle die der Benutzer über die Command Line aufrufen kann
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class Run {
    private static Logger log = LoggerFactory.getLogger(Run.class);

    /**
     * Ausführbare Methode über CMD für das laufen lassen des Programms
     * Default Settings laden
     * Alle Accounts aus dem File einlesen
     * Alle Accounts Zertifizieren
     * Intervall starten
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

//        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
//
//        jsonDefaultSettingsHandler.loadDefaultSettings();
//
//        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
//        jsonAccountHandler.loadAccounts();
//
//        jsonDefaultSettingsHandler.writeDefaultSettings();
//        log.info("Schreiben von Accounts Accounts");
//        jsonAccountHandler.writeAccountList();
//
//        log.info("Anzahl Accounts: " + jsonAccountHandler.getJsonAccountList().size());
//        jsonDefaultSettingsHandler.loadDefaultSettings();
//
//        ChainSetup.getInstance().setUpAfterChainStart();
//        //       chainSetUp.initChain();
//        ChainInteractions chainInteractions = new ChainInteractions(ChainSetup.getInstance());
//
//        //TODO load all accounts from list
//        jsonAccountHandler.loadAccounts();
//        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());
//
//        System.out.println("Anzahl zertifizierte Accounts: " + jsonAccountHandler.getJsonAccountList().size());
//        System.out.println("Anzahl gesperrte Accounts: " + jsonAccountHandler.getRevokedAccounts());
//        System.out.println("Anzahl gelöschte Accounts: " + jsonAccountHandler.getDeletedAccounts());
//
//        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
//        subscriptionTX.run(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());

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
        subscriptionTX.run();

    }




}

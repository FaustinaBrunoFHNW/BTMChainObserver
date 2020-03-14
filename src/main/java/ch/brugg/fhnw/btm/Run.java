package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO anderes JAvadoc

/**
 * In dieser Klasse befinden sich der Befehl welcher der benutzer für
 * das Starten des Programms ausführen kann
 *
 * @Author Faustina Bruno, Serge Jurij Maikoff
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
        log.info("Run Befehl wurde gestartet");
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();


        log.info("Anzahl Accounts: " + jsonAccountHandler.getJsonAccountList().size());

        ChainSetup.getInstance().setUpAfterChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(ChainSetup.getInstance());
        chainInteractions.revokeAccountList(jsonAccountHandler.getJsonAccountsToDelete());
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());

        log.info("Anzahl zertifizierte Accounts: " + jsonAccountHandler.getJsonAccountList().size());
        log.info("Anzahl gesperrte Accounts: " + jsonAccountHandler.getRevokedAccounts());
        log.info("Anzahl gelöschte Accounts: " + jsonAccountHandler.getDeletedAccounts());

        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();

    }

}

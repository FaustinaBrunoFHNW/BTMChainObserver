package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In dieser Klasse befinden sich der Befehl welcher der Benutzer
 * für das Initieren des Programms nach dem Starten der Blockchain
 * ausführen kann
 *
 * @Author Faustina Bruno, Serge Jurij Maikoff
 */
public class Init {
    private static Logger log = LoggerFactory.getLogger(Init.class);

    /**
     * Auführbare Methode fürs Aufsetzten der Blockchain
     * Hier wird der certifyer deployed
     * Die certify Adresse Gebrinted
     * Zertifizieren des JavaProgramm Accounts
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        log.info("Init Befehl wurde gestartet.");

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        ChainSetup chainSetUup = ChainSetup.getInstance();
        chainSetUup.initChain();

        ChainInteractions chainInteractions = new ChainInteractions(chainSetUup);
        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();

    }

}

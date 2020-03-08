package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//TODO javadoc
public class Stop {

    private static Logger log = LoggerFactory.getLogger(Stop.class);
    /**
     * Ausführbare Methode fürs stoppen aller Vorgänge und das Revoken aller Accounts
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
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

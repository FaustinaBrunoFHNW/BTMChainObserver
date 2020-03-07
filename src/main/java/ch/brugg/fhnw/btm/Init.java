package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;

public class Init {
    //TODO für CML init Methode schreiben

    /**
     * Auführbare Methode fürs Aufsetzten der Blockchain
     * Hier wird der certifyer deployed
     * Die certify Adresse Gebrinted
     * Zertifizieren des JavaProgramm Accounts
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {


        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        ChainSetup chainSetUup = ChainSetup.getInstance();
        chainSetUup.initChain();

        ChainInteractions chainInteractions = new ChainInteractions(chainSetUup);
        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());

    }




}

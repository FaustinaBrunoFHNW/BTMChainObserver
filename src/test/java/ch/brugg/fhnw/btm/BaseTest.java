package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.ResetHelper;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BaseTest {

     SendEtherHelper sendEtherHelper;
    private ChainInteractions chainInteractions;
    private ResetHelper resetHelper= new ResetHelper();
    static String ADDRESS = "0x3e7Beee9585bA4526e8a7E41715D93B2bE014B34";
    static BigInteger GASPRICEZERO = new BigInteger("0");
    static BigInteger GASLIMIT = new BigInteger("21000");
    JsonDefaultSettingsHandler jsonDefaultSettingsHandler;

    public void setUpChain() throws Exception {
         jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        ChainSetup chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
        chainInteractions = new ChainInteractions(chainSetup);
        resetHelper.setAccountsCountersToMax();
        sendEtherHelper = new SendEtherHelper();
        sendEtherHelper.sendEtherFromTransaktionManager(ADDRESS, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());

        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();
    }

    public SendEtherHelper getSendEtherHelper() {
        return sendEtherHelper;
    }

    public void setSendEtherHelper(SendEtherHelper sendEtherHelper) {
        this.sendEtherHelper = sendEtherHelper;
    }

    public ChainInteractions getChainInteractions() {
        return chainInteractions;
    }

    public void setChainInteractions(ChainInteractions chainInteractions) {
        this.chainInteractions = chainInteractions;
    }

    public ResetHelper getResetHelper() {
        return resetHelper;
    }

    public void setResetHelper(ResetHelper resetHelper) {
        this.resetHelper = resetHelper;
    }

    public static String getADDRESS() {
        return ADDRESS;
    }

    public static void setADDRESS(String ADDRESS) {
        BaseTest.ADDRESS = ADDRESS;
    }

    public static BigInteger getGASPRICEZERO() {
        return GASPRICEZERO;
    }

    public static void setGASPRICEZERO(BigInteger GASPRICEZERO) {
        BaseTest.GASPRICEZERO = GASPRICEZERO;
    }

    public static BigInteger getGASLIMIT() {
        return GASLIMIT;
    }

    public static void setGASLIMIT(BigInteger GASLIMIT) {
        BaseTest.GASLIMIT = GASLIMIT;
    }
}

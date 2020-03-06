package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;

public class AccountCertifyTest {

    private ChainSetup chainSetup;
    private ChainInteractions chainInteractions;
    private JsonAccountHandler jsonAccountHandler;

    @BeforeClass public void setUpChain() throws Exception {
        chainSetup = ChainSetup.getInstance();
        chainInteractions = new ChainInteractions(chainSetup);

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        jsonAccountHandler = JsonAccountHandler.getInstance();

        ChainSetup.getInstance().setUpAfterChainStart();
    }

    @Test public void certifyAccount() {
        Assert.assertEquals(0, jsonAccountHandler.getJsonAccountList().size());
        String accountAddress = "";
        Assert.assertFalse(chainInteractions.isCertified(accountAddress));
        chainInteractions.certifyAccount(accountAddress);
        Assert.assertTrue(chainInteractions.isCertified(accountAddress));
        Assert.assertEquals(1, jsonAccountHandler.getJsonAccountList().size());
    }

}

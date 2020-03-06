package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RevokeAccountTest {

    private ChainSetup chainSetup;
    private ChainInteractions chainInteractions;
    private JsonAccountHandler jsonAccountHandler;

    @BeforeClass public void setUpChain() throws Exception {
        chainSetup = ChainSetup.getInstance();
        chainInteractions = new ChainInteractions(chainSetup);

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());
        ChainSetup.getInstance().setUpAfterChainStart();
    }

    @Test public void certifyAccount() {
        Assert.assertEquals(3, jsonAccountHandler.getJsonAccountList().size());
        String accountAddress = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        Assert.assertTrue(chainInteractions.isCertified(accountAddress));
        chainInteractions.revokeAccount(accountAddress);
        Assert.assertFalse(chainInteractions.isCertified(accountAddress));
        Assert.assertEquals(2, jsonAccountHandler.getJsonAccountList().size());
    }
}

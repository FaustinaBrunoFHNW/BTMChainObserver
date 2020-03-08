package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;

public class AccountCertifyTest {


    @Test public void certifyAccountTest() throws Exception {

        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        jsonDefaultSettingsHandler.loadDefaultSettings();
        ChainSetup chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(chainSetup);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();
        jsonAccountHandler.writeAccountList();

        JsonAccount account= new JsonAccount();
        account.setAddress("0xf13264C4bD595AEbb966E089E99435641082ff24");

        chainInteractions.revokeAccount(account.getAddress());
        Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
        //TODO do gratis TX asser false
        chainInteractions.certifyAccount(account.getAddress());
        //TODO do gratis TX asser true
        Assert.assertTrue(chainInteractions.isCertified(account.getAddress()));

        }

}

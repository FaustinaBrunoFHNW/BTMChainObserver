package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.ResetHelper;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransactionManagerAccountNotRevokeTest {

    private static SendEtherHelper sendEtherHelper;
    private static ResetHelper resetHelper = new ResetHelper();
    private static ChainInteractions chainInteractions;
    private static String TOADDRESS = "0xaf02DcCdEf3418F8a12f41CB4ed49FaAa8FD366b";

    private static BigInteger GASPRICEZERO = new BigInteger("0");
    private static BigInteger GASLIMIT = new BigInteger("21000");

    @BeforeClass public static void setUpChain() throws Exception {
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        ChainSetup chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
        chainInteractions = new ChainInteractions(chainSetup);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();
        jsonAccountHandler.writeAccountList();
        resetHelper.setAccountsCountersToMax();
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());
        sendEtherHelper = new SendEtherHelper();
        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();
    }

    @After
    @Before public void reset() throws InterruptedException {
        Thread.sleep(30000);
        resetHelper.setAccountsCountersToMax();
    }

    @Test public void tmOneTxMoreThenLimit() throws Exception {


        JsonAccount account = new JsonAccount();
        account.setAddress(TOADDRESS);
        BigDecimal ether = new BigDecimal("1");
        sendEtherHelper.txLoop(6, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Thread.sleep(15000);
        Assert.assertTrue(chainInteractions.isCertified(account.getAddress()));

    }

}

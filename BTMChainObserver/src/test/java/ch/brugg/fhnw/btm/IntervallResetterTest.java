package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.ResetHelper;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntervallResetterTest  {

    private static SendEtherHelper sendEtherHelper;
    private static ChainInteractions chainInteractions;
    private static ResetHelper resetHelper = new ResetHelper();
    private static ChainSetup chainSetup;
    private static String ADDRESS = "0x3e7Beee9585bA4526e8a7E41715D93B2bE014B34";
    private static BigInteger GASPRICEZERO = new BigInteger("0");
    private static BigInteger GASLIMIT = new BigInteger("21000");
    private static JsonDefaultSettingsHandler jsonDefaultSettingsHandler;

    @BeforeClass public static void startUp() throws Exception {
        jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
        chainInteractions = new ChainInteractions(chainSetup);
        JsonAccountHandler jsonAccountHandler= JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();
        resetHelper.setAccountsCountersToMax();

        sendEtherHelper = new SendEtherHelper();
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());
        sendEtherHelper.sendEtherFromTransaktionManager(ADDRESS, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);

        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();
    }
    @Before
    @After
    public void reset() throws InterruptedException {
        Thread.sleep(30000);
        resetHelper.setAccountsCountersToMax();
    }

    @Test public void transactionReset() throws Exception {

        Thread.sleep(2000);
        JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
        JsonAccount account = accountHandler.getAccount(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        int anzahltTransaktionen = 2;
        Assert.assertEquals(account.getTransactionLimit().intValue(), account.getRemainingTransactions());
        sendEtherHelper.txLoop(anzahltTransaktionen, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Thread.sleep(15000);
        Assert.assertEquals(account.getTransactionLimit().intValue() - anzahltTransaktionen,
                account.getRemainingTransactions());
        Thread.sleep(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval() * 100000);
        Assert.assertEquals(account.getTransactionLimit().intValue(), account.getRemainingTransactions());
        Thread.sleep(2000);
        resetHelper.setAccountsCountersToMax();
    }

    @Test public void gasReset() throws Exception {
        resetHelper.setAccountsCountersToMax();
        Thread.sleep(2000);
        JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
        JsonAccount account = accountHandler.getAccount(ADDRESS);
        BigDecimal ether = new BigDecimal("100");
        int anzahltTransaktionen = 1;
        Assert.assertEquals(42000, account.getRemainingGas());
        sendEtherHelper.txLoop(anzahltTransaktionen, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Thread.sleep(15000);
        Assert.assertEquals(21000, account.getRemainingGas());
        Thread.sleep(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval() * 100000);
        Assert.assertEquals(42000, account.getRemainingGas());
        Thread.sleep(2000);
        resetHelper.setAccountsCountersToMax();
    }
}

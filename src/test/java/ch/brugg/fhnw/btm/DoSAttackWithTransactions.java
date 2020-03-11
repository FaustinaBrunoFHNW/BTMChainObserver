package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.SubscriptionTX;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.ResetHelper;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.*;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Performance Tests für DoS Attacken mit viele Transaktionen
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class DoSAttackWithTransactions {
    private static  SendEtherHelper sendEtherHelper;
    private static ResetHelper resetHelper = new ResetHelper();
    private static ChainInteractions chainInteractions;
    private static String ADDRESS = "0x3e7Beee9585bA4526e8a7E41715D93B2bE014B34";
    private static String TOADDRESS = "0xaf02DcCdEf3418F8a12f41CB4ed49FaAa8FD366b";
    private static BigInteger GASPRICEZERO = new BigInteger("0");
    private static BigInteger GASLIMIT = new BigInteger("21000");

    @BeforeClass
    public static void setUpChain() throws Exception {
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
        sendEtherHelper.sendEtherFromTransaktionManager(ADDRESS, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);
        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();
    }

    @After
    public void reset() throws InterruptedException {
        Thread.sleep(5000);
        resetHelper.setAccountsCountersToMax();
    }
    /**
     * Test mir Randlimite unter der Limite
     *
     * @throws Exception
     */
    @Test public void txAttack4() throws Exception {
        Thread.sleep(5000);
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        sendEtherHelper.txLoop(4, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Assert.assertTrue(chainInteractions.isCertified(account.getAddress()));

    }

    /**
     * Test mir Randlimite genau der Limite
     *
     * @throws Exception
     */
    @Test public void txAttack5() throws Exception {
        Thread.sleep(5000);
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");

        sendEtherHelper.txLoop(5, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Assert.assertTrue(chainInteractions.isCertified(account.getAddress()));

    }

    /**
     * Test mir Randlimite über der limite
     *
     * @throws Exception
     */
    @Test public void txAttack6() throws Exception {
        Thread.sleep(5000);
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        try {
            Thread.sleep(1000);
            sendEtherHelper.txLoop(6, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(15000);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
            Thread.sleep(5000);
        }
    }

    @Test public void txAttack10() throws Exception {
        Thread.sleep(5000);
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");

        try {
            Thread.sleep(1000);
            sendEtherHelper.txLoop(10, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(15000);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));

        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
            Thread.sleep(5000);
        };
    }

    @Test public void txAttack100() throws Exception {
        Thread.sleep(5000);
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        try {
            Thread.sleep(1000);
            sendEtherHelper.txLoop(100, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(5000);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
            Thread.sleep(15000);
        }
    }

}

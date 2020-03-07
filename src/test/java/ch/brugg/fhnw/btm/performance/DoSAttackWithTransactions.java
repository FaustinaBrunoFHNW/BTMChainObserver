package ch.brugg.fhnw.btm.performance;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.Main;
import ch.brugg.fhnw.btm.SubscriptionTX;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Performance Tests für DoS Attacken mit viele Transaktionen
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class DoSAttackWithTransactions {
    private SendEtherHelper sendEtherHelper;
    private ChainInteractions chainInteractions;
    private static String ADDRESS = "0x3e7Beee9585bA4526e8a7E41715D93B2bE014B34";
    private static String TOADDRESS = "0xaf02DcCdEf3418F8a12f41CB4ed49FaAa8FD366b";
    private static BigInteger GASPRICEZERO = new BigInteger("0");
    private static BigInteger GASLIMIT = new BigInteger("21000");

    private void setUpChain() throws Exception {
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        ChainSetup chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
        chainInteractions = new ChainInteractions(chainSetup);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();
        jsonAccountHandler.writeAccountList();
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());
        sendEtherHelper = new SendEtherHelper();
        sendEtherHelper.sendEtherFromTransaktionManager(ADDRESS, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);
        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());
    }

    /**
     * Test mir Randlimite unter der Limite
     *
     * @throws Exception
     */
    @Test public void txAttack4() throws Exception {
        this.setUpChain();
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        sendEtherHelper.txLoop(4, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Assert.assertTrue(this.chainInteractions.isCertified(account.getAddress()));

    }

    /**
     * Test mir Randlimite genau der Limite
     *
     * @throws Exception
     */
    @Test public void txAttack5() throws Exception {
        this.setUpChain();
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");

        sendEtherHelper.txLoop(5, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Assert.assertTrue(this.chainInteractions.isCertified(account.getAddress()));

    }

    /**
     * Test mir Randlimite über der limite
     *
     * @throws Exception
     */
    @Test public void txAttack6() throws Exception {
        this.setUpChain();
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        try {
            Thread.sleep(1000);
            sendEtherHelper.txLoop(6, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(15000);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        }

    }

    @Test public void txAttack10() throws Exception {
        this.setUpChain();
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");

        try {
            Thread.sleep(1000);
            sendEtherHelper.txLoop(10, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(15000);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));

        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        }

    }

    @Test public void txAttack100() throws Exception {
        this.setUpChain();
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        try {
            Thread.sleep(1000);
            sendEtherHelper.txLoop(100, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(5000);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        }

    }

    @Test public void txAttack1000() throws Exception {
        this.setUpChain();
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        try {
            Thread.sleep(1000);
            sendEtherHelper.txLoop(1000, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        }

    }

}

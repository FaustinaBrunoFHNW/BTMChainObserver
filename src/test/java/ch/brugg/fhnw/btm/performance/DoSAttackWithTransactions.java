package ch.brugg.fhnw.btm.performance;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.Main;
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
    }

    /**
     * Test mir Randlimite unter der Limite
     *
     * @throws Exception
     */
    @Test public void txAttack4() throws Exception {
        this.setUpChain();
        JsonAccount account= new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("543");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        Thread.sleep(5000);
        sendEtherHelper.txLoop(4, account.getAddress(), ether, gasPrice, gasLimit);
        Assert.assertTrue(this.chainInteractions.isCertified(account.getAddress()));
    }

    /**
     * Test mir Randlimite über der limite
     *
     * @throws Exception
     */
    @Test public void txAttack6() throws Exception {
        this.setUpChain();
        JsonAccount account= new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("543");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(6, account.getAddress(), ether, gasPrice, gasLimit);
        Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
    }

    @Test public void txAttack10() throws Exception {
        this.setUpChain();
        JsonAccount account= new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("543");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(10, account.getAddress(), ether, gasPrice, gasLimit);
        Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
    }

    @Test public void txAttack100000() throws Exception {
        this.setUpChain();
        JsonAccount account= new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("543");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, account.getAddress(), ether, gasPrice, gasLimit);
        Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
    }

    @Test public void txAttack1000000() throws Exception {
        this.setUpChain();
        JsonAccount account= new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("56");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, account.getAddress(), ether, gasPrice, gasLimit);
        Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
    }

    @Test public void txAttack10000000() throws Exception {
        this.setUpChain();
        JsonAccount account= new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("34");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, account.getAddress(), ether, gasPrice, gasLimit);
        Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
    }

    @Test public void txAttack100000000() throws Exception {
        this.setUpChain();
        JsonAccount account= new JsonAccount();
        account.setAddress(ADDRESS);
        BigDecimal ether = new BigDecimal("23");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, account.getAddress(), ether, gasPrice, gasLimit);
        Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
    }


}

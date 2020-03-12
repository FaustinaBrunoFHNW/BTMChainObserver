package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.*;
import ch.brugg.fhnw.btm.command.ResetAccountsCommand;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.ResetHelper;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class DoSAttackWithGas  {

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

    @Test public void gasAttack21000() throws Exception {

        BigDecimal ether = new BigDecimal("2");
        Thread.sleep(2000);
        sendEtherHelper.txLoop(1,ADDRESS,ether,GASPRICEZERO,GASLIMIT);
        Thread.sleep(15000);
        Assert.assertTrue(chainInteractions.isCertified(ADDRESS));

    }
    @Test public void gasAttack42000() throws Exception {

        BigDecimal ether = new BigDecimal("2");
        Thread.sleep(2000);
        sendEtherHelper.txLoop(2,ADDRESS,ether,GASPRICEZERO,GASLIMIT);
        Thread.sleep(15000);
        Assert.assertTrue(chainInteractions.isCertified(ADDRESS));
    }
    @Test public void gasAttack10500() throws Exception {

        BigDecimal ether = new BigDecimal("2");
        Thread.sleep(2000);
        try {
            sendEtherHelper.txLoop(5,ADDRESS,ether,GASPRICEZERO,GASLIMIT);
            Thread.sleep(15000);
            Assert.assertFalse(chainInteractions.isCertified(ADDRESS));
        }
        catch (RuntimeException e){Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(ADDRESS));
            }


    }


}

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

public class DoSAttackWithGas extends BaseTest {



    @Test public void gasAttack21000() throws Exception {
        this.setUpChain();
        BigDecimal ether = new BigDecimal("2");
        Thread.sleep(1000);
        sendEtherHelper.txLoop(1,ADDRESS,ether,GASPRICEZERO,GASLIMIT);
        Thread.sleep(15000);
        Assert.assertTrue(chainInteractions.isCertified(ADDRESS));
    }
    @Test public void gasAttack42000() throws Exception {
        this.setUpChain();
        BigDecimal ether = new BigDecimal("2");
        Thread.sleep(1000);
        sendEtherHelper.txLoop(2,ADDRESS,ether,GASPRICEZERO,GASLIMIT);
        Thread.sleep(15000);
        Assert.assertTrue(chainInteractions.isCertified(ADDRESS));
        resetHelper.setAccountsCountersToMax();
    }
    @Test public void gasAttack10500() throws Exception {
        this.setUpChain();
        BigDecimal ether = new BigDecimal("2");
        Thread.sleep(1000);
        try {
            sendEtherHelper.txLoop(5,ADDRESS,ether,GASPRICEZERO,GASLIMIT);
            Thread.sleep(15000);
            Assert.assertFalse(chainInteractions.isCertified(ADDRESS));
        }
        catch (RuntimeException e){Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(ADDRESS));
            }
        resetHelper.setAccountsCountersToMax();
    }


}

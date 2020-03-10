package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDoSAttackTest extends BaseTest {
    private static String PRIVATE_KEY = "2D4E070C7586E271AE9DB6C2F44E154A5BCBEDFBC6AA7BEFD2A7E9EACAC2BFFE";
    static String ADDRESS_TXLIMITHIGH = "0x2063Cfc6a737a9033459014C0ad444a1ae02d2DB";


    @Test public void txAttack500() throws Exception {
        Thread.sleep(1000*60*3);
        this.setUpChain();
        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS_TXLIMITHIGH);
        this.sendEtherFromTransaktionManager(ADDRESS_TXLIMITHIGH,new BigDecimal("10000"),GASPRICEZERO,GASLIMIT);
        BigDecimal ether = new BigDecimal("1");
        try {
            Thread.sleep(1000);
            this.txLoop(500, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(40000);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(this.chainInteractions.isCertified(account.getAddress()));
        }
       resetHelper.setAccountsCountersToMax();
    }

    public TransactionReceipt sendEtherFromTransaktionManager(String addresseTo, BigDecimal etherValue,
            BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Transfer transfer = new Transfer(chainSetup.getWeb3j(), chainSetup.getTransactionManager());
        return transfer.sendFunds(addresseTo, etherValue, Convert.Unit.ETHER, gasPrice, gasLimit).send();

    }

    private TransactionReceipt sendEther(String addresseTo, BigDecimal etherValue, BigInteger gasPrice,
            BigInteger gasLimit) throws Exception {
        TransactionManager transactionManager = new RawTransactionManager(chainSetup.getWeb3j(),
                this.getCredentialsFromPrivateKey());
        Transfer transfer = new Transfer(chainSetup.getWeb3j(), transactionManager);
        return transfer.sendFunds(addresseTo, etherValue, Convert.Unit.ETHER, gasPrice, gasLimit).send();
    }

    public void txLoop(int loopCount, String addressTo, BigDecimal etherValue, BigInteger gasPrice, BigInteger gasLimit)
            throws Exception {
        int counter = 0;
        while (counter < loopCount) {
            System.out.println(
                    counter + ". Transaktion: " + this.sendEther(addressTo, etherValue, gasPrice, gasLimit).toString());
            counter++;

        }

    }

    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }
}

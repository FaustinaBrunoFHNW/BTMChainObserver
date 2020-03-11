package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.ResetHelper;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDoSAttackTest {
    private static String PRIVATE_KEY = "2D4E070C7586E271AE9DB6C2F44E154A5BCBEDFBC6AA7BEFD2A7E9EACAC2BFFE";
    private static String ADDRESS_TXLIMITHIGH = "0x2063Cfc6a737a9033459014C0ad444a1ae02d2DB";

    private static SendEtherHelper sendEtherHelper;
    private static ChainInteractions chainInteractions;
    private static ResetHelper resetHelper = new ResetHelper();
    private static ChainSetup chainSetup;
    private static String ADDRESS = "0x3e7Beee9585bA4526e8a7E41715D93B2bE014B34";
    private static BigInteger GASPRICEZERO = new BigInteger("0");
    private static BigInteger GASLIMIT = new BigInteger("21000");
    private static JsonDefaultSettingsHandler jsonDefaultSettingsHandler;

    @BeforeClass public static void setUpChain() throws Exception {
        jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
        chainInteractions = new ChainInteractions(chainSetup);
        resetHelper.setAccountsCountersToMax();

        sendEtherHelper = new SendEtherHelper();
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        chainInteractions.certifyAccountList(jsonAccountHandler.getJsonAccountList());
        sendEtherHelper.sendEtherFromTransaktionManager(ADDRESS, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);

        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();
    }

    @Before public void reset() throws InterruptedException {
        Thread.sleep(5000);
        resetHelper.setAccountsCountersToMax();
    }

    @Test public void txAttack500() throws Exception {

        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS_TXLIMITHIGH);
       sendEtherFromTransaktionManager(ADDRESS_TXLIMITHIGH, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);
       Thread.sleep(5000);
        BigDecimal ether = new BigDecimal("1");
        EthGetBalance balanceWeiBefore = chainSetup.getWeb3j()
                .ethGetBalance(ADDRESS, DefaultBlockParameterName.LATEST).send();
        System.out.println("**********************Balance before: "+balanceWeiBefore.getBalance().intValue());
        try {
            Thread.sleep(1000);
            this.txLoop(1000, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(40000);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
            EthGetBalance balanceWeiAfter = chainSetup.getWeb3j()
                    .ethGetBalance(ADDRESS, DefaultBlockParameterName.LATEST)
                    .send();
            System.out.println("***********************Balance After: "+balanceWeiAfter.getBalance().intValue());
            Assert.assertEquals(balanceWeiBefore.getBalance().intValue()-450,balanceWeiAfter.getBalance().intValue());

        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
            EthGetBalance balanceWeiAfter = chainSetup.getWeb3j()
                    .ethGetBalance(ADDRESS, DefaultBlockParameterName.LATEST)
                    .send();
            System.out.println("***********************Balance After: "+balanceWeiAfter.getBalance().intValue());
            Assert.assertEquals(balanceWeiBefore.getBalance().intValue()-450,balanceWeiAfter.getBalance().intValue());
        }
    }

    private TransactionReceipt sendEtherFromTransaktionManager(String addresseTo, BigDecimal etherValue,
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

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
import java.sql.Timestamp;

public class BigDoSAttackTest {
    private static String PRIVATE_KEY = "C2931976ACD9A18F03F7002BCE6FB4C43D75D6368BD4EA6B6B5CBC45021F611A";
    private static String ADDRESS_TXLIMITHIGH = "0xD4810f6177e8BD64a4156e1f439ac202E9Ffca22";

    private static SendEtherHelper sendEtherHelper;
    private static ChainInteractions chainInteractions;
    private static ResetHelper resetHelper = new ResetHelper();
    private static ChainSetup chainSetup;
    private static String TO_ADDRESS = "0xe0280100D5B3f35d771903B1E6f6eA253977E56B";
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
        sendEtherHelper.sendEtherFromTransaktionManager(ADDRESS_TXLIMITHIGH, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);

        SubscriptionTX subscriptionTX = new SubscriptionTX(chainInteractions);
        subscriptionTX.run();
    }

    @After
    @Before public void reset() throws InterruptedException {
        Thread.sleep(30000);
        resetHelper.setAccountsCountersToMax();
    }

    @Test public void txAttack500() throws Exception {

        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS_TXLIMITHIGH);
    //  sendEtherFromTransaktionManager(ADDRESS_TXLIMITHIGH, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);
       Thread.sleep(5000);
        BigDecimal ether = new BigDecimal("1");
        try {
            Thread.sleep(1000);
            this.txLoop(500, TO_ADDRESS, ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(50000);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
              }
    }

    @Test public void txAttack2000() throws Exception {

        JsonAccount account = new JsonAccount();
        account.setAddress(ADDRESS_TXLIMITHIGH);
       // sendEtherFromTransaktionManager(ADDRESS_TXLIMITHIGH, new BigDecimal("10000"), GASPRICEZERO, GASLIMIT);
        Thread.sleep(5000);
        BigDecimal ether = new BigDecimal("1");
       try {
            Thread.sleep(1000);
            this.txLoop(5000, TO_ADDRESS, ether, GASPRICEZERO, GASLIMIT);
            Thread.sleep(50000);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
            EthGetBalance balanceWeiAfter = chainSetup.getWeb3j()
                    .ethGetBalance(ADDRESS_TXLIMITHIGH, DefaultBlockParameterName.LATEST)
                    .send();

        } catch (RuntimeException e) {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
            Assert.assertFalse(chainInteractions.isCertified(account.getAddress()));
            EthGetBalance balanceWeiAfter = chainSetup.getWeb3j()
                    .ethGetBalance(ADDRESS_TXLIMITHIGH, DefaultBlockParameterName.LATEST)
                    .send();
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
        Timestamp tempStamp;
        while (counter < loopCount) {
            this.sendEther(addressTo, etherValue, gasPrice, gasLimit);
            System.out.println(counter);
//                    counter + ". Transaktion: " + this.sendEther(addressTo, etherValue, gasPrice, gasLimit).toString());
            counter++;
            tempStamp = new Timestamp(System.currentTimeMillis());
            System.out.println(tempStamp);

        }

    }

    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }
}

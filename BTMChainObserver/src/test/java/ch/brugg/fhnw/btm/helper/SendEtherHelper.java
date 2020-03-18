package ch.brugg.fhnw.btm.helper;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SendEtherHelper {
    private static String PRIVATE_KEY = "FD825395E5B421CE6DDCEDBE4205FAB1F916E73E56721D5FD4D49C540BA2DB94";

    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
    private ChainSetup chainSetup = ChainSetup.getInstance();

    public TransactionReceipt sendEtherFromTransaktionManager(String addresseTo, BigDecimal etherValue,
            BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Transfer transfer = new Transfer(chainSetup.getWeb3j(), chainSetup.getTransactionManager());
        return transfer.sendFunds(addresseTo, etherValue, Convert.Unit.ETHER, gasPrice, gasLimit).send();

    }

    private TransactionReceipt sendEther(String addresseTo, BigDecimal etherValue, BigInteger gasPrice,
            BigInteger gasLimit) throws Exception {
        TransactionManager transactionManager= new RawTransactionManager(chainSetup.getWeb3j(), this.getCredentialsFromPrivateKey());
        Transfer transfer= new Transfer(chainSetup.getWeb3j(),transactionManager);
        return transfer.sendFunds(addresseTo, etherValue, Convert.Unit.ETHER, gasPrice, gasLimit).send();
    }

    public void txLoop(int loopCount, String addressTo, BigDecimal etherValue, BigInteger gasPrice, BigInteger gasLimit)
            throws Exception {
        int counter = 0;
        while (counter < loopCount) {
            System.out.println(counter+". Transaktion: "+this.sendEther(addressTo, etherValue, gasPrice, gasLimit).toString());
            counter++;
        }
    }

    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }
}

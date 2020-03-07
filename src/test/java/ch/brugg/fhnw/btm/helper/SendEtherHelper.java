package ch.brugg.fhnw.btm.helper;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SendEtherHelper {
    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
    private ChainSetup chainSetup = ChainSetup.getInstance();

    public void sendEther(String addresseTo, BigDecimal etherValue, BigInteger gasPrice, BigInteger gasLimit)
            throws Exception {
        Transfer transfer = new Transfer(chainSetup.getWeb3j(), chainSetup.getTransactionManager());
        transfer.sendFunds(addresseTo, etherValue, Convert.Unit.ETHER, gasPrice, gasLimit).send();

    }

    public void txLoop(int loopCount, String addressTo, BigDecimal etherValue, BigInteger gasPrice,
            BigInteger gasLimit) throws Exception {
        int counter = 0;
        while (counter<loopCount) {
            this.sendEther(addressTo, etherValue, gasPrice, gasLimit);
            counter++;
        }
    }

}

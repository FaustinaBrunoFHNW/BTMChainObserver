package ch.brugg.fhnw.btm;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ChainInteractions {



    private static TransactionReceipt sendEtherToAccount(Web3j web3j, BigInteger gasPrice, BigInteger gasLimit, String accountAddress, TransactionManager transactionManager) throws Exception {

        Transfer transfer = new Transfer(web3j, transactionManager);
        return transfer.sendFunds(accountAddress, new BigDecimal(1000), Convert.Unit.ETHER, gasPrice, gasLimit).send();
    }


}

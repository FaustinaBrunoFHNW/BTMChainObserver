package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ChainInteractions {

    private Logger log = LoggerFactory.getLogger(ChainInteractions.class);
    private ChainSetUp chainSetUp;
    private SimpleCertifier simpleCertifier;
    private TransactionManager transactionManager;
    private Web3j web3j;

    public ChainInteractions(ChainSetUp chainSetUp) {
        this.chainSetUp = chainSetUp;
        this.simpleCertifier = chainSetUp.getSimpleCertifier();
        this.web3j=chainSetUp.getWeb3j();
        this.transactionManager=chainSetUp.getTransactionManager();

    }

    //TODO JavaDoc
    public TransactionReceipt sendEtherToAccount(BigInteger gasPrice, BigInteger gasLimit,
            String accountAddress ) throws Exception {
        this.log.info("Methode sendEtherToAccount wurde aufgerufen.");
        Transfer transfer = new Transfer(this.web3j, this.transactionManager);
        return transfer.sendFunds(accountAddress, new BigDecimal(1000), Convert.Unit.ETHER, gasPrice, gasLimit).send();
    }

    //TODO java doc
    public boolean revokeAccount(String accountAddress) throws Exception {
        this.log.info("Methode revokeAccount wurde aufgerufen. Folgender Account wird aus der Whiteliste entfernt: "
                + accountAddress);
        this.simpleCertifier.revoke(accountAddress);
        if (this.simpleCertifier.certified(accountAddress).send()) {
            this.log.info(accountAddress+" wurde nicht erfolgreich aus der Whiteliste entfernt");
            return false;
        }
        this.log.info(accountAddress+" wurde erfolgreich aus der Whiteliste entfernt");
        return true;
    }

}

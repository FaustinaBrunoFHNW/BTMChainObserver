package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
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
    private SimpleRegistry simpleRegistry;
    private TransactionManager transactionManager;
    private Web3j web3j;

    //TODO JavaDoc für Constructor
    public ChainInteractions(ChainSetUp chainSetUp) {
        this.chainSetUp = chainSetUp;
        this.simpleCertifier = chainSetUp.getSimpleCertifier();
        this.simpleRegistry = chainSetUp.getSimpleRegistry();
        this.web3j = chainSetUp.getWeb3j();
        this.transactionManager = chainSetUp.getTransactionManager();

    }

    //TODO JavaDoc
    public TransactionReceipt sendEtherToAccount(BigInteger gasPrice, BigInteger gasLimit, String accountAddress)
            throws Exception {
        this.log.info("Methode sendEtherToAccount wurde aufgerufen.");
        Transfer transfer = new Transfer(this.web3j, this.transactionManager);
        return transfer.sendFunds(accountAddress, new BigDecimal(1000), Convert.Unit.ETHER, gasPrice, gasLimit).send();
    }

    //TODO java doc
    public boolean revokeAccount(String accountAddress)   {
        this.log.info("Methode revokeAccount wurde aufgerufen. Folgender Account wird aus der Whiteliste entfernt: "
                + accountAddress);

        try {
            this.simpleCertifier.revoke(accountAddress).send();
           // log.info(simpleRegistry.getAddress(this.chainSetUp.getHash(), "A").send());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!this.isCertified(accountAddress)) {
            this.log.info(accountAddress + " wurde  erfolgreich aus der Whiteliste entfernt");
            return true;
        }
        this.log.info(accountAddress + " wurde NICHT erfolgreich aus der Whiteliste entfernt");
        return false;
    }

    //TODO JavaDoc
    public boolean certifyAccount(String add) {

        try {
            log.info("Certifying Account mit folgender Adresse: " + add);
            this.simpleCertifier.certify(add).send();
            log.info(simpleRegistry.getAddress(this.chainSetUp.getHash(), "A").send());
            log.info("Done certifying account");
            return isCertified(add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //TODO JavaDoc
    public boolean isCertified(String add) {
        try {
            return simpleCertifier.certified(add).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
import ch.brugg.fhnw.btm.dosAlgorithm.DoSAlgorithm;
import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * In dieser Klasse sind alle Interaktionen mit der Blockchain über den SimpleCertifier
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class ChainInteractions {

    private Logger log = LoggerFactory.getLogger(ChainInteractions.class);
    private ChainSetup chainSetUp;
    private SimpleCertifier simpleCertifier;
    private SimpleRegistry simpleRegistry;
    private TransactionManager transactionManager;
    private Web3j web3j;

    //TODO JavaDoc für Constructor
    public ChainInteractions(ChainSetup chainSetUp) {
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

    /**
     * In dieser Methode werden alle Account einer Liste von der Whiteliste entfernt
     * @param accounts Liste mit Accounts
     */
    public void revokeAccountList(ArrayList<Account> accounts){
        log.info("Alle Accounts von Liste werden revoked");
        for (Account account : accounts) {
            log.info("Revoked: "+account.getAddress());
            this.revokeAccount(account.getAddress());
        }
    }


    public boolean revokeAccount(String accountAddress)   {
        this.log.info("Methode revokeAccount wurde aufgerufen. Folgender Account wird aus der Whiteliste entfernt: "
                + accountAddress);

        try {
            this.simpleCertifier.revoke(accountAddress).send();
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

    /**
     * In dieser Methode werden alle Account einer Liste in die Whiteliste integriert
     * @param accounts Liste mit Accounts
     */
    public void certifyAccountList(ArrayList<Account> accounts) {

        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (Account acc : accounts) {
            if (acc.getTimeStamp() == null) {
                log.info("Account hat keinen TimeStamp und wurde in die Whitelist aufgenommen: " + acc.getAddress());
                certifyAccount(acc.getAddress());
            }
            else if(acc.getTimeStamp().before(now)){
                log.info("Account hat einen Timestamp, liegt aber in der Vergangenheit und "
                        + "wird in die Whitelist aufegnommen: " + acc.getAddress());
                acc.setTimeStamp(null);
                certifyAccount(acc.getAddress());
            }
            else if (acc.getTimeStamp().after(now)){
                log.info("Account hat einen TimeStamp in der Zukunft. Account ist gesperrt: " + acc.getAddress());
                DoSAlgorithm.getInstance().offerAccount(acc);
            }
        }
    }

    //TODO public kontrollieren

    /**
     * Hier wird ein Account in die Whiteliste hinzugefügt
     * @param add Adresse des Accounts welches in die Whiteliste hinzugefügt wird
     * @return es wird ausgegeben ob der Account in der Whiteliste hinzugefügt wurde
     */
    public boolean certifyAccount(String add) {

        try {
            log.info("zertifizierung des Accounts mit folgender Adresse: " + add);
            this.simpleCertifier.certify(add).send();
            log.info("Zertifizierung erfolgreich");
            return isCertified(add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //TODO public kontrollieren
    /**
     * Diese Methode gibts aus ob ein Account zertifiziert/in der Whiteliste ist
     * @param add Adresse des Accounts
     * @return boolean ob Adresse zertifiziert ist --> true oder eben nicht --> false
     */
    public boolean isCertified(String add) {
        try {
            return simpleCertifier.certified(add).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

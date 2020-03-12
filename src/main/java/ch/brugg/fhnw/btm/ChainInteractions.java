package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
import ch.brugg.fhnw.btm.dosAlgorithm.DoSAlgorithm;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
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



    /**
     * public Constructor der Klasse
     * @param chainSetUp
     */
    public ChainInteractions(ChainSetup chainSetUp) {
        this.chainSetUp = chainSetUp;
        this.simpleCertifier = chainSetUp.getSimpleCertifier();
        this.simpleRegistry = chainSetUp.getSimpleRegistry();
        this.web3j = chainSetUp.getWeb3j();
        this.transactionManager = chainSetUp.getTransactionManager();

    }


    /**
     * In dieser Methode werden alle Account einer Liste von der Whiteliste entfernt
     *
     * @param jsonAccounts Liste mit Accounts
     */
    public void revokeAccountList(ArrayList<JsonAccount> jsonAccounts) {
        this.log.info("Alle Accounts von Liste werden revoked");
        for (JsonAccount jsonAccount : jsonAccounts) {
            this.log.info("Revoked: " + jsonAccount.getAddress());
            this.revokeAccount(jsonAccount.getAddress());
        }
    }

    /**
     * Dieser Methode entfernt einen Account aus der Whitelist und kontrolliert ob es erfolgreich war
     *
     * @param accountAddress Adresse des zu sperrenden Accounts
     */
    public void revokeAccount(String accountAddress) {
        this.log.info("Methode revokeAccount wurde aufgerufen. Folgender Account wird aus der Whiteliste entfernt: "
                + accountAddress);

        try {
            this.simpleCertifier.revoke(accountAddress).send();
        } catch (Exception e) {

            this.log.error("Es gab ein Error, der Account konnte nicht von der Whitelist entfernt werden");
           e.printStackTrace();
        }

        if (!this.isCertified(accountAddress)) {
            this.log.info(accountAddress + " wurde  erfolgreich aus der Whiteliste entfernt");
        } else {
            this.log.info(accountAddress + " wurde NICHT erfolgreich aus der Whiteliste entfernt");
        }
    }

    /**
     * In dieser Methode werden alle geladenen Accounts geprüft ob sie einen Timestamp besitzten oder nicht
     * kein TimeStamp --> Account wird in Whiteliste aufgenommen
     * Timestamp in Vergangenheit --> Account wird in die Whiteliste aufgenommen
     * Timestamp in der zukunft --> Account ist gesperrt und wird in die PrioQue hinzugefügt
     *
     * @param jsonAccounts Liste mit Accounts
     */
    public void certifyAccountList(ArrayList<JsonAccount> jsonAccounts) {

        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (JsonAccount acc : jsonAccounts) {
            if (acc.getTimeStamp() == null) {
                this.log.info(
                        "Account hat keinen TimeStamp und wurde in die Whitelist aufgenommen: " + acc.getAddress());
                this.certifyAccount(acc.getAddress());
            } else if (acc.getTimeStamp().before(now)) {
                this.log.info("Account hat einen Timestamp, liegt aber in der Vergangenheit und "
                        + "wird in die Whitelist aufegnommen: " + acc.getAddress());
                acc.setTimeStamp(null);
                this.certifyAccount(acc.getAddress());
            } else if (acc.getTimeStamp().after(now)) {
                this.log.info("Account hat einen TimeStamp in der Zukunft. Account ist gesperrt: " + acc.getAddress());
                DoSAlgorithm.getInstance().offerAccount(acc);
            }
        }
    }


    /**
     * Hier wird ein Account in die Whiteliste hinzugefügt
     *
     * @param add Adresse des Accounts welches in die Whiteliste hinzugefügt wird
     * @return es wird ausgegeben ob der Account in der Whiteliste hinzugefügt wurde
     */
    public boolean certifyAccount(String add) {

        try {
            this.log.info("zertifizierung des Accounts mit folgender Adresse: " + add);
            this.simpleCertifier.certify(add).send();
            this.log.info("Zertifizierung erfolgreich");
            return this.isCertified(add);
        } catch (Exception e) {
            //TODO log error ausfüllen
            this.log.error("");
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Diese Methode gibts aus ob ein Account zertifiziert/in der Whiteliste ist
     *
     * @param add Adresse des Accounts
     * @return boolean ob Adresse zertifiziert ist --> true oder eben nicht --> false
     */
    public boolean isCertified(String add) {
        try {
            return this.simpleCertifier.certified(add).send();
        } catch (Exception e) {
            //TODO log error ausfüllen
            this.log.error("");
            e.printStackTrace();
        }
        return false;
    }

}

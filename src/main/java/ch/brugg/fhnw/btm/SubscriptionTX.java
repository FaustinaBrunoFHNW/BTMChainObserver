package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.loader.AccountLoader;
import ch.brugg.fhnw.btm.pojo.Account;
import ch.brugg.fhnw.btm.writer.AccountWriter;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//TODO Beschreischbung was diese Klasse macht
public class SubscriptionTX {

    //TODO limite nach Besprechung stellen
    private final static int TRANSAKTIONS_LIMITE = 3;
    private Web3j web3j;
    private Subscription subscription;
    private static Logger log = LoggerFactory.getLogger(SubscriptionTX.class);
    private AccountLoader accountLoader;
    private ChainInteractions chainInteractions;
    private AccountWriter accountWriter;

    public SubscriptionTX(Web3j web3j, ChainInteractions chainInteractions) {
        this.web3j = web3j;
        this.accountLoader = AccountLoader.getInstance();
        this.chainInteractions = chainInteractions;
        accountWriter = AccountWriter.getInstance();
    }

    //TODO JAVADOC
    public void run(int min) throws Exception {
        log.info("Doing simple subscription");
        this.txFilter();
        this.intervall(min);

    }

    void txFilter() throws Exception {
        Disposable subscription = web3j.transactionFlowable().subscribe(tx -> {
            //TODO Gas pro Zeiteinheit anzeigen
            log.info("Eine Transaktion von folgender Adresse wurde gefunden: " + tx.getFrom());
            log.info("Gas price used: " + tx.getGasPrice());
            log.info("Gas : " + tx.getGas());
            log.info("Gas : " + tx.getGasRaw());
            log.info("Transferierter Ether: " + Convert.fromWei(tx.getValue().toString(), Convert.Unit.ETHER));

            //TODO increaseCounter wieder einkommentieren und hier die DOS Algo implemeniteren
            if (tx.getGasPrice().equals(BigInteger.ZERO)) {
                // AccountLoader.getInstance().increaseCounter(tx.getFrom().trim().toLowerCase());
                log.info("Transaktionskosten waren 0");
                log.info("Anzahl Accounts:" + accountLoader.getAccountArrayList().size());

                for (Account account : accountLoader.getAccountArrayList()) {
                    if (account.getAdressValue().equals(tx.getFrom())) {
                        account.decraseTransaktionCounter();
                        account.decraseGasUsedCounter(Integer.parseInt(tx.getGas().toString()));

                        //TODO DCREASE GASUSED COUNTER
                        //account.decraseGasUsedCounter(tx.getGas());
                        log.info("Account: " + account.getAddress() + " hat noch " + account.getTransaktionCounter()
                                + " Transaktionen auf dem Counter und noch so viel Gas zum verbauchen " + account
                                .getGasUsedCounter());
                        this.dosAlgorithmus(account);
                        break;
                    }
                }

            }
        }, Throwable::printStackTrace);
    }

    //TODO Admin Account darf unendlich viele Transaktionen machen (ID von Account == dann nicht revoked)
    //TODO Algo anhand Gasprice und nicht Anzahl Transaktionen
    //TODO Counter runterzählen

    /**
     * In dieser Methode wird geschaut, wieviel Transaktionen von dem Account gemacht wurden.
     * Wenn dieser Account mehr Transaktionen als die Limite gemacht hat, wird er gesperrt.
     *
     * @param account Account der inspiziert/kontrolliert wird
     */
    private void dosAlgorithmus(Account account) {
        if (account.getTransaktionCounter() == 0) {
            this.chainInteractions.revokeAccount(account.getAdressValue());

            account.increaseRevoked();
            this.accountLoader.getRevokedAccountArrayList().add(account);
            this.accountLoader.getAccountArrayList().remove(account);
            //TODO fixe Revoke Zeit
            account.setRevokePeriodCounter(account.getRevokePeriod() - 1);
            log.info(
                    "Der Acccount hat zu viele Transaktionen verbruacht und " + account.getAdressValue() + " wurde zum "
                            + account.getRevoked() + " Mal gesperrt. Die Revoke Periode ist: " + account
                            .getRevokePeriodCounter());
            try {
                this.accountWriter.writeAccountsInFile();
            } catch (IOException e) {
                log.warn("Probleme beim Schreiben in die BlockedCounter Datei");
            }
        } else if (account.getGasUsedCounter() < 0) {
            this.chainInteractions.revokeAccount(account.getAdressValue());
            account.increaseRevoked();
            this.accountLoader.getRevokedAccountArrayList().add(account);
            this.accountLoader.getAccountArrayList().remove(account);
            //TODO fixe Zeit eingeben
            account.setRevokePeriodCounter(account.getRevokePeriod());
            log.info("Der Acccount hat zu viel Gas verbraucht " + account.getAdressValue() + " wurde zum " + account
                    .getRevoked() + " Mal gesperrt. Die Revoke Periode ist: " + account.getRevokePeriodCounter());
        }
    }

    //TODO Intervall Methode wo alle Counter von Accounts raufzählt
    //TODO javadoc
    /*

     */
    private void setAllCountersToMax() {

        for (Account account : accountLoader.getAccountArrayList()) {
            account.setTransaktionCounter(account.getMaxTransaktionCounter().intValue());
            account.setGasUsedCounter(account.getMaxGasUsed().intValue());

        }

    }

    /*
    Alle Accounts die genug lange gesperrt waren werden wieder entsperrt
     */
    private void certifyRevokedAccounts() {
        List<Account> removeFromRevokeList = new ArrayList<>();

        for (Account account : this.accountLoader.getRevokedAccountArrayList()) {
            if (this.controlRevokePeriode(account)) {
                this.chainInteractions.certifyAccount(account.getAdressValue());
                log.info("Account wurde wieder certifiziert: " + account.getAdressValue());
                account.setRevokePeriodCounter(account.getRevokePeriod());
                account.setTransaktionCounter(Integer.parseInt(account.getMaxTransaktionCounter().toString()));
                account.setGasUsedCounter(Integer.parseInt(account.getMaxGasUsed().toString()));
                this.accountLoader.getAccountArrayList().add(account);
                removeFromRevokeList.add(account);
            } else {
                account.setRevokePeriodCounter(account.getRevokePeriodCounter() - 1);

            }
        }

        //TODO in eigene Methode auslagern
        for (Account removeAccount : removeFromRevokeList) {
            this.accountLoader.getRevokedAccountArrayList().remove(removeAccount);
        }

    }

    //TODO evtl unnötige methode da einzeiler
    private boolean controlRevokePeriode(Account account) {
        return account.getRevokePeriodCounter() < 0;

    }

    //TODO JAVADOV
    private void intervall(int min) throws InterruptedException, IOException {
        System.out.println("*************Intervall ist gestartet********************");
        while (true) {
            System.out.println(new Date());
            Thread.sleep(min * 60 * 1000);
            this.setAllCountersToMax();
            this.certifyRevokedAccounts();
            this.accountWriter.writeAccountsInFile();
        }
    }

}

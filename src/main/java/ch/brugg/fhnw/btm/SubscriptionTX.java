package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.dosAlgorithm.DoSAlgorithm;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.Account;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

//TODO Beschreischbung was diese Klasse macht

/**
 * In dieser Klasse wird die Subscription gemacht um die gemachten Transaktionen beobachten zu können
 * In dieser Klasse werden die Transaktionen gefiltert, um bei gratis Transaktionen den DoS Algorithmus zu starten
 * In dieser Klasse wird das ResetIntervall gestartet, welches alle Counters Resettet und das Speichern der daten reguliert
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class SubscriptionTX {

    private Web3j web3j;
    private static Logger log = LoggerFactory.getLogger(SubscriptionTX.class);
    private ChainInteractions chainInteractions;
    private JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
    private DoSAlgorithm dosAlgorithm = DoSAlgorithm.getInstance();

    public SubscriptionTX(Web3j web3j, ChainInteractions chainInteractions) {
        this.web3j = web3j;
        this.chainInteractions = chainInteractions;
        dosAlgorithm.setChainInteractions(chainInteractions);
    }


    /**
     * Mit dieser Methode wird der filter und der Intervall gestaret
     * @param min Anzahl Minuten wo das Intervall neu gestartet wird
     * @throws Exception
     */
    public void run(int min) throws Exception {
        log.info("Filter und Intervall werden gestartet");
        this.txFilter();
        this.intervall(min);
    }

    /**
     * In dieser Methode wird eine Subscription gemacht und geprüft ob die getätigten Transaktionen
     * gratis Transaktionen sind.
     * Wenn ja, wird der DoS Algorithmus gestartet.
     */
    private void txFilter() {
        Disposable subscription = web3j.transactionFlowable().subscribe(tx -> {
            //TODO Gas pro Zeiteinheit anzeigen
            log.info("Eine Transaktion von folgender Adresse wurde gefunden: " + tx.getFrom());
            log.info("Gas price used: " + tx.getGasPrice());
            log.info("Gas : " + tx.getGas());
            log.info("Gas : " + tx.getGasRaw());
            log.info("Transferierter Ether: " + Convert.fromWei(tx.getValue().toString(), Convert.Unit.ETHER));

            //TODO Admin Account darf unendlich viele Transaktionen machen (ID von Account == dann nicht revoked)
            if (tx.getGasPrice().equals(BigInteger.ZERO)) {
                log.info("Transaktionskosten waren 0");

                this.dosAlgorithm.dosAlgorithm(accountHandler.processAccount(tx.getFrom(), tx.getGas()));

            }
        });
    }

    /**
     * In dieser Methode werden alle Counters zurückgesetzt
     */
    private void setAllCountersToMax() {

        for (Account account : accountHandler.getAccountList()) {
            account.setTransactionCounter(account.getTxLimit().intValue());
            account.setGasUsedCounter(account.getGasLimit().intValue());
        }

    }

    /**
     * In dieser Methode wird der Intervall ausgeführt Nach jedem ausführen werden alleCounters zurückgesetzt
     * und der Zustand der Accounts wie auch der DefaultSettings in die dateien gespeichert.
     * @param min Anzahl Minuten des Intervalls
     * @throws InterruptedException
     * @throws IOException
     */
    private void intervall(int min) throws InterruptedException, IOException {
        log.info("*************Intervall ist gestartet********************");
        while (true) {
            log.info(new Date().toString());

            //TODO eigener THREAD
            Thread.sleep(min * 60 * 1000);
            this.setAllCountersToMax();
            this.jsonDefaultSettingsHandler.getDefaultSettings()
                    .setTimestampLastReset(new Timestamp(System.currentTimeMillis()));
            this.accountHandler.writeAccountList();
            this.jsonDefaultSettingsHandler.writeDefaultSettings();
        }
    }

}

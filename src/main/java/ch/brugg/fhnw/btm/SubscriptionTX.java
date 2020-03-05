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
public class SubscriptionTX {

    private Web3j web3j;
    private static Logger log = LoggerFactory.getLogger(SubscriptionTX.class);
    private ChainInteractions chainInteractions ;
    private JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
    private DoSAlgorithm dosAlgorithm = DoSAlgorithm.getInstance();

    public SubscriptionTX(Web3j web3j, ChainInteractions chainInteractions) {
        this.web3j = web3j;
        this.chainInteractions = chainInteractions;
        dosAlgorithm.setChainInteractions(chainInteractions);
    }

    //TODO JAVADOC
    public void run(int min) throws Exception {
        log.info("Doing simple subscription");
        this.txFilter();
        this.intervall(min);


    }

    void txFilter() {
        Disposable subscription = web3j.transactionFlowable().subscribe(tx -> {
            //TODO Gas pro Zeiteinheit anzeigen
            log.info("Eine Transaktion von folgender Adresse wurde gefunden: " + tx.getFrom());
            log.info("Gas price used: " + tx.getGasPrice());
            log.info("Gas : " + tx.getGas());
            log.info("Gas : " + tx.getGasRaw());
            log.info("Transferierter Ether: " + Convert.fromWei(tx.getValue().toString(), Convert.Unit.ETHER));


            if (tx.getGasPrice().equals(BigInteger.ZERO)) {
                log.info("Transaktionskosten waren 0");

                this.dosAlgorithm.dosAlgorithm(accountHandler.processAccount(tx.getFrom(), tx.getGas()));

            }
        });
    }

    //TODO Admin Account darf unendlich viele Transaktionen machen (ID von Account == dann nicht revoked)
    //TODO Algo anhand Gasprice und nicht Anzahl Transaktionen
    //TODO Counter runterzählen



    //TODO Intervall Methode wo alle Counter von Accounts raufzählt
    //TODO javadoc
    /*

     */
    private void setAllCountersToMax() {

        for (Account account : accountHandler.getAccountList()) {
            account.setTransactionCounter(account.getTxLimit().intValue());
            account.setGasUsedCounter(account.getGasLimit().intValue());
        }

    }
//
//    /*
//    Alle Accounts die genug lange gesperrt waren werden wieder entsperrt
//     */
//    private void certifyRevokedAccounts() {
//        List<Account> removeFromRevokeList = new ArrayList<>();
//
//        for (Account account : this.accountHanler.getRevokedAccountArrayList()) {
//            if (this.controlRevokePeriode(account)) {
//                this.chainInteractions.certifyAccount(account.getAddress());
//                log.info("Account wurde wieder certifiziert: " + account.getAddress());
//                account.setRevokeTime( .getDefaultSettings().getRevokeMultiplier());
//                account.setTransactionCounter(Integer.parseInt(account.getTxLimit().toString()));
//                account.setGasUsedCounter(Integer.parseInt(account.getGasLimit().toString()));
//                this.accountHanler.getAccountArrayList().add(account);
//                removeFromRevokeList.add(account);
//            } else {
//                account.setRevokeTime(account.getRevokeTime() - 1);
//
//            }
//        }
//
//        //TODO in eigene Methode auslagern
//        for (Account removeAccount : removeFromRevokeList) {
//            this.accountHanler.getRevokedAccountArrayList().remove(removeAccount);
//        }
//
//    }

    //TODO JAVADOV
    private void intervall(int min) throws InterruptedException, IOException {
        System.out.println("*************Intervall ist gestartet********************");
        while (true) {
            System.out.println(new Date());
            Thread.sleep(min * 60 * 1000);
            this.setAllCountersToMax();
             jsonDefaultSettingsHandler.getDefaultSettings()
                    .setTimestampLastReset(new Timestamp(System.currentTimeMillis()));
           accountHandler.writeAccountList();
            this.jsonDefaultSettingsHandler.writeDefaultSettings();
        }
    }

}

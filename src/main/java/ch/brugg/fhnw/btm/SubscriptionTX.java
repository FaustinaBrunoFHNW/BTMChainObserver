package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.pojo.Account;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.Date;
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

    public SubscriptionTX(Web3j web3j, AccountLoader accountLoader, ChainInteractions chainInteractions) {
        this.web3j = web3j;
        this.accountLoader = accountLoader;
        this.chainInteractions = chainInteractions;

    }

    public void run(int min) throws Exception {
        log.info("Doing simple subscription");
        //        simpleFilterExample();
        simpleTxFilter();
        this.intervall(min);

    }

    void simpleFilterExample() throws Exception {
        Disposable subscription = web3j.blockFlowable(false).subscribe(block -> {
            log.info("Sweet, block number " + block.getBlock().getNumber() + " has just been created");
        }, Throwable::printStackTrace);

        TimeUnit.MINUTES.sleep(2);
        subscription.dispose();
    }

    void simpleTxFilter() throws Exception {
        Disposable subscription = web3j.transactionFlowable().subscribe(tx -> {
            //TODO Gas pro Zeiteinheit anzeigen
            log.info("Found a TX from " + tx.getFrom());
            log.info("Gas price used: " + tx.getGasPrice());
            log.info("Ether moved " + Convert.fromWei(tx.getValue().toString(), Convert.Unit.ETHER));

            //TODO increaseCounter wieder einkommentieren und hier die DOS Algo implemeniteren
            if (tx.getGasPrice().equals(BigInteger.ZERO)) {
                // AccountLoader.getInstance().increaseCounter(tx.getFrom().trim().toLowerCase());
                log.info("Transaktionskosten waren 0");
                log.info("Anzahl Accounts:" + accountLoader.getAccountArrayList().size());

                for (Account account : accountLoader.getAccountArrayList()) {
                    if (account.getAdressValue().equals(tx.getFrom())) {
                        account.decraseTransaktionCounter();

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
            int revokedCounter = account.increaseRevoked();
            this.accountLoader.getRevokedAccountArrayList().add(account);
            log.info("Der Acccount " + account.getAdressValue() + " wurde zum " + revokedCounter + " Mal gesperrt. ");
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
    private void certifyRevokedAccounts(){
        for (Account account : this.accountLoader.getRevokedAccountArrayList()) {
           if( this.controlRevokePeriode(account)){
               this.chainInteractions.certifyAccount(account.getAdressValue());
               this.accountLoader.getAccountArrayList().add(account);
               accountLoader.getRevokedAccountArrayList().remove(account);
           }
           else{
               account.setRevokePeriodCounter(account.getRevokePeriodCounter()-1);
           }
        }
    }


    //TODO evtl unnötige methode da einzeiler
    private boolean controlRevokePeriode(Account account) {
        if (account.getRevokePeriodCounter() == 0) {
            return true;
        }
        return false;

    }

    //TODO JAVADOV
    private void intervall(int min) throws InterruptedException {
        System.out.println("*************Intervall ist gestartet********************");
        while (true) {
            System.out.println(new Date());
            Thread.sleep(min*60 * 1000);
            this.setAllCountersToMax();
            this.certifyRevokedAccounts();
        }
    }

}

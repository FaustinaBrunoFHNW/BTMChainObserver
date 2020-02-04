package ch.brugg.fhnw.btm;


import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

//TODO Beschreischbung was diese Klasse macht
public class SubscriptionTX {

    private Web3j web3j;
    private Subscription subscription;
    private static Logger log = LoggerFactory.getLogger(SubscriptionTX.class);

    public SubscriptionTX(Web3j web3j) {
        this.web3j = web3j;

    }

    public void run() throws Exception {
        log.info("Doing simple subscription");
        //        simpleFilterExample();
        simpleTxFilter();

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
            log.info("Found a TX from " + tx.getFrom());
            log.info("Gas price used: " + tx.getGasPrice());
            log.info("Ether moved " + Convert.fromWei(tx.getValue().toString(), Convert.Unit.ETHER));


            //TODO increaseCounter wieder einkommentieren und hier die DOS Algo implemeniteren
            if (tx.getGasPrice().equals(BigInteger.ZERO)) {
             //   AccountLoader.getInstance().increaseCounter(tx.getFrom().trim().toLowerCase());
            }
        }, Throwable::printStackTrace);
    }
}

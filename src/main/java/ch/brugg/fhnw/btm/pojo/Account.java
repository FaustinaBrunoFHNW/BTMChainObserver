package ch.brugg.fhnw.btm.pojo;

import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Account {

    private String address;
    private int transactionCounter;
    private BigInteger transactionLimit;
    private BigInteger revokeTime;
    private BigInteger gasLimit;
    private long gasUsedCounter;
    public boolean deleteMe = false;
    private Timestamp timeStamp = null;

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    private static Logger log = LoggerFactory.getLogger(Account.class);

    //TODO brauch es das?
    /**
     * Erhöht den Counter bis er auf den definierten maximal Wert komnmt
     */
    public void increaseTransactionCounter() {
        if (this.transactionCounter < this.transactionLimit.intValue()) {
            this.transactionCounter++;
        }
        log.info("Account " + address + " hat noch " + transactionCounter + " Transktionen");

    }

    //TODO brauch es das?
    public void increaseGasUsedCounter() {
        if (this.gasUsedCounter < this.transactionLimit.intValue()) {
            this.gasUsedCounter++;
        }
        log.info("Account " + address + " hat noch " + gasUsedCounter + " Gas zu verbrauchen");

    }


    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decraseTransactionCounter() {
        if (transactionCounter > 0) {
            this.transactionCounter--;
        }
        log.info("Account " + address + " hat noch " + transactionCounter + " Transktionen");
    }

    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decreaseGasUsedCounter(long gasUsedOnTX) {
        if (gasUsedCounter > 0) {
            this.gasUsedCounter= this.getGasUsedCounter()-gasUsedOnTX;
        }
        log.info("Account " + address + " hat noch " + gasUsedCounter + " Gas zu verbrauchen");
    }


    //**************************GETTER und SETTER*********************************

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getRevokeTime() {
        if (revokeTime == null){
            return JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultRevokeTime();
        }
        return revokeTime;
    }

    public void setRevokeTime(BigInteger revokeTime) {
        this.revokeTime = revokeTime;
    }

    public BigInteger getGasLimit() {
        if (gasLimit == null){
            return JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultGasLimit();
        }
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public int getTransactionCounter() {
        return transactionCounter;
    }

    public void setTransactionCounter(int transactionCounter) {
        this.transactionCounter = transactionCounter;
    }

    public long getGasUsedCounter() {
        return gasUsedCounter;
    }

    public void setGasUsedCounter(long gasUsedCounter) {
        this.gasUsedCounter = gasUsedCounter;
    }

    public BigInteger getTransactionLimit() {

        if (transactionLimit == null){
            return JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultTxLimit();
        }

        return transactionLimit;
    }

    public void setTransactionLimit(BigInteger transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

}

package ch.brugg.fhnw.btm.pojo;

import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Account {

    private String address;
    private int transactionCounter;
    private BigInteger txLimit;
    private long revokeTime;
    private BigInteger gasLimit;
    private long gasUsedCounter;
    public boolean deleteMe = false;
    private Timestamp timeStamp = null;


    private static Logger log = LoggerFactory.getLogger(Account.class);

    //TODO JAVADOC
    public Account(String address) {
        this.address = address;
    }

    //TODO JAVADOC Constructor für initial Lösung
    public Account(String address, String txLimit, String gasLimit, int revokedPeriodCounter) {
        this.address = address;
        this.txLimit = new BigInteger(txLimit);
        this.gasLimit = new BigInteger(gasLimit);
        this.revokeTime = revokedPeriodCounter;
        this.transactionCounter = this.txLimit.intValue();
        this.gasUsedCounter = this.gasLimit.intValue();
    }

    //TODO JAVADOC Constructor für  Lesung mit Revoked und revoked Period
    public Account(String address, String txLimit, String gasLimit, String revokedPeriodCounter) {
        this.address = address;
        this.txLimit = new BigInteger(txLimit);
        this.gasLimit = new BigInteger(gasLimit);
        this.revokeTime = Integer.parseInt(revokedPeriodCounter);
        this.transactionCounter = this.txLimit.intValue();
        this.gasUsedCounter = this.gasLimit.intValue();
    }

    //TODO

    /**
     * Erhöht den Counter bis er auf den definierten maximal Wert komnmt
     */
    public void increaseTransactionCounter() {
        if (this.transactionCounter < this.txLimit.intValue()) {
            this.transactionCounter++;
        }
        log.info("Account " + address + " hat noch " + transactionCounter + " Transktionen");

    }

    public void increaseGasUsedCounter() {
        if (this.gasUsedCounter < this.txLimit.intValue()) {
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
    public void decreaseGasUsedCounter(int gasUsedOnTX) {
        if (gasUsedCounter > 0) {
            this.gasUsedCounter= this.getGasUsedCounter()-gasUsedOnTX;
        }
        log.info("Account " + address + " hat noch " + gasUsedCounter + " Gas zu verbrauchen");
    }




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getRevokeTime() {
        return revokeTime;
    }

    public void setRevokeTime(long revokeTime) {
        this.revokeTime = revokeTime;
    }

    public BigInteger getGasLimit() {
        if (gasLimit.equals(BigInteger.ZERO)){
            return BigInteger.valueOf(JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultGasLimit());
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

    public BigInteger getTxLimit() {

        if (txLimit.equals(BigInteger.ZERO)){
            return BigInteger.valueOf(JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultTxLimit());
        }

        return txLimit;
    }

    public void setTxLimit(BigInteger txLimit) {
        this.txLimit = txLimit;
    }

}

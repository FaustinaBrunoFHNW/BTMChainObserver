package ch.brugg.fhnw.btm.pojo;

import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Timestamp;

public class JsonAccount {

    private String address;
    private int remainingTransactions;
    private BigInteger transactionLimit;
    private BigInteger revokeTime;
    private BigInteger gasLimit;
    private long remainingGas;
    public boolean deleteMe = false;
    private Timestamp timeStamp = null;

    public Timestamp getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    private static Logger log = LoggerFactory.getLogger(JsonAccount.class);

    //TODO brauch es das?
    /**
     * Erhöht den Counter bis er auf den definierten maximal Wert komnmt
     */
    public void increaseTransactionCounter() {
        if (this.remainingTransactions < this.transactionLimit.intValue()) {
            this.remainingTransactions++;
        }
        log.info("Account " + this.address + " hat noch " + this.remainingTransactions + " Transktionen");

    }

    //TODO brauch es das?
    public void increaseGasUsedCounter() {
        if (this.remainingGas < this.transactionLimit.intValue()) {
            this.remainingGas++;
        }
        log.info("Account " + this.address + " hat noch " + this.remainingGas + " Gas zu verbrauchen");

    }


    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decraseTransactionCounter() {
        if (this.remainingTransactions > 0) {
            this.remainingTransactions--;
        }
        log.info("Account " + this.address + " hat noch " + this.remainingTransactions + " Transktionen");
    }

    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decreaseGasUsedCounter(long gasUsedOnTX) {
        if (this.remainingGas > 0) {
            this.remainingGas = this.getRemainingGas()-gasUsedOnTX;
        }
        log.info("Account " + this.address + " hat noch " + this.remainingGas + " Gas zu verbrauchen");
    }


    //**************************GETTER und SETTER*********************************

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getRevokeTime() {
        if (this.revokeTime == null){
            return JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultRevokeTime();
        }
        return this.revokeTime;
    }

    public void setRevokeTime(BigInteger revokeTime) {
        this.revokeTime = revokeTime;
    }

    public BigInteger getGasLimit() {
        if (this.gasLimit == null){
            return JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultGasLimit();
        }
        return this.gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public int getRemainingTransactions() {
        return this.remainingTransactions;
    }

    public void setRemainingTransactions(int remainingTransactions) {
        this.remainingTransactions = remainingTransactions;
    }

    public long getRemainingGas() {
        return this.remainingGas;
    }

    public void setRemainingGas(long remainingGas) {
        this.remainingGas = remainingGas;
    }

    public BigInteger getTransactionLimit() {

        if (this.transactionLimit == null){
            return JsonDefaultSettingsHandler.getInstance().getDefaultSettings().getDefaultTxLimit();
        }

        return this.transactionLimit;
    }

    public void setTransactionLimit(BigInteger transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public boolean isDeleteMe() {
        return deleteMe;
    }

    public void setDeleteMe(boolean deleteMe) {
        this.deleteMe = deleteMe;
    }
}

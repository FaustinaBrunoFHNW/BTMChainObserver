package ch.brugg.fhnw.btm.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.primitive.Int;

import java.math.BigInteger;

public class Account {

    private Address address;
    private String adressValue;
    private int transaktionCounter;
    private BigInteger maxTransaktionCounter;
    private int revokePeriodCounter;
    private int revokePeriod;
    private int revoked;
    private BigInteger maxGasUsed;
    private int gasUsedCounter;
    private boolean defaultSettings;

    private static Logger log = LoggerFactory.getLogger(Account.class);

    //TODO JAVADOC
    public Account(String address) {
        this.address = new Address(address.trim().toLowerCase());
        this.adressValue = address;
    }

    //TODO JAVADOC Constructor für initial Lesung
    public Account(String address, String maxTransaktionCounter, String maxGasUsed, int revokePeriod) {
        this.address = new Address(address.trim().toLowerCase());
        this.adressValue = address;
        this.maxTransaktionCounter = new BigInteger(maxTransaktionCounter);
        this.maxGasUsed = new BigInteger(maxGasUsed);
        this.revokePeriodCounter = revokePeriod;
        this.revokePeriod=revokePeriod;

        this.transaktionCounter = this.maxTransaktionCounter.intValue();
        this.gasUsedCounter = this.maxGasUsed.intValue();
        this.revoked=0;
    }

    //TODO JAVADOC Constructor für  Lesung mit Revoked und revoked Period
    public Account(String address, String maxTransaktionCounter, String maxGasUsed, int revokePeriod,String revokedPeriodCounter,String revoked) {
        this.address = new Address(address.trim().toLowerCase());
        this.adressValue = address;
        this.maxTransaktionCounter = new BigInteger(maxTransaktionCounter);
        this.maxGasUsed = new BigInteger(maxGasUsed);
        this.revokePeriodCounter = Integer.parseInt(revokedPeriodCounter);
        this.revokePeriod=revokePeriod;

        this.transaktionCounter = this.maxTransaktionCounter.intValue();
        this.gasUsedCounter = this.maxGasUsed.intValue();
        this.revoked=Integer.parseInt(revoked);
    }

    //TODO

    /**
     * Erhöht den Counter bis er auf den definiertern maximal Wert komnmt
     */
    public void increaseTransaktionCounter() {
        if (this.transaktionCounter < this.maxTransaktionCounter.intValue()) {
            this.transaktionCounter++;
        }
        log.info("Account " + address + " hat noch " + transaktionCounter + " Transktionen");

    }

    public void increaseGasUsedCounter() {
        if (this.gasUsedCounter < this.maxTransaktionCounter.intValue()) {
            this.gasUsedCounter++;
        }
        log.info("Account " + address + " hat noch " + gasUsedCounter + " Gas zu verbrauchen");

    }

    public int increaseRevoked(){
      return   this.revoked++;
    }

    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decraseTransaktionCounter() {
        if (transaktionCounter > 0) {
            this.transaktionCounter--;
        }
        log.info("Account " + address + " hat noch " + transaktionCounter + " Transktionen");
    }

    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decraseGasUsedCounter(int gasUsedOnTX) {
        if (gasUsedCounter > 0) {
            this.gasUsedCounter= this.getGasUsedCounter()-gasUsedOnTX;
        }
        log.info("Account " + address + " hat noch " + gasUsedCounter + " Gas zu verbrauchen");
    }

    // Getter und Setter
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAdressValue() {
        return adressValue;
    }

    public void setAdressValue(String adressValue) {
        this.adressValue = adressValue;
    }

    public int getRevokePeriodCounter() {
        return revokePeriodCounter;
    }

    public void setRevokePeriodCounter(int revokePeriodCounter) {
        this.revokePeriodCounter = revokePeriodCounter;
    }

    public BigInteger getMaxGasUsed() {
        return maxGasUsed;
    }

    public void setMaxGasUsed(BigInteger maxGasUsed) {
        this.maxGasUsed = maxGasUsed;
    }

    public int getTransaktionCounter() {
        return transaktionCounter;
    }

    public void setTransaktionCounter(int transaktionCounter) {
        this.transaktionCounter = transaktionCounter;
    }

    public int getGasUsedCounter() {
        return gasUsedCounter;
    }

    public void setGasUsedCounter(int gasUsedCounter) {
        this.gasUsedCounter = gasUsedCounter;
    }

    public BigInteger getMaxTransaktionCounter() {
        return maxTransaktionCounter;
    }

    public void setMaxTransaktionCounter(BigInteger maxTransaktionCounter) {
        this.maxTransaktionCounter = maxTransaktionCounter;
    }

    public int getRevokePeriod() {
        return revokePeriod;
    }

    public void setRevokePeriod(int revokePeriod) {
        this.revokePeriod = revokePeriod;
    }

    public int getRevoked() {
        return revoked;
    }

    public void setRevoked(int revoked) {
        this.revoked = revoked;
    }

    public boolean isDefaultSettings() {
        return defaultSettings;
    }

    public void setDefaultSettings(boolean defaultSettings) {
        this.defaultSettings = defaultSettings;
    }
}

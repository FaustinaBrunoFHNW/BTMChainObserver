package ch.brugg.fhnw.btm.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Address;

import java.math.BigInteger;

public class Account {

    private Address address;
    private String adressValue;
    private int transaktionCounter;
    private BigInteger maxTransaktionCounter;
    private int revokePeriod;
    private BigInteger maxGasUsed;
    private int gasUsedCounter;

    private static Logger log = LoggerFactory.getLogger(Account.class);

    public Account(Address address) {
        this.address = address;
        this.revokePeriod = revokePeriod;
    }

    public Account(String address, String maxTransaktionCounter, String maxGasUsed) {
        this.address = new Address(address.trim().toLowerCase());
        this.adressValue = address;
        this.maxTransaktionCounter=new BigInteger(maxTransaktionCounter);
        this.maxGasUsed= new BigInteger(maxGasUsed);


        this.transaktionCounter = this.maxTransaktionCounter.intValue();
        this.gasUsedCounter= this.maxGasUsed.intValue();
    }

    //TODO
    /**
     * Erhöht den Counter bis er auf den definiertern maximal Wert komnmt
     */
    public void increaseTransaktionCounter() {
        if (this.transaktionCounter<this.maxTransaktionCounter.intValue()) {
            this.transaktionCounter++;
        }
        log.info("Account " + address + " hat noch " + transaktionCounter+" Transktionen");

    }

    public void increaseGasUsedCounter() {
        if (this.gasUsedCounter<this.maxTransaktionCounter.intValue()) {
            this.gasUsedCounter++;
        }
        log.info("Account " + address + " hat noch " + gasUsedCounter+" Gas zu verbrauchen");

    }

        /**
         * Zählt den Counter runter bis er bei 0 angekommen ist
         */
    public void decraseTransaktionCounter() {
       if (transaktionCounter>0) {
            this.transaktionCounter--;
        }
        log.info("Account " + address + " hat noch " + transaktionCounter+" Transktionen");
    }

    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decraseGasUsedCounter() {
        if (gasUsedCounter>0) {
            this.gasUsedCounter--;
        }
        log.info("Account " + address + " hat noch " + gasUsedCounter+" Gas zu verbrauchen");
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



    public int getRevokePeriod() {
        return revokePeriod;
    }

    public void setRevokePeriod(int revokePeriod) {
        this.revokePeriod = revokePeriod;
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


}

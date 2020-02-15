package ch.brugg.fhnw.btm.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Address;

import java.math.BigInteger;

public class Account {

    private Address address;
    private String adressValue;
    private BigInteger transaktionCounter;
    private BigInteger maxTransaktionCounter;
    private int revokePeriod;
    private BigInteger maxGasUsed;
    private BigInteger gasUsedCounter;

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

        //TODO bei abwärtszählen hier den max Wert rein
        this.transaktionCounter = new BigInteger("0");
        this.gasUsedCounter= this.maxGasUsed;
    }

    //TODO
    /**
     * Erhöht den Counter bis er auf den definiertern maximal Wert komnmt
     */
    public void increaseCounter() {
        if (this.transaktionCounter.compareTo(this.maxTransaktionCounter) <0) {
            this.transaktionCounter.add(BigInteger.ONE);
        }
        log.info("Counter von Account " + address + " hat der Wert: " + transaktionCounter);

    }

    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decraseCounter() {
       // if (transaktionCounter != 0) {
          //  this.transaktionCounter--;
     //   }
       log.info("Counter von Account " + address + " hat der Wert: " + transaktionCounter);
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

    public BigInteger getTransaktionCounter() {
        return transaktionCounter;
    }

    public void setTransaktionCounter(BigInteger transaktionCounter) {
        this.transaktionCounter = transaktionCounter;
    }

    public BigInteger getMaxTransaktionCounter() {
        return maxTransaktionCounter;
    }

    public void setMaxTransaktionCounter(BigInteger maxTransaktionCounter) {
        this.maxTransaktionCounter = maxTransaktionCounter;
    }

    public BigInteger getGasUsedCounter() {
        return gasUsedCounter;
    }

    public void setGasUsedCounter(BigInteger gasUsedCounter) {
        this.gasUsedCounter = gasUsedCounter;
    }
}

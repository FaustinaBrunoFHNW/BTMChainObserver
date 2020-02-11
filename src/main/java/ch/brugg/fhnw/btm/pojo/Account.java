package ch.brugg.fhnw.btm.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Address;

public class Account {

    private Address address;
    private String adressValue;
    private int counter;
    private int maxCounter;
    private int revokePeriod;

    private static Logger log = LoggerFactory.getLogger(Account.class);

    public Account(Address address, int maxCounter, int revokePeriod) {
        this.address = address;
        this.maxCounter = maxCounter;
        this.counter = maxCounter;
        this.revokePeriod = revokePeriod;
    }

    public Account(String address) {
        this.address = new Address(address.trim().toLowerCase());
        this.adressValue = address;
        this.counter = 0;
    }

    /**
     * Erhöht den Counter bis er auf den definiertern maximal Wert komnmt
     */
    public void increaseCounter() {
        if (this.counter < this.maxCounter) {
            this.counter++;
        }
        log.info("Counter von Account " + address + " hat der Wert: " + counter);

    }

    /**
     * Zählt den Counter runter bis er bei 0 angekommen ist
     */
    public void decraseCounter() {
        if (counter != 0) {
            this.counter--;
        }
        log.info("Counter von Account " + address + " hat der Wert: " + counter);
    }

    // Getter und Setter
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public String getAdressValue() {
        return adressValue;
    }

    public void setAdressValue(String adressValue) {
        this.adressValue = adressValue;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getMaxCounter() {
        return maxCounter;
    }

    public void setMaxCounter(int maxCounter) {
        this.maxCounter = maxCounter;
    }

    public int getRevokePeriod() {
        return revokePeriod;
    }

    public void setRevokePeriod(int revokePeriod) {
        this.revokePeriod = revokePeriod;
    }
}

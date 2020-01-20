package ch.brugg.fhnw.btm.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Address;


public class Account {

    private Address address;
    private Integer counter;
    private static Logger log = LoggerFactory.getLogger(Account.class);

    public Account(Address address) {
        this.address = address;
        this.counter = 0;
    }

    public Account(String address){
        this.address = new Address(address.trim().toLowerCase());
        this.counter = 0;
    }

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

    public void increaseCounter() {
        this.counter++;
        log.info("Counter for Account "+ address +" reached count: " +counter);

    }
}

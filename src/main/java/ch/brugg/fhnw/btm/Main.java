package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.pojo.Account;
import org.web3j.protocol.Web3j;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        String P_ADD = "0x00a329c0648769a73afac7f9381e08fb43dbea72";
        AccountLoader accountLoader = new AccountLoader();
        ChainSetUp chainSetUp = new ChainSetUp(P_ADD);
        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);
        Web3j web3j = chainSetUp.getWeb3j();

        chainSetUp.setUpAfterChainStart();


        //TODO alle Benutzer aus Liste auslesen und certifien
       // accountLoader.loadAccounts();
       // HashMap<BigInteger, Account> accounts = accountLoader.getAccounts();

        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j);
        subscriptionTX.run();
    }
}

package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.pojo.Account;
import org.web3j.protocol.Web3j;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {

        //TODO aus File auslesen
        String PRIVATE_KEY = "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7";
        //TODO aus erstem hochfahren rauslesen
        String CERTIFIER_ADD = "0x84b51678f9a4869e384f737ed2a5d56c8ca16c81";
        AccountLoader accountLoader = new AccountLoader();

        System.out.println("Anzahl Accounts: " + accountLoader.getAccountArrayList().size());

        ChainSetUp chainSetUp = new ChainSetUp(PRIVATE_KEY, CERTIFIER_ADD);
        chainSetUp.setUpAfterChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

        Web3j web3j = chainSetUp.getWeb3j();

        //TODO load all accounts from list
        accountLoader.loadAccounts();
        chainInteractions.certifyAccountList(accountLoader.getAccountArrayList());

        System.out.println("Anzahl Accounts: " + accountLoader.getAccountArrayList().size());


        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j, accountLoader, chainInteractions);
        subscriptionTX.run();
    }

}

package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.pojo.Account;
import org.web3j.protocol.Web3j;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        String PRIVATE_KEY = "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7";
        String P_ADD = "0x00a329c0648769a73afac7f9381e08fb43dbea72";
        String CERTIFIER_ADD = "0x84b51678f9a4869e384f737ed2a5d56c8ca16c81";
        AccountLoader accountLoader = new AccountLoader();

        System.out.println("Anzahl Accounts: "+accountLoader.getAccountArrayList().size());

        ChainSetUp chainSetUp = new ChainSetUp(PRIVATE_KEY, CERTIFIER_ADD);
        chainSetUp.setUpAfterChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

        Web3j web3j = chainSetUp.getWeb3j();

        Account account = new Account(P_ADD);
        accountLoader.addAccountToList(P_ADD);
        System.out.println("Anzahl Accounts: "+accountLoader.getAccountArrayList().size());
        //chainInteractions.revokeAccount("0x00a329c0648769A73afAc7F9381E08FB43dBEA72");
        if (chainInteractions.isCertified(P_ADD)) {
            System.out.println("Account is Certified");
        }

        if (!chainInteractions.isCertified(P_ADD)) {
            System.out.println("Account is Not Certified");
        }

        /**
         chainInteractions.certifyAccount("0x00a329c0648769A73afAc7F9381E08FB43dBEA72");

         if( chainInteractions.isCertified("0x00a329c0648769A73afAc7F9381E08FB43dBEA72")){
         System.out.println("Account is Certified");
         }

         if(!chainInteractions.isCertified("0x00a329c0648769A73afAc7F9381E08FB43dBEA72")){
         System.out.println("Account is Not Certified");
         }
         */

        //TODO alle Benutzer aus Liste auslesen und certifien
        // accountLoader.loadAccounts();
        // HashMap<BigInteger, Account> accounts = accountLoader.getAccounts();

        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j, accountLoader,chainInteractions);
        subscriptionTX.run();
    }

}

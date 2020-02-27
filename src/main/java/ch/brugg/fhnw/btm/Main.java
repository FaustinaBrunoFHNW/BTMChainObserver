package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.loader.AccountLoader;
import ch.brugg.fhnw.btm.loader.BlockedCounterLoader;
import org.web3j.protocol.Web3j;

public class Main {

    public static void main(String[] args) throws Exception {

        //TODO revoke all acounts die certifyed sind

        //TODO aus File auslesen
        String PRIVATE_KEY = "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7";
        //TODO aus erstem hochfahren rauslesen
        String CERTIFIER_ADD = "0xee35211c4d9126d520bbfeaf3cfee5fe7b86f221";
        AccountLoader accountLoader =  AccountLoader.getInstance();

        BlockedCounterLoader blockedCounterLoader = new BlockedCounterLoader();

        System.out.println("Anzahl Accounts: " + accountLoader.getAccountArrayList().size());

        //TODO Singleton parameter übergeben
        ChainSetUp chainSetUp =  ChainSetUp.getInstance(PRIVATE_KEY, CERTIFIER_ADD);
        chainSetUp.setUpAfterChainStart();
        //       chainSetUp.setUpNewChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

        Web3j web3j = chainSetUp.getWeb3j();

        //TODO load all accounts from list
        accountLoader.loadAccounts();
        chainInteractions.certifyAccountList(accountLoader.getAccountArrayList());
        blockedCounterLoader.loadBlockedInfo(accountLoader.getAccountArrayList());
        blockedCounterLoader.loadBlockedInfo(accountLoader.getRevokedAccountArrayList());

        System.out.println("Anzahl Accounts: " + accountLoader.getAccountArrayList().size());

        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j, accountLoader, chainInteractions,blockedCounterLoader);
        subscriptionTX.run(accountLoader.getDefaultSettings().getIntervalResetCounter());
    }

}

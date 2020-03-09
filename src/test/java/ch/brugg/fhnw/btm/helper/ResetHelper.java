package ch.brugg.fhnw.btm.helper;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;

import java.util.ArrayList;

public class ResetHelper {

    public void setAccountsCountersToMax() throws InterruptedException {
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();

        jsonAccountHandler.loadAccounts();
        for (JsonAccount account :jsonAccountHandler.getJsonAccountList() ) {
            Thread.sleep(5000);
            account.setRemainingGas(account.getGasLimit().longValue());
            account.setRemainingTransactions(account.getTransactionLimit().intValue());
            account.setTimeStamp(null);
        }
        jsonAccountHandler.writeAccountList();
    }
}

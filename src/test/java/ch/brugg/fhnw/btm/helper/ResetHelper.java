package ch.brugg.fhnw.btm.helper;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;

import java.util.ArrayList;

public class ResetHelper {

    public void setAccountsCountersToMax(){
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();

        jsonAccountHandler.loadAccounts();
        for (JsonAccount account :jsonAccountHandler.getJsonAccountList() ) {
            account.setRemainingGas(account.getGasLimit().longValue());
            account.setRemainingTransactions(account.getTransactionLimit().intValue());
            account.setTimeStamp(null);
        }
        jsonAccountHandler.writeAccountList();
    }
}

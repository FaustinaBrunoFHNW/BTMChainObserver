package ch.brugg.fhnw.btm.helper;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import ch.brugg.fhnw.btm.pojo.JsonDefaultSettings;

import java.util.ArrayList;

public class ResetHelper {

    public void setAccountsCountersToMax() throws InterruptedException {
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();

        jsonAccountHandler.loadAccounts();
        for (JsonAccount account : jsonAccountHandler.getJsonAccountList()) {
            Thread.sleep(5000);
            if (account.getGasLimit() == null) {
                account.setRemainingGas(
                        jsonDefaultSettingsHandler.getDefaultSettings().getDefaultGasLimit().longValue());
            } else {
                account.setRemainingGas(account.getGasLimit().longValue());
            }
            if (account.getTransactionLimit() == null) {
                account.setRemainingTransactions(
                        jsonDefaultSettingsHandler.getDefaultSettings().getDefaultTxLimit().intValue());
            } else {
                account.setRemainingTransactions(account.getTransactionLimit().intValue());
            }
            account.setTimeStamp(null);
        }
        jsonAccountHandler.writeAccountList();
    }
}

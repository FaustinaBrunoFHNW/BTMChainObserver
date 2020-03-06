package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.dosAlgorithm.DoSAlgorithm;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;

public class ResetAccountsCommand implements Command {
    private Logger log = LoggerFactory.getLogger(ResetAccountsCommand.class);
    private JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

    private Timestamp timestamp;

    public ResetAccountsCommand(){
        super();
        timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp lastReset = jsonDefaultSettingsHandler.getDefaultSettings().getTimestampLastReset();
        if (lastReset == null){
            log.error("Zeitstempel in DefaultSettings ist Null");
            lastReset = new Timestamp(System.currentTimeMillis());
            jsonDefaultSettingsHandler.writeDefaultSettings();
        }
        //Timestamp is dependent on timestamp in DefaultSettings. If not present, a new one is created dependent on now
        this.timestamp.setTime(lastReset.getTime() + 60* 1000 * jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());
    }


    private void setAllCountersToMax() {

        for (JsonAccount jsonAccount : this.jsonAccountHandler.getJsonAccountList()) {
            jsonAccount.setRemainingTransactions(jsonAccount.getTransactionLimit().intValue());
            jsonAccount.setRemainingGas(jsonAccount.getGasLimit().intValue());
        }

    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public void execute() {
        log.info(new Date().toString());
        setAllCountersToMax();
        this.jsonDefaultSettingsHandler.getDefaultSettings()
                .setTimestampLastReset(new Timestamp(System.currentTimeMillis()));
        this.jsonAccountHandler.writeAccountList();
        this.jsonDefaultSettingsHandler.writeDefaultSettings();

        DoSAlgorithm.getInstance().offerCommand(new ResetAccountsCommand());
    }
}

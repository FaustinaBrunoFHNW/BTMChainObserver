package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.dosAlgorithm.DoSAlgorithm;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
//JAVADOC

/**
 * Command Pattern fürs
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class ResetAccountsCommand implements Command {
    private Logger log = LoggerFactory.getLogger(ResetAccountsCommand.class);
    private JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
    private JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

    private Timestamp timestamp;

    /**
     * Constuctor
     * Timestamp vom letzten Reset wird aus den DefaultSettings geholt, falls dieser noch nie gesetzt wurde, wird auf
     * den jetzigen Zeitpunkt gesetzt.
     * Ein neuer Timestamp wird anhand dem Timestamp in den DefaultSettings generiert
     */
    public ResetAccountsCommand() {
        super();
        timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp lastReset = jsonDefaultSettingsHandler.getDefaultSettings().getTimestampLastReset();
        if (lastReset == null) {
            log.error("Zeitstempel in DefaultSettings ist Null");
            lastReset = new Timestamp(System.currentTimeMillis());
            jsonDefaultSettingsHandler.writeDefaultSettings();
        }
        //TODO auf Deutsch oder weg
        //Timestamp is dependent on timestamp in DefaultSettings. If not present, a new one is created dependent on now
        this.timestamp.setTime(
                lastReset.getTime() + 60 * 1000 * jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());
    }

    /**
     * In dieser Methode werden bei allen zertifizierten Accounts die Counters zurückgesetzt
     */
    private void setAllCountersToMax() {

        for (JsonAccount jsonAccount : this.jsonAccountHandler.getJsonAccountList()) {
            jsonAccount.setRemainingTransactions(jsonAccount.getTransactionLimit().intValue());
            jsonAccount.setRemainingGas(jsonAccount.getGasLimit().intValue());
        }

    }

    @Override public Timestamp getTimestamp() {
        return timestamp;
    }

    //TODO JAVADOC

    /**
     * In dieser Methode werden die Methode um alle Counters zurückzusetzten und den Timestamp zu setzen gestartet,
     * wie auch beide  Methoden um die Accounts wie auch die DefaultSettings in die Dateien zu speichern.
     * Am Ende wird ein neuer Command iniziert um bei der im Timestamp gespeicherten Zeit wieder den gleichen
     * Command auszuführen
     */
    @Override public void execute() {
        log.info(new Date().toString());
        this.setAllCountersToMax();
        this.jsonDefaultSettingsHandler.getDefaultSettings()
                .setTimestampLastReset(new Timestamp(System.currentTimeMillis()));
        this.jsonAccountHandler.writeAccountList();
        this.jsonDefaultSettingsHandler.writeDefaultSettings();

        DoSAlgorithm.getInstance().offerCommand(new ResetAccountsCommand());
    }
}

package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import java.sql.Timestamp;


/**
 * Abstrakte Klasse für die Command Klassen
 *
 * @Author Faustina Bruno, Serge Jurij Maikoff
 */
public abstract class AbstractCommand implements Command {
    JsonAccount jsonAccount;
    ChainSetup instance;
    private Timestamp timestamp;

    /**
     * Getter für den Timestamp
     *
     * @return
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }


    /**
     * Constructor für Command Klasse
     * @param jsonAccount
     */
    public AbstractCommand(JsonAccount jsonAccount) {
        this.jsonAccount = jsonAccount;
        this.timestamp = jsonAccount.getTimeStamp();
        this.instance = ChainSetup.getInstance();
    }

}

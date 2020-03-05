package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.pojo.JsonAccount;

import java.sql.Timestamp;

//TODO JAVADOC
/**
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public abstract class AbstractCommand implements Command {
    //TODO parameter public/private setzen
    JsonAccount jsonAccount;
    ChainSetup instance;
    Timestamp timestamp;

    //TODO JAVADOC
    public Timestamp getTimestamp(){
        return timestamp;
    }
    //TODO JAVADOC
    public AbstractCommand(JsonAccount jsonAccount){
        this.jsonAccount = jsonAccount;
        this.timestamp = jsonAccount.getTimeStamp();
        instance = ChainSetup.getInstance();
    }


}

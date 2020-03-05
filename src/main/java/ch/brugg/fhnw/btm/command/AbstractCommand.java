package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.pojo.Account;

import java.sql.Timestamp;

//TODO JAVADOC
/**
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public abstract class AbstractCommand implements Command {
    //TODO parameter public/private setzen
    Account account;
    ChainSetup instance;
    Timestamp timestamp;

    //TODO JAVADOC
    public Timestamp getTimestamp(){
        return timestamp;
    }
    //TODO JAVADOC
    public AbstractCommand(Account account){
        this.account = account;
        this.timestamp = account.getTimeStamp();
        instance = ChainSetup.getInstance();
    }


}

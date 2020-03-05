package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.pojo.Account;

import java.sql.Timestamp;


public abstract class AbstractCommand implements Command {
    Account account;
    ChainSetup instance;
    Timestamp timestamp;

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public AbstractCommand(Account account){
        this.account = account;
        this.timestamp = account.getTimeStamp();
        instance = ChainSetup.getInstance();
    }


}

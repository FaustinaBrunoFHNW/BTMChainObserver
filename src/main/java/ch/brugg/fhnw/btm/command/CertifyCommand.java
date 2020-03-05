package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO JAVADOC
/**
 * Command Pattern fürs Zertifizieren von Accounts
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class CertifyCommand extends AbstractCommand implements Command {
    private Logger log = LoggerFactory.getLogger(AbstractCommand.class);

    //TODO JAVADOC
    public CertifyCommand(Account account){
        super(account);
    }


    /**
     * In dieser Execute Methode wird ein Account zertifiziert und die Counter wieder zurückgesetzt
     */
    @Override
    public void execute() {
        log.info("Certifying Account mit folgender Adresse: " + account.getAddress());
        account.setTimeStamp(null);
        try {
            instance.getSimpleCertifier().certify(account.getAddress()).send();
            account.setTransactionCounter(account.getTransactionLimit().intValue());
            account.setGasUsedCounter(account.getGasLimit().intValue());
            log.info("Zertifizierung hat funktioniert.");
        } catch (Exception e) {
            log.error("Zertifizierung hat nicht funktioniert.");
            e.printStackTrace();
        }

    }
}

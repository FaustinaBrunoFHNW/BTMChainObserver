package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO JAVADOC
/**
 * Command Pattern fürs Revoken von Accounts
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class RevokeAccountCommand extends AbstractCommand implements Command {
    private Logger log = LoggerFactory.getLogger(AbstractCommand.class);
    public RevokeAccountCommand(Account account){
        super(account);
    }

    //TODO JAVADOC

    /**
     * In dieser Execute Methode wird ein Account aus der Whiteliste entfernt und somit gesperrt
     */
    @Override
    public void execute() {
        try {
            log.info("Der Account "+account.getAddress()+" wird versucht aus der Whiteliste zu entfernen.");

            //TODO timestemp setzten?
            instance.getSimpleCertifier().revoke(account.getAddress()).send();
            log.info("Der Account "+account.getAddress()+" wurde erfolgreich aus der Whiteliste  entfernt.");
        } catch (Exception e) {
            log.error("Account konnte nicht von Whitelist entfernt werden");
            e.printStackTrace();
        }

    }
}

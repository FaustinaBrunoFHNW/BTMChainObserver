package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Command Pattern fürs Revoken/Sperren von Accounts
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class RevokeAccountCommand extends AbstractCommand implements Command {
    private Logger log = LoggerFactory.getLogger(RevokeAccountCommand.class);

    /**
     * Constructor für RevokeAccountCommand
     * @param jsonAccount Account der gesperrt werden soll
     */
    public RevokeAccountCommand(JsonAccount jsonAccount) {
        super(jsonAccount);
    }

    /**
     * In dieser Execute Methode wird ein Account aus der Whiteliste entfernt und somit gesperrt
     */
    @Override public void execute() {
        try {
            this.log.info(
                    "Der Account " + jsonAccount.getAddress() + " wird versucht aus der Whiteliste zu entfernen.");
            instance.getSimpleCertifier().revoke(jsonAccount.getAddress()).send();
            this.log.info(
                    "Der Account " + jsonAccount.getAddress() + " wurde erfolgreich aus der Whiteliste  entfernt.");
        } catch (Exception e) {
            this.log.error("Account konnte nicht von Whitelist entfernt werden");
            e.printStackTrace();
        }

    }
}

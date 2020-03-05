package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevokeAccountCommand extends AbstractCommand implements Command {
    private Logger log = LoggerFactory.getLogger(AbstractCommand.class);
    public RevokeAccountCommand(Account account){
        super(account);
    }

    @Override
    public void execute() {
        try {
            instance.getSimpleCertifier().revoke(account.getAddress()).send();
        } catch (Exception e) {
            log.error("Account konnte nicht von Whitelist entfernt werden");
            e.printStackTrace();
        }

    }
}

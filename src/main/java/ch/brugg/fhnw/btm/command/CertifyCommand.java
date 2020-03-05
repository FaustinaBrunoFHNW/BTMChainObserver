package ch.brugg.fhnw.btm.command;

import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertifyCommand extends AbstractCommand implements Command {
    private Logger log = LoggerFactory.getLogger(AbstractCommand.class);
    public CertifyCommand(Account account){
        super(account);
    }

    @Override
    public void execute() {
        log.info("Certifying Account mit folgender Adresse: " + account.getAddress());
        account.setTimeStamp(null);
        try {
            instance.getSimpleCertifier().certify(account.getAddress()).send();
            account.setTransactionCounter(account.getTxLimit().intValue());
            account.setGasUsedCounter(account.getGasLimit().intValue());

            log.info("Zertifizierung hat funktioniert.");
        } catch (Exception e) {
            log.warn("Zertifizierung hat nicht funktioniert.");
            e.printStackTrace();
        }

    }
}

package ch.brugg.fhnw.btm.dosAlgorithm;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.command.CertifyCommand;
import ch.brugg.fhnw.btm.command.Command;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.handler.old.DefaultSettingsLoader;
import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

public class DoSAlgorithm {
    public static DoSAlgorithm instance;
    private ChainInteractions chainInteractions;
    private JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
    private JsonDefaultSettingsHandler defaultSettingsHandler =JsonDefaultSettingsHandler.getInstance();
    private  Logger log = LoggerFactory.getLogger(DoSAlgorithm.class);
    private Queue<Command> queue = new PriorityQueue<>(comparing(Command::getTimestamp));

    public static DoSAlgorithm getInstance() {

        if (DoSAlgorithm.instance == null) {
            DoSAlgorithm.instance = new DoSAlgorithm();
        }
        return DoSAlgorithm.instance;
    }

    public void setChainInteractions(ChainInteractions chainInteractions) {
        this.chainInteractions = chainInteractions;
    }

    private DoSAlgorithm(){

        log.info("Queue wird gestartet");
        new Thread(queueInspector).start();
        log.info("Queue wurde erfolgreich gestartet");
    }

    public void dosAlgorithm(Account account) {
        Timestamp tempStamp = new Timestamp(System.currentTimeMillis());
        long temp = tempStamp.getTime();
        if (account.getTransactionCounter() == 0) {
            this.chainInteractions.revokeAccount(account.getAddress());
            int intervall= this.defaultSettingsHandler.getDefaultSettings().getResetInterval();
           long revokeTime = account.getRevokeTime().intValue()*intervall *60 * 1000;

            tempStamp.setTime(temp + revokeTime);
            account.setTimeStamp(tempStamp);

            queue.add(new CertifyCommand(account));
            //TODO Account in Priority Queue werfen
            log.info("Der Acccount "+ account.getAddress()+" hat zu viele Transaktionen getätigt und wurde gesperrt. " +
                    " Die Sperrung  wird um "+ tempStamp.toString() +" aufgehoben ");

            accountHandler.writeAccountList();

        }  if (account.getGasUsedCounter() < 0) {
            this.chainInteractions.revokeAccount(account.getAddress());
            int intervall= this.defaultSettingsHandler.getDefaultSettings().getResetInterval();
            long revokeTime = account.getRevokeTime().intValue()*intervall *60 * 1000;
            tempStamp.setTime(temp + revokeTime);

            account.setTimeStamp(tempStamp);
            queue.add(new CertifyCommand(account));

            //TODO Account in Priority Queue werfen
            log.info("Der Acccount "+ account.getAddress()+" hat zu viel Gas verbraucht und wurde gesperrt. " +
                    " Die Sperrung  wird um "+ tempStamp.toString() +" aufgehoben ");

            accountHandler.writeAccountList();
        }
        log.info("Account Datei wird aktualisiert");
        JsonAccountHandler.getInstance().writeAccountList();
    }
    public void offerAccount(Account acc){
        queue.add(new CertifyCommand(acc));
    }
    Runnable queueInspector = () -> {
        log.info("Queue wurde gestartet und läuft.");
        while (true) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (queue.peek() !=null){
                log.info("Timestamp now: "+now.toString());
                log.info("Timestamp account: " +queue.peek().getTimestamp());
                if (!queue.peek().getTimestamp().after(now)){
                    queue.poll().execute();

                    //TODO Frage: darf man das hier?
                    this.accountHandler.writeAccountList();
                }

            }
            try {
                //TODO wieso 1000, müssen Zahlen als Konstanten definieren
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };


}

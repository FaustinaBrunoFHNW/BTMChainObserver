package ch.brugg.fhnw.btm.dosAlgorithm;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.command.CertifyCommand;
import ch.brugg.fhnw.btm.command.Command;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.sql.Timestamp;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

public class DoSAlgorithm {
    public static DoSAlgorithm instance;
    private ChainInteractions chainInteractions;
    private JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
    private static Logger log = LoggerFactory.getLogger(DoSAlgorithm.class);
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
        System.out.printf("Start Queue!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        new Thread(queueInspector).start();
        System.out.printf("fucking did it ..................................");
    }

    public void dosAlgorithm(Account account) {
        Timestamp tempStamp = new Timestamp(System.currentTimeMillis());
        long temp = tempStamp.getTime();
        if (account.getTransactionCounter() == 0) {
            this.chainInteractions.revokeAccount(account.getAddress());
            long revokeTime = account.getRevokeTime().intValue() *60 * 1000;

            tempStamp.setTime(temp + revokeTime);
            account.setTimeStamp(tempStamp);
            queue.add(new CertifyCommand(account));
            //TODO Account in Priority Queue werfen
            log.info("Der Acccount "+ account.getAddress()+" hat zu viele Transaktionen getätigt und wurde gesperrt. " +
                    " Die Sperrung  wird um "+ tempStamp.toString() +" aufgehoben ");

            accountHandler.writeAccountList();

        } else if (account.getGasUsedCounter() < 0) {
            this.chainInteractions.revokeAccount(account.getAddress());
            long revokeTime = account.getRevokeTime().intValue() *60 * 1000;
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
        log.info("Queue is up and running");
        while (true) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (queue.peek() !=null){
                if (queue.peek().getTimestamp().after(now)){
                    queue.poll().execute();
                }

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };


}

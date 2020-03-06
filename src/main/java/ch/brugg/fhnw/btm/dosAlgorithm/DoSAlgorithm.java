package ch.brugg.fhnw.btm.dosAlgorithm;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.command.CertifyCommand;
import ch.brugg.fhnw.btm.command.Command;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

//TODO JavaDov ergänzen
/**
 * In dieser Klasse ist der Dos Algorithmus implementiert, der gratis Transaktionen überwacht
 * Der Account der die Transaktion auslöst hat eine max. Anzahl Transaktionen und Gas die er benutzen kann,
 * bis er gesperrt wird
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class DoSAlgorithm {
    public static DoSAlgorithm instance;
    private ChainInteractions chainInteractions;
    private JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
    private JsonDefaultSettingsHandler defaultSettingsHandler =JsonDefaultSettingsHandler.getInstance();
    private  Logger log = LoggerFactory.getLogger(DoSAlgorithm.class);
    private Queue<Command> queue = new PriorityQueue<>(comparing(Command::getTimestamp));

    //TODO JAVADOC
    public static DoSAlgorithm getInstance() {

        if (DoSAlgorithm.instance == null) {
            DoSAlgorithm.instance = new DoSAlgorithm();
        }
        return DoSAlgorithm.instance;
    }
    //TODO JAVADOC
    public void setChainInteractions(ChainInteractions chainInteractions) {
        this.chainInteractions = chainInteractions;
    }
    //TODO JAVADOC
    private DoSAlgorithm(){

        this.log.info("Queue wird gestartet");
        new Thread(this.queueInspector).start();
        this.log.info("Queue wurde erfolgreich gestartet");
    }

    //TODO JAVADOC
    public void dosAlgorithm(JsonAccount jsonAccount) {


        if (jsonAccount.getAddress().equalsIgnoreCase(this.defaultSettingsHandler.getMasterKey())){
            this.log.info("a free transaction with the master key has been ignored");
            return;
        }

        Timestamp tempStamp = new Timestamp(System.currentTimeMillis());
        long temp = tempStamp.getTime();
        if (jsonAccount.getRemainingTransactions() == 0) {
            this.chainInteractions.revokeAccount(jsonAccount.getAddress());
           long revokeTime = jsonAccount.getRevokeTime().intValue() *60 * 1000;

            tempStamp.setTime(temp + revokeTime);
            jsonAccount.setTimeStamp(tempStamp);

            this.queue.add(new CertifyCommand(jsonAccount));
            //TODO Account in Priority Queue werfen
            this.log.info("Der Acccount "+ jsonAccount.getAddress()+" hat zu viele Transaktionen getätigt und wurde gesperrt. " +
                    " Die Sperrung  wird um "+ tempStamp.toString() +" aufgehoben ");

            this.accountHandler.writeAccountList();

        }  if (jsonAccount.getRemainingGas() < 0) {
            this.chainInteractions.revokeAccount(jsonAccount.getAddress());
            long revokeTime = jsonAccount.getRevokeTime().intValue()*60 * 1000;
            tempStamp.setTime(temp + revokeTime);

            jsonAccount.setTimeStamp(tempStamp);
            this.queue.add(new CertifyCommand(jsonAccount));

            //TODO Account in Priority Queue werfen
            this.log.info("Der Acccount "+ jsonAccount.getAddress()+" hat zu viel Gas verbraucht und wurde gesperrt. " +
                    " Die Sperrung  wird um "+ tempStamp.toString() +" aufgehoben ");

            this.accountHandler.writeAccountList();
        }
        this.log.info("Account Datei wird aktualisiert");
        JsonAccountHandler.getInstance().writeAccountList();
    }
    //TODO JAVADOC
    public void offerAccount(JsonAccount acc){
        this.queue.add(new CertifyCommand(acc));
    }
    Runnable queueInspector = () -> {
        this.log.info("Queue wurde gestartet und läuft.");
        while (true) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (this.queue.peek() !=null){
                this.log.info("Timestamp now: "+now.toString());
                this.log.info("Timestamp account: " + this.queue.peek().getTimestamp());
                //TODO wieso ist es hier umgekehrt
                if (!this.queue.peek().getTimestamp().after(now)){
                    this.queue.poll().execute();
                    //TODO Frage: darf man das hier?
                    this.accountHandler.writeAccountList();
                }

            }
            try {
                //TODO wieso 1000, müssen Zahlen als Konstanten definieren
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //TODO error Meldung schreiben
                this.log.error("");
                e.printStackTrace();
            }

        }
    };


}

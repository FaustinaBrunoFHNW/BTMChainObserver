package ch.brugg.fhnw.btm.handler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * In dieser Klasse werden Accounts aus dem JSON File geladen und wieder reingeschrieben
 *
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class JsonAccountHandler {

    private static JsonAccountHandler instance;
    private static JsonDefaultSettingsHandler jsonDefaultSettingsHandler;
    private ArrayList<JsonAccount> jsonAccountList = new ArrayList<>();
    private  Logger log = LoggerFactory.getLogger(JsonAccountHandler.class);
    private int deletedAccounts = 0, revokedAccounts = 0;


    private String accountsFile = "src/main/resources/whitelist/AccountList.json";


//TODO JAVADOC
    public static JsonAccountHandler getInstance() {

        if (JsonAccountHandler.instance == null) {
            JsonAccountHandler.instance = new JsonAccountHandler();
        }
        return JsonAccountHandler.instance;
    }

    //TODO JAVADOC
    private JsonAccountHandler(){
        jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
    }

    //TODO JAVADOC
    public void loadAccounts() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(this.accountsFile));
            Type accountListType = new TypeToken<ArrayList<JsonAccount>>(){}.getType();
            this.jsonAccountList = gson.fromJson(reader, accountListType);
            this.log.info("Accounts wurden aus Datei geladen. Es sind " + this.jsonAccountList.size()+ " Accounts geladen worden. ");


            Iterator<JsonAccount> itr = this.jsonAccountList.iterator();
            while(itr.hasNext()){
                JsonAccount temp = itr.next();
                if (temp.deleteMe){
                    this.log.info("Account zum Löschen wurde gefunden. Folgender Account wird gelöscht: "+temp.getAddress());
                    this.deletedAccounts++;
                    itr.remove();
                }

            }
        } catch (FileNotFoundException e) {
            //TODO error log ausfüllen
            this.log.error("");
            e.printStackTrace();
        }
    }


    //TODO JAVADOC
    public void writeAccountList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(this.accountsFile));
            gson.toJson(this.jsonAccountList,writer);
            writer.close();
            //TODO log Info ausfüllen
            this.log.info("");
        } catch (IOException e) {
            //TODO error log ausfüllen
            this.log.error("");
            e.printStackTrace();

        }
    }

    //TODO JAVADOC
    //TODO public?
    public JsonAccount getAccount(String address){
        for (JsonAccount acc: this.jsonAccountList){
            if (acc.getAddress().equalsIgnoreCase(address)){
                return acc;
            }
        }
        return null;
    }

    //TODO JAVADOC
    public JsonAccount processAccount(String address, BigInteger gasUsed){
        JsonAccount toProcess = this.getAccount(address);
        toProcess.decraseTransactionCounter();
        toProcess.decreaseGasUsedCounter(gasUsed.longValue());

        this.log.info("Account: " + toProcess.getAddress() + " hat noch " + toProcess.getRemainingTransactions()
                + " Transaktionen auf dem Counter und noch so viel Gas zum verbauchen " + toProcess
                .getRemainingGas());
        return toProcess;
    }


    //******************GETTER und SETTER*******************************

    public ArrayList<JsonAccount> getJsonAccountList() {
        return this.jsonAccountList;
    }

    public void setJsonAccountList(ArrayList<JsonAccount> jsonAccountList) {
        this.jsonAccountList = jsonAccountList;
    }

    public int getDeletedAccounts() {
        return this.deletedAccounts;
    }

    public int getRevokedAccounts() {
        return this.revokedAccounts;
    }
}

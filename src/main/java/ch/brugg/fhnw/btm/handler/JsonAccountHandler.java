package ch.brugg.fhnw.btm.handler;
import ch.brugg.fhnw.btm.dosAlgorithm.DoSAlgorithm;
import ch.brugg.fhnw.btm.pojo.Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonAccountHandler {

    private static JsonAccountHandler instance;
    private static JsonDefaultSettingsHandler JSONDefaultSettingsHandler;
    private ArrayList<Account> accountList = new ArrayList<>();
    private static Logger log = LoggerFactory.getLogger(JsonAccountHandler.class);
    private int deletedAccounts = 0, revokedAccounts = 0;


    private String accountsFile = "src/main/resources/whitelist/AccountList.json";



    public static JsonAccountHandler getInstance() {

        if (JsonAccountHandler.instance == null) {
            JsonAccountHandler.instance = new JsonAccountHandler();
        }
        return JsonAccountHandler.instance;
    }

    private JsonAccountHandler(){
        this.JSONDefaultSettingsHandler = JSONDefaultSettingsHandler.getInstance();
    }

    public void loadAccounts() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(accountsFile));
            Type accountListType = new TypeToken<ArrayList<Account>>(){}.getType();
            accountList = gson.fromJson(reader, accountListType);
            log.info("Accounts wurden aus Datei geladen. Es sind " + accountList.size()+ " Accounts geladen worden. ");


            Iterator<Account> itr = accountList.iterator();
            while(itr.hasNext()){
                Account temp = itr.next();
                if (temp.deleteMe){
                    log.info("Account zum Löschen wurde gefunden. Folgender Account wird gelöscht: "+temp.getAddress());
                    deletedAccounts++;
                    itr.remove();
                }

            }
            certifyLoadedAccounts();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void writeAccountList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(accountsFile));
            gson.toJson(accountList,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public Account getAccount(String address){
        for (Account acc: accountList){
            if (acc.getAddress().equalsIgnoreCase(address)){
                return acc;
            }
        }
        return null;
    }

    public Account processAccount(String address, BigInteger gasUsed){
        Account toProcess = getAccount(address);
        toProcess.decraseTransactionCounter();
        toProcess.decreaseGasUsedCounter(gasUsed.longValue());

        log.info("Account: " + toProcess.getAddress() + " hat noch " + toProcess.getTransactionCounter()
                + " Transaktionen auf dem Counter und noch so viel Gas zum verbauchen " + toProcess
                .getGasUsedCounter());
        return toProcess;
    }

    //TODO WAS passiert hier
    public void certifyLoadedAccounts(){

    }

    //******************GETTER und SETTER*******************************

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;
    }

    public int getDeletedAccounts() {
        return deletedAccounts;
    }

    public int getRevokedAccounts() {
        return revokedAccounts;
    }
}

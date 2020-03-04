package ch.brugg.fhnw.btm.handler;

import ch.brugg.fhnw.btm.handler.old.AccountLoader;
import ch.brugg.fhnw.btm.pojo.Account;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JsonAccountHandler {

    private static JsonAccountHandler instance;
    private static JsonDefaultSettingsHandler JSONDefaultSettingsHandler;
    private ArrayList<Account> accountList;
    private ArrayList<Account> revokedAccountList;
    private ArrayList<Account> deleteAccountList;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);

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


            System.out.println("done");
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

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;
    }

    public ArrayList<Account> getRevokedAccountList() {
        return revokedAccountList;
    }

    public void setRevokedAccountList(ArrayList<Account> revokedAccountList) {
        this.revokedAccountList = revokedAccountList;
    }

    public ArrayList<Account> getDeleteAccountList() {
        return deleteAccountList;
    }

    public void setDeleteAccountList(ArrayList<Account> deleteAccountList) {
        this.deleteAccountList = deleteAccountList;
    }
}

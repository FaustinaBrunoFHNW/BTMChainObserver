package ch.brugg.fhnw.btm.loader;

import ch.brugg.fhnw.btm.pojo.Account;
import ch.brugg.fhnw.btm.pojo.MasterKey;
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
import java.time.Instant;
import java.util.ArrayList;

public class JSONAccountLoader {

    private static JSONAccountLoader instance;
    private static  JSONDefaultSettingsLoader JSONDefaultSettingsLoader;
    private MasterKey masterKey;

    private ArrayList<Account> accountArrayList;
    private ArrayList<Account> revokedAccountArrayList;
    private ArrayList<Account> deleteAccountList;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);

    private String accountsFile = "src/main/resources/whitelist/AccountList.json";
    private String transaktionManagerAccountFile = "src/main/resources/whitelist/TransaktionsManagerAccount.json";


    public static JSONAccountLoader getInstance() {

        if (JSONAccountLoader.instance == null) {
            JSONAccountLoader.instance = new JSONAccountLoader();
        }
        return JSONAccountLoader.instance;
    }

    private JSONAccountLoader(){
        this.JSONDefaultSettingsLoader = JSONDefaultSettingsLoader.getInstance();
    }

    public void loadAccounts() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(accountsFile));
            Type accountListType = new TypeToken<ArrayList<Account>>(){}.getType();
            accountArrayList = gson.fromJson(reader, accountListType);

            setDefaultValues();

            System.out.println("done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultValues(){
        for (Account acc : accountArrayList){
            if (acc.getTransaktionCounter() == 0){
                acc.setTransaktionCounter(JSONDefaultSettingsLoader.getDefaultSettings().getDefaultTransaktionCount());
            }
            if (acc.getGasUsedCounter() == 0){
                acc.setGasUsedCounter(JSONDefaultSettingsLoader.getDefaultSettings().getDefaultGasUsedCount());
            }
            if (acc.getRevokeMultiplier()== 0){
                acc.setRevokeMultiplier(JSONDefaultSettingsLoader.getDefaultSettings().getRevokeMultiplier());
            }
        }
    }

    public String getMasterKey(){
        Gson gson = new Gson();
        try {

            JsonReader reader = new JsonReader(new FileReader(transaktionManagerAccountFile));
            masterKey = gson.fromJson(reader, MasterKey.class);
            return  masterKey.getPrivateKey();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return  null;
        }

    }

    public boolean writeAccountList() {


        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(accountsFile));

            gson.toJson(accountArrayList,writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



}

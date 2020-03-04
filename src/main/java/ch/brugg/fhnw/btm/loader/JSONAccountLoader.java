package ch.brugg.fhnw.btm.loader;

import ch.brugg.fhnw.btm.pojo.Account;
import ch.brugg.fhnw.btm.pojo.DefaultSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONAccountLoader {

    private static JSONAccountLoader instance;
    private JSONDefaultSettingsLoader JSONDefaultSettingsLoader;

    private ArrayList<Account> accountArrayList;
    private ArrayList<Account> revokedAccountArrayList;
    private ArrayList<Account> deleteAccountList;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);

    private String accountsFile = "src/main/resources/whitelist/AccountList.json";
    private String transaktionManagerAccountFile = "src/main/resources/whitelist/TransaktionManagerAccount.json";


    public static JSONAccountLoader getInstance() {

        if (JSONAccountLoader.instance == null) {
            JSONAccountLoader.instance = new JSONAccountLoader();
        }
        return JSONAccountLoader.instance;
    }

    public void loadAccounts() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(accountsFile));
            Type accountListType = new TypeToken<ArrayList<Account>>(){}.getType();
            accountArrayList = gson.fromJson(reader, accountListType);

            System.out.println("done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}

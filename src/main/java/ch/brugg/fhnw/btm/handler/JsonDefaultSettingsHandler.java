package ch.brugg.fhnw.btm.handler;

import ch.brugg.fhnw.btm.handler.old.DefaultSettingsLoader;
import ch.brugg.fhnw.btm.pojo.JsonDefaultSettings;
import ch.brugg.fhnw.btm.pojo.MasterKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;

public class JsonDefaultSettingsHandler {

    private Logger log = LoggerFactory.getLogger(DefaultSettingsLoader.class);

    private static JsonDefaultSettingsHandler instance;
    private MasterKey masterKey;

    private JsonDefaultSettings defaultSettings = JsonDefaultSettings.getInstance();

    private String defaultSettingsFile = "src/main/resources/whitelist/DefaultSettings.json";
    private String transaktionManagerAccountFile = "src/main/resources/whitelist/TransaktionsManagerAccount.json";


    public static JsonDefaultSettingsHandler getInstance() {

        if (JsonDefaultSettingsHandler.instance == null) {
            JsonDefaultSettingsHandler.instance = new JsonDefaultSettingsHandler();
        }
        return JsonDefaultSettingsHandler.instance;
    }


    public void loadDefaultSettings() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(defaultSettingsFile));
            defaultSettings = gson.fromJson(reader, JsonDefaultSettings.class);
            System.out.println("done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeDefaultSettings() {


        defaultSettings.setTimestampLastReset(new Timestamp(System.currentTimeMillis()));

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(defaultSettingsFile));
            gson.toJson(defaultSettings,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    public JsonDefaultSettings getDefaultSettings() {
        return defaultSettings;
    }

}

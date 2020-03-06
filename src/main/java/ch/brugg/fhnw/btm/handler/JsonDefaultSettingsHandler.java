package ch.brugg.fhnw.btm.handler;

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

/**
 * In dieser Klasse werden die Defaul Setting aus dem JSON File gelesen und wieder reingeschrieben
 * @Author Faustina Bruno, Serge-Jurij Maikoff
 */
public class JsonDefaultSettingsHandler {

    private Logger log = LoggerFactory.getLogger(JsonDefaultSettingsHandler.class);

    private static JsonDefaultSettingsHandler instance;
    private MasterKey masterKey;

    private JsonDefaultSettings defaultSettings = JsonDefaultSettings.getInstance();

    private String defaultSettingsFile = "src/main/resources/whitelist/DefaultSettings.json";
    private String transaktionManagerAccountFile = "src/main/resources/whitelist/TransaktionsManagerAccount.json";

    //TODO JAVADOC
    public static JsonDefaultSettingsHandler getInstance() {

        if (JsonDefaultSettingsHandler.instance == null) {
            JsonDefaultSettingsHandler.instance = new JsonDefaultSettingsHandler();
        }
        return JsonDefaultSettingsHandler.instance;
    }

    //TODO JAVADOC
    public void loadDefaultSettings() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(this.defaultSettingsFile));
            this.defaultSettings = gson.fromJson(reader, JsonDefaultSettings.class);
            this.log.info("Default Settings wurden aus dem JSON File geladen");
        } catch (FileNotFoundException e) {
            //TODO error log ausfüllen
            this.log.error("");
            e.printStackTrace();
        }
    }
    //TODO JAVADOC
    public void writeDefaultSettings() {

        this.defaultSettings.setTimestampLastReset(new Timestamp(System.currentTimeMillis()));

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(this.defaultSettingsFile));
            gson.toJson(this.defaultSettings,writer);
            //TODO log info ausfüllen
            this.log.info("");
            writer.close();
        } catch (IOException e) {
            //TODO error log ausfüllen
            this.log.error("");
            e.printStackTrace();
        }

    }


    //TODO JAVADOC
    public String getMasterKey(){
        Gson gson = new Gson();
        try {

            JsonReader reader = new JsonReader(new FileReader(this.transaktionManagerAccountFile));
            this.masterKey = gson.fromJson(reader, MasterKey.class);
            //TODO log info ausfüllen
            this.log.info("");
            return  this.masterKey.getPrivateKey();
        } catch (FileNotFoundException e) {
            //TODO error log ausfüllen
            this.log.error("");
            e.printStackTrace();
            return  null;
        }

    }


    //*************************GETTER und SETTER *************************

    public JsonDefaultSettings getDefaultSettings() {
        return this.defaultSettings;
    }

}

package ch.brugg.fhnw.btm.handler;

import ch.brugg.fhnw.btm.pojo.JsonDefaultSettings;
import ch.brugg.fhnw.btm.pojo.MasterKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;


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
 * @Author Faustina Bruno, Serge Jurij Maikoff
 */
public class JsonDefaultSettingsHandler {

    private Logger log = LoggerFactory.getLogger(JsonDefaultSettingsHandler.class);

    private static JsonDefaultSettingsHandler instance;
    private MasterKey masterKey;

    private JsonDefaultSettings defaultSettings = JsonDefaultSettings.getInstance();

//    private String defaultSettingsFile = "src/main/resources/whitelist/DefaultSettings.json";
//    private String transaktionManagerAccountFile = "src/main/resources/whitelist/TransaktionsManagerAccount.json";

    private String defaultSettingsFile = "./DefaultSettings.json";
    private String transaktionManagerAccountFile = "./TransaktionsManagerAccount.json";

    /**
     * Getter um Singleton Instanz der Klasse zu holen
     * @return
     */
    public static JsonDefaultSettingsHandler getInstance() {

        if (JsonDefaultSettingsHandler.instance == null) {
            JsonDefaultSettingsHandler.instance = new JsonDefaultSettingsHandler();
        }
        return JsonDefaultSettingsHandler.instance;
    }


    /**
     * Ladet aus der JSON Datei die gesetzten Default Settings und speiehrt diese in einem JsonDefaultSettings Objekt ab
     */
    public void loadDefaultSettings() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(this.defaultSettingsFile));
            this.defaultSettings = gson.fromJson(reader, JsonDefaultSettings.class);
            this.log.info("Default Settings wurden aus dem JSON File geladen");
        } catch (FileNotFoundException e) {
            this.log.error("Beim laden der Default Settings gabs Probleme");
            e.printStackTrace();
        }
    }

    /**
     * In dieser Methode wird ein neuer Timestamp gesetzt und
     * die aktuellen Settings der DefaultSettings in die Datei gespeichert
     */
    public void writeDefaultSettings() {

        this.defaultSettings.setTimestampLastReset(new Timestamp(System.currentTimeMillis()));

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(this.defaultSettingsFile));
            gson.toJson(this.defaultSettings,writer);
            this.log.info("DefaultSettings werden geschrieben");
            writer.close();
        } catch (IOException e) {
            this.log.error("Default Settings konnten nicht geschrieben werden");
            e.printStackTrace();
        }

    }




    /**
     * Diese Methode holt den privat Key des transaktion Manager Accounts,
     * welches in einer separaten Datei gespeichert wird
     * @return der Account Private Key des TM Accounts
     */
    public String getMasterKey(){
        Gson gson = new Gson();
        try {

            JsonReader reader = new JsonReader(new FileReader(this.transaktionManagerAccountFile));
            this.masterKey = gson.fromJson(reader, MasterKey.class);
            JsonDefaultSettings.getInstance().setMasterKeyAddress(Credentials.create(this.masterKey.getPrivateKey()).getAddress());
            this.log.info("Private Key des Transaktionsmanagers ist geladen");
            return  this.masterKey.getPrivateKey();
        } catch (FileNotFoundException e) {

            this.log.error("Private Key des Transaktionsmanagers konnte nich geladen werden");
            e.printStackTrace();
            return  null;
        }

    }


    //*************************GETTER und SETTER *************************

    public JsonDefaultSettings getDefaultSettings() {
        return this.defaultSettings;
    }

}

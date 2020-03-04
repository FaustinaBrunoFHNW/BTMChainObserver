package ch.brugg.fhnw.btm.loader;

import ch.brugg.fhnw.btm.pojo.DefaultSettings;
import ch.brugg.fhnw.btm.pojo.JSONDefaultSettings;
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
import java.time.Instant;
import java.time.LocalTime;

public class JSONDefaultSettingsLoader {

    private static Logger log = LoggerFactory.getLogger(DefaultSettingsLoader.class);

    private static JSONDefaultSettingsLoader instance;

    private JSONDefaultSettings defaultSettings;

    private String defaultSettingsFile = "src/main/resources/whitelist/DefaultSettings.json";


    public static JSONDefaultSettingsLoader getInstance() {

        if (JSONDefaultSettingsLoader.instance == null) {
            JSONDefaultSettingsLoader.instance = new JSONDefaultSettingsLoader();
        }
        return JSONDefaultSettingsLoader.instance;
    }


    public void loadDefaultSettings() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(defaultSettingsFile));
            defaultSettings = gson.fromJson(reader, JSONDefaultSettings.class);
            System.out.println("done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean writeDefaultSettings() {


        defaultSettings.setTimestampLastReset(Instant.now().toString());

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
        Writer writer = Files.newBufferedWriter(Paths.get(defaultSettingsFile));

        gson.toJson(defaultSettings,writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public JSONDefaultSettings getDefaultSettings() {
        return defaultSettings;
    }

}

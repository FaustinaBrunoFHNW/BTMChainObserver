package ch.brugg.fhnw.btm.loader;

import ch.brugg.fhnw.btm.loader.old.DefaultSettingsLoader;
import ch.brugg.fhnw.btm.pojo.JsonDefaultSettings;
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

public class JsonDefaultSettingsLoader {

    private static Logger log = LoggerFactory.getLogger(DefaultSettingsLoader.class);

    private static JsonDefaultSettingsLoader instance;

    private JsonDefaultSettings defaultSettings = JsonDefaultSettings.getInstance();

    private String defaultSettingsFile = "src/main/resources/whitelist/DefaultSettings.json";


    public static JsonDefaultSettingsLoader getInstance() {

        if (JsonDefaultSettingsLoader.instance == null) {
            JsonDefaultSettingsLoader.instance = new JsonDefaultSettingsLoader();
        }
        return JsonDefaultSettingsLoader.instance;
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

    public JsonDefaultSettings getDefaultSettings() {
        return defaultSettings;
    }

}

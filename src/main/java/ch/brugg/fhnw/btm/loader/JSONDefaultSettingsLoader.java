package ch.brugg.fhnw.btm.loader;

import ch.brugg.fhnw.btm.pojo.DefaultSettings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileNotFoundException;
import java.io.FileReader;

public class JSONDefaultSettingsLoader {

    private static Logger log = LoggerFactory.getLogger(DefaultSettingsLoader.class);

    private static JSONDefaultSettingsLoader instance;

    private DefaultSettings defaultSettings;
    private String certifierAdd;

    private String defaultSettingsFile = "src/main/resources/whitelist/DefaultSettings.json";


    public static JSONDefaultSettingsLoader getInstance() {

        if (JSONDefaultSettingsLoader.instance == null) {
            JSONDefaultSettingsLoader.instance = new JSONDefaultSettingsLoader();
        }
        return JSONDefaultSettingsLoader.instance;
    }

    private JSONDefaultSettingsLoader(){

    }

    public void loadDefaultSettings() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(defaultSettingsFile));
            defaultSettings = gson.fromJson(reader, DefaultSettings.class);
            System.out.println("done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




}

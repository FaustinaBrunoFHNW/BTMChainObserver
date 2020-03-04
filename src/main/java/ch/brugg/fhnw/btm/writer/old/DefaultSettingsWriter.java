package ch.brugg.fhnw.btm.writer.old;

import ch.brugg.fhnw.btm.handler.old.DefaultSettingsLoader;
import ch.brugg.fhnw.btm.pojo.old.DefaultSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DefaultSettingsWriter {

    private File defaultSettingsFile = new File("src/main/resources/whitelist/DefaultSettings.txt");
    private static DefaultSettingsWriter instance;
    private static Logger log = LoggerFactory.getLogger(DefaultSettingsWriter.class);
    private DefaultSettingsLoader defaultSettingsLoader;

    /**
     * Privater Constructor für Singleton
     */
    private DefaultSettingsWriter() {
        this.defaultSettingsLoader=DefaultSettingsLoader.getInstance();
    }

    /**
     * @return die einmalige Instanz der Klasse
     */
    public static DefaultSettingsWriter getInstance() {
        if (DefaultSettingsWriter.instance == null) {
            DefaultSettingsWriter.instance = new DefaultSettingsWriter();
        }
        return DefaultSettingsWriter.instance;
    }



    //TODO JAVADOC und Impl
    public void writeDefaultSettingsInFile() throws IOException {
        FileWriter fw = new FileWriter(defaultSettingsFile);
        BufferedWriter bw = new BufferedWriter(fw);
        log.info("Default Settings werden in Datei gespeichert.");
        bw.write(this.prepareDefaultSeetingsLineForFile(defaultSettingsLoader.getDefaultSettings()));
        bw.newLine();
        bw.close();
        log.info("Daten wurden in datei gespeichert.");
    }

    private String prepareDefaultSeetingsLineForFile(DefaultSettings defaultSettings) {
        String defaultSettingsLine;
        defaultSettingsLine =
                defaultSettings.getConnectionAddress() + ";" +
                        defaultSettings.getResetIntervall() + ";" +
                        defaultSettings.getRevokeMultiplier() + ";" +
                        defaultSettings.getDefaultTransaktionCount() + ";" +
                        defaultSettings.getDefaultGasUsedCount() + ";" +
                        defaultSettings.getRegisterAddress()+ ";" +
                        defaultSettings.getCertifyierAddress() + ";" +
                        defaultSettings.getTimestampLastReset();
        return defaultSettingsLine;

    }
}

package ch.brugg.fhnw.btm.loader.old;

import ch.brugg.fhnw.btm.pojo.old.DefaultSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DefaultSettingsLoader {

    private static Logger log = LoggerFactory.getLogger(DefaultSettingsLoader.class);

    private static DefaultSettingsLoader instance;

    private DefaultSettings defaultSettings;
    private String certifierAdd;

    private File defaultSettingsFile = new File("src/main/resources/whitelist/DefaultSettings.txt");

    private DefaultSettingsLoader() {

    }

    /**
     * Instanziert eine Instanz der Klasse falls es noch keine gibt und gibt
     * die existierende oder eben die neue zurück
     *
     * @return die einmalige Instanz der Klasse
     */
    public static DefaultSettingsLoader getInstance() {

        if (DefaultSettingsLoader.instance == null) {
            DefaultSettingsLoader.instance = new DefaultSettingsLoader();
        }
        return DefaultSettingsLoader.instance;
    }

    public void loadDefaultSettings() {
        log.info("Laden der Default Settings");
        try {
            FileReader fileReader = new FileReader(defaultSettingsFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");

                if (accountArray.length == 6) {
                    this.readDefaultValues(accountArray);

                } else if (accountArray.length == 8) {
                    this.readDefaultValuesWithCerfierAdd(accountArray);
                }

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * In dieser Methode wird ein DefaultSettings Objekt aus den ausgelesenen Attributen der Datei
     * erstellt.
     *
     * @param fileInput String Array mit den DefaultSettings Attributen
     */
    private void readDefaultValues(String[] fileInput) {
        defaultSettings = new DefaultSettings(fileInput[0], fileInput[1], fileInput[2], fileInput[3], fileInput[4], fileInput[5]);

        log.info("******************DEFAULT WERTE***************");
        log.info("Connection Adresse " + fileInput[0]);
        log.info("ResetCounter Intervall wurde auf " + fileInput[1] + " Minuten gesetzt");
        log.info("Revoke Zeit wurde auf " + fileInput[2] + " Intervalle  gesetzt");
        log.info("Default Max Transaktionen wurden auf " + fileInput[3] + " gesetzt");
        log.info("Default Max Gas Used wurde auf " + fileInput[4] + " gesetzt");
        log.info("Register Adresse: "+fileInput[5]);

    }

    /**
     * In dieser Methode wird ein DefaultSettings Objekt aus den ausgelesenen Attributen der Datei
     * erstellt. Hier ist die CertifierAdd dabei
     *
     * @param fileInput String Array mit den DefaultSettings Attributen
     */
    private void readDefaultValuesWithCerfierAdd(String[] fileInput) {
        defaultSettings = new DefaultSettings(fileInput[0], fileInput[1], fileInput[2], fileInput[3], fileInput[4],fileInput[5]);
        if (fileInput.length > 6) {
            defaultSettings.setCertifyierAddress(fileInput[6]);
            defaultSettings.setTimestampLastReset(Long.parseLong(fileInput[7]));
        }

        log.info("******************DEFAULT WERTE***************");
        log.info("Connection Adresse " + fileInput[0]);
        log.info("ResetCounter Intervall wurde auf " + fileInput[1] + " Minuten gesetzt");
        log.info("Revoke Zeit wurde auf " + fileInput[2] + " Intervalle  gesetzt");
        log.info("Default Max Transaktionen wurden auf " + fileInput[3] + " gesetzt");
        log.info("Default Max Gas Used wurde auf " + fileInput[4] + " gesetzt");

    }


    public void setCertifierAddress(String certfierAdd) {
        this.defaultSettings.setCertifyierAddress(certfierAdd);
    }

    public DefaultSettings getDefaultSettings() {
        return defaultSettings;
    }

    public void setDefaultSettings(DefaultSettings defaultSettings) {
        this.defaultSettings = defaultSettings;
    }

    public String getCertifierAdd() {
        return certifierAdd;
    }

    public void setCertifierAdd(String certifierAdd) {
        this.certifierAdd = certifierAdd;
    }
}

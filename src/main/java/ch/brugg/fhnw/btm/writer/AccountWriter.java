package ch.brugg.fhnw.btm.writer;

import ch.brugg.fhnw.btm.loader.FileLoader;
import ch.brugg.fhnw.btm.pojo.Account;
import ch.brugg.fhnw.btm.pojo.DefaultSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Diese Klasse ist dafür da,
 * alle Informationen der Accounts regelmässig zu speichern,
 * falls das System unterbrochen wird
 */
public class AccountWriter {

    private File accountFile = new File("src/main/resources/whitelist/AccountList.txt");
    private File defaultSettingsFile = new File("src/main/resources/whitelist/DefaultSettings.txt");
    private static AccountWriter instance;
    private static Logger log = LoggerFactory.getLogger(AccountWriter.class);
    private FileLoader fileLoader;

    //TODO

    /**
     * Privater Constructor für Singleton
     */
    private AccountWriter() {
        this.fileLoader = FileLoader.getInstance();
    }

    /**
     * @return die einmalige Instanz der Klasse
     */
    public static AccountWriter getInstance() {
        if (AccountWriter.instance == null) {
            AccountWriter.instance = new AccountWriter();
        }
        return AccountWriter.instance;
    }

    /**
     * Methode fürs Schreiben der Accounts ins AccountFiles
     *
     * @throws IOException
     */
    public void writeAccountsInFile() throws IOException {

        FileWriter fw = new FileWriter(accountFile);
        BufferedWriter bw = new BufferedWriter(fw);

        log.info("Alle Accounts werden in Datei gespeichert.");

        log.info(fileLoader.getRevokedAccountArrayList().size() + " Accounts sind blockiert");
        for (Account account : fileLoader.getRevokedAccountArrayList()) {
            bw.write(this.prepareAccountLineForFile(account));
            log.info(account.getAdressValue());
            bw.newLine();

        }

        log.info(fileLoader.getAccountArrayList().size() + " Accounts sind zertifiziert");
        for (Account account : fileLoader.getAccountArrayList()) {

            bw.write(this.prepareAccountLineForFile(account));
            log.info(account.getAdressValue());
            bw.newLine();

        }
        bw.close();
        log.info("Daten wurden in datei gespeichert.");
    }

    //TODO JAVADOC und Impl
    public void writeDefaultSettingsInFile() throws IOException {
        FileWriter fw = new FileWriter(defaultSettingsFile);
        BufferedWriter bw = new BufferedWriter(fw);
        log.info("Default Settings werden in Datei gespeichert.");
        bw.write(this.prepareDefaultSeetingsLineForFile(fileLoader.getDefaultSettings()));
        bw.newLine();
        bw.close();
        log.info("Daten wurden in datei gespeichert.");
    }

    private String prepareDefaultSeetingsLineForFile(DefaultSettings defaultSettings) {
        String defaultSettingsLine;
        defaultSettingsLine =
                defaultSettings.getConnectionAddress() + ";" +
                        defaultSettings.getIntervalResetCounter() + ";" +
                        defaultSettings.getIntervalRevoke() + ";" +
                        defaultSettings.getDefaultTransaktionCount() + ";" +
                        defaultSettings.getDefaultGasUsedCount() + ";" +
                        defaultSettings.getRegisterAddress()+ ";" +
                        defaultSettings.getCertifyierAddress() + ";" +
                        defaultSettings.getTimestampLastReset();
        return defaultSettingsLine;

    }

    private String prepareAccountLineForFile(Account account) {
        String accountLine;
        //TODO Default Werte nicht speichern
        if (account.isDefaultSettings()) {
            accountLine =
                    account.getAdressValue() + ";" + account.getMaxTransaktionCounter() + ";" + account.getMaxGasUsed()
                            + ";" + account.getRevokePeriodCounter() + ";" + account.getRevoked();
        } else {
            accountLine =
                    account.getAdressValue() + ";" + account.getMaxTransaktionCounter() + ";" + account.getMaxGasUsed()
                            + ";" + account.getRevokePeriodCounter() + ";" + account.getRevoked();
        }
        return accountLine;
    }
}

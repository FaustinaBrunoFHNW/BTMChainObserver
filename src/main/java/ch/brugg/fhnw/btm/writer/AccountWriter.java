package ch.brugg.fhnw.btm.writer;

import ch.brugg.fhnw.btm.loader.AccountLoader;
import ch.brugg.fhnw.btm.pojo.Account;
import ch.brugg.fhnw.btm.pojo.DefaultSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Diese Klasse ist dafür da,
 * alle Informationen der Accounts regelmässig zu speichern,
 * falls das System unterbrochen wird
 */
public class AccountWriter {

    private File file = new File("src/main/resources/whitelist/Accounts.txt");
    private static AccountWriter instance;
    private static Logger log = LoggerFactory.getLogger(AccountWriter.class);
    private AccountLoader accountLoader;

    //TODO

    /**
     * Privater Constructor für Singleton
     */
    private AccountWriter() {
        this.accountLoader = AccountLoader.getInstance();
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

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        log.info("Alle Accounts werden in Datei gespeichert.");

        log.info(accountLoader.getRevokedAccountArrayList().size()+" Accounts sind blockiert");
        for (Account account : accountLoader.getRevokedAccountArrayList()) {


                bw.write(this.prepareAccountLineForFile(account));
                log.info(account.getAdressValue());
                bw.newLine();

        }

        log.info(accountLoader.getAccountArrayList().size()+" Accounts sind zertifiziert");
        for (Account account : accountLoader.getAccountArrayList()) {

                bw.write(this.prepareAccountLineForFile(account));
                log.info(account.getAdressValue());
                bw.newLine();

        }
        bw.close();
        log.info("Daten wurden in datei gespeichert.");
    }


    //TODO JAVADOC und Impl
    public void writeDefaultSettingsInFile() throws IOException {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        log.info("Default Settings werden in Datei gespeichert.");
        bw.write(this.prepareDefaultSeetingsLineForFile(accountLoader.getDefaultSettings()));
        bw.newLine();
        bw.close();
        log.info("Daten wurden in datei gespeichert.");
    }

    private String prepareDefaultSeetingsLineForFile(DefaultSettings defaultSettings) {
        String defaultSettingsLine;
        defaultSettingsLine =
                defaultSettings.getIntervalResetCounter() + ";" + defaultSettings.getIntervalRevoke() + ";"
                        + defaultSettings.getDefaultTransaktionCount() + ";" + defaultSettings.getDefaultGasUsedCount()
                        + ";"+defaultSettings.getCertifyierAdress()+ ";"+"DEFAULSETTINGS" ;
        return defaultSettingsLine;

    }

    private String prepareAccountLineForFile(Account account) {
        String accountLine;
        accountLine =
                account.getAdressValue() + ";" + account.getMaxTransaktionCounter() + ";" + account.getMaxGasUsed()
                        + ";" + account.getRevokePeriodCounter() + ";" + account.getRevoked();

        return accountLine;
    }
}

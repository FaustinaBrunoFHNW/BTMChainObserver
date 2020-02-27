package ch.brugg.fhnw.btm.loader;

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

    private File file = new File("src/main/resources/whitelist/AccountList.txt");
    private static AccountWriter instance;
    private static Logger log = LoggerFactory.getLogger(AccountWriter.class);

    //TODO

    /**
     * Privater Constructor für Singleton
     */
    private AccountWriter() {
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
     * Methode fürs Schreiben des Files
     *
     * @param accountsBlocked   Liste mit allen Blockierten Accounts
     * @param certifyedAccounts Liste mit allen certifizierten Accounts
     * @param defaultSettings   Objekt mit allen Default Werten
     * @throws IOException
     */
    public void writeInFile(ArrayList<Account> accountsBlocked, ArrayList<Account> certifyedAccounts,
            DefaultSettings defaultSettings) throws IOException {

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        log.info("Alle Account werden in Datei gespeichert.");

        bw.write(this.prepareDefaultSeetingsLineForFile(defaultSettings));

        for (Account account : accountsBlocked) {
            if (account.getRevoked() > 0) {

                bw.write(this.prepareAccountLineForFile(account));
            }
        }

        for (Account account : certifyedAccounts) {
            if (account.getRevoked() > 0) {
                bw.write(this.prepareAccountLineForFile(account));
            }
        }
        bw.close();
        log.info("Daten wurden in datei gespeichert.");
    }

    private String prepareDefaultSeetingsLineForFile(DefaultSettings defaultSettings) {
        String defaultSettingsLine;
        defaultSettingsLine =
                defaultSettings.getIntervalResetCounter() + ";" + defaultSettings.getIntervalRevoke() + ";"
                        + defaultSettings.getDefaultTransaktionCount() + ";" + defaultSettings.getDefaultGasUsedCount();
        return defaultSettingsLine;

    }

    private String prepareAccountLineForFile(Account account) {
        String accountLine;
        accountLine =
                account.getAdressValue() + ";" + account.getMaxTransaktionCounter() + ";" + account.getMaxGasUsed()
                        + ";" + account.getRevokePeriod() + ";" + account.getRevoked();

        return accountLine;
    }
}

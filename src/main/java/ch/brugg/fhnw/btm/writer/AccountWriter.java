package ch.brugg.fhnw.btm.writer;

import ch.brugg.fhnw.btm.loader.AccountLoader;
import ch.brugg.fhnw.btm.loader.DefaultSettingsLoader;
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

        FileWriter fw = new FileWriter(accountFile);
        BufferedWriter bw = new BufferedWriter(fw);

        log.info("Alle Accounts werden in Datei gespeichert.");

        log.info(accountLoader.getRevokedAccountArrayList().size() + " Accounts sind blockiert");
        for (Account account : accountLoader.getRevokedAccountArrayList()) {
            bw.write(this.prepareAccountLineForFile(account));
            log.info(account.getAdressValue());
            bw.newLine();

        }

        log.info(accountLoader.getAccountArrayList().size() + " Accounts sind zertifiziert");
        for (Account account : accountLoader.getAccountArrayList()) {

            bw.write(this.prepareAccountLineForFile(account));
            log.info(account.getAdressValue());
            bw.newLine();

        }
        bw.close();
        log.info("Daten wurden in datei gespeichert.");
    }



    private String prepareAccountLineForFile(Account account) {
        String accountLine;
        if (account.isDefaultSettings()) {
            accountLine =
                    account.getAdressValue() + ";" + account.getRevokePeriodCounter();
        } else {
            accountLine =
                    account.getAdressValue() + ";" + account.getMaxTransaktionCounter() + ";" + account.getMaxGasUsed()
                            + ";" + account.getRevokePeriodCounter() ;
        }
        return accountLine;
    }
}

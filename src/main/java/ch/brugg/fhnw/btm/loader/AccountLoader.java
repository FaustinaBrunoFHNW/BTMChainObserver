package ch.brugg.fhnw.btm.loader;

import ch.brugg.fhnw.btm.pojo.Account;
import ch.brugg.fhnw.btm.pojo.DefaultSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

/**
 * In dieser Singleton Klasse werden die Accounts wie die Default Settings aus dem File geladen
 * und die Listen der Accounts generiert
 */
public class AccountLoader {

    private static AccountLoader instance;

    private ArrayList<Account> accountArrayList;
    private ArrayList<Account> revokedAccountArrayList;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);
    private DefaultSettings defaultSettings;

    private File file = new File("src/main/resources/whitelist/Accounts.txt");

    private AccountLoader() {
        this.accountArrayList = new ArrayList();
        this.revokedAccountArrayList = new ArrayList();
        //TODO wieso bei initieren schon alle Laden? muss geprüft werden
        // loadAccounts();
    }

    /**
     * Instanziert eine Instanz der Klasse falls es noch keine gibt und gibt
     * die existierende oder eben die neue zurück
     *
     * @return die einmalige Instanz der Klasse
     */
    public static AccountLoader getInstance() {

        if (AccountLoader.instance == null) {
            AccountLoader.instance = new AccountLoader();
        }
        return AccountLoader.instance;
    }

    /*
    Läd alle Accounts die im File eingetragen sind, erstellt Accounts und füllt
    diese in eine ArrayList und in eine Hashmap ein
     */
    public void loadAccounts() {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");
                if (accountArray.length == 4) {
                    this.readDefaultValues(accountArray);

                } else if (accountArray.length == 3) {
                    this.readAccountValuesSimple(accountArray);

                } else if (accountArray.length == 1) {
                    this.readAccountValuesWithDefault(accountArray);

                } else if (accountArray.length == 5) {
                    this.readAccountValuesComplex(accountArray);
                }

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * In dieser Methode wird ein DefaulSettings Objekt aus den ausgelesenen Attributen der Datei
     * erstellt.
     *
     * @param fileInput String Array mit den DefaultSettings Attributen
     */
    private void readDefaultValues(String[] fileInput) {
        defaultSettings = new DefaultSettings(fileInput[0], fileInput[1], fileInput[2], fileInput[3]);

        log.info("******************DEFAULT WERTE***************");
        log.info("ResetCounter Intervall wurde auf " + fileInput[0] + " Minuten gesetzt");
        log.info("Revoke Zeit wurde auf " + fileInput[1] + " Intervalle  gesetzt");
        log.info("Default Max Transaktionen wurden auf " + fileInput[2] + " gesetzt");
        log.info("Default Max Gas Used wurde auf " + fileInput[3] + " gesetzt");

    }

    /**
     * In dieser Methode wird ein Account Objekt aus den ausgelesenen Attributen der Datei und Default Werten
     * erstellt und in die Account Liste hinzugefügt.
     *
     * @param fileInput String Array mit 1 Attributen für das Account Objekt
     */
    private void readAccountValuesWithDefault(String[] fileInput) {
        Account account = new Account(fileInput[0], this.defaultSettings.getDefaultTransaktionCount(),
                this.defaultSettings.getDefaultGasUsedCount(), defaultSettings.getIntervalRevoke());
        System.out.println("Folgender Account wurde geladen: " + account.getAdressValue()
                + " Es wurden die Default werde für max Transaktionen und max Gas Used gesetzt ");
        this.accountArrayList.add(account);
    }

    /**
     * In dieser Methode wird ein Account Objekt aus den ausgelesenen Attributen der Datei
     * erstellt und in die Account Liste hinzugefügt.
     *
     * @param fileInput String Array mit 3 Attributen für das Account Objekt
     */
    private void readAccountValuesSimple(String[] fileInput) {
        Account account = new Account(fileInput[0], fileInput[1], fileInput[2], defaultSettings.getIntervalRevoke());
        System.out.println(
                "Folgender Account wurde geladen: " + account.getAdressValue() + " Mit Max Transaktionen: " + account
                        .getMaxTransaktionCounter() + " und max GasUsed:" + account.getMaxGasUsed());
        this.accountArrayList.add(account);
    }

    /**
     * In dieser Methode wird ein Account Objekt aus den ausgelesenen Attributen der Datei
     * erstellt. Hier wird der RevokePeriodCounter und RevokedTimes gesetzt
     * Ist der RevokePeriodCounter kleiner als RevokePeriod, ist der Account gesperrt und er
     * wird in der Liste der gesperrten Accounts hinzugefügt
     * Ansonsten wird er in die Liste der Zertifizierten Accounts hinzugefügt
     *
     * @param fileInput String Array mit 5 Attributen für das Account Objekt
     */
    private void readAccountValuesComplex(String[] fileInput) {
        Account account = new Account(fileInput[0], fileInput[1], fileInput[2], defaultSettings.getIntervalRevoke(),
                fileInput[3], fileInput[4]);
        System.out.println("Folgender Account wurde geladen: " + account.getAdressValue()
                + " Es wurden die Default werde für max Transaktionen und max Gas Used gesetzt ");
        if (account.getRevokePeriodCounter() == account.getRevokePeriod()) {
            this.accountArrayList.add(account);
        } else {
            this.revokedAccountArrayList.add(account);
        }

    }

    //TODO Explain java DOC // für Testzwecke
    public void addAccountToList(String address, String maxTransaktionen, String maxGasUsed, int revokePeriod) {
        Account account = new Account(address, maxTransaktionen, maxGasUsed, revokePeriod);
        accountArrayList.add(account);
    }

    //TODO Explain java DOC
    private void appendAccount(String address) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.newLine();
            output.append(address.trim().toLowerCase());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO Explain java DOC
    /*
    Läd alle Accounts wieder in die ArrayList und Hashmap
     */
    public void reset() {
        loadAccounts();
    }

    //TODO Explain java DOC

    /**
     * @param address
     * @return
     */
    public boolean removeAccount(String address) {
        File temp = new File("src/main/resources/accountInput/Accounts_temp.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp));

            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {
                if (currentLine.trim().toLowerCase().equals(address.trim().toLowerCase())) {
                    continue;
                }
                bufferedWriter.write(currentLine);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            bufferedReader.close();
            file.delete();
            if (temp.renameTo(file)) {
                loadAccounts();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //*******************GETTER und SETTER

    public ArrayList<Account> getAccountArrayList() {
        return accountArrayList;
    }

    public void setAccountArrayList(ArrayList<Account> accountArrayList) {
        this.accountArrayList = accountArrayList;
    }

    public DefaultSettings getDefaultSettings() {
        return defaultSettings;
    }

    //TODO rausnhemen falls nicht gebraucht
    private void setDefaultSettings(DefaultSettings defaultSettings) {
        this.defaultSettings = defaultSettings;
    }

    public ArrayList<Account> getRevokedAccountArrayList() {
        return revokedAccountArrayList;
    }

    public void setRevokedAccountArrayList(ArrayList<Account> revokedAccountArrayList) {
        this.revokedAccountArrayList = revokedAccountArrayList;
    }
}

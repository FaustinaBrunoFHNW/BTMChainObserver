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
    private ArrayList<Account> deleteAccountList;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);
    private DefaultSettings defaultSettings;
    private String certifierAdd;

    private File accountsFile = new File("src/main/resources/whitelist/AccountList.txt");
    private File transaktionManagerAccountFile = new File("src/main/resources/whitelist/TransaktionManagerAccount.txt");
    private File defaultSettingsFile = new File("src/main/resources/whitelist/DefaultSettings.txt");

    private AccountLoader() {
        this.accountArrayList = new ArrayList();
        this.revokedAccountArrayList = new ArrayList();
        this.deleteAccountList = new ArrayList<>();
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

    /**
     * Diese Methode startet alle Loader Methoden in der richtigen Rheinfolge
     */
    public void loadAll() {
        this.loadDefaultSettings();
        this.loadAccounts();
        //   this.loadTransaktionManagerAccount();
    }

    private void loadTransaktionManagerAccount() {
        log.info("Laden der Transaktion Manager Account");
        try {
            FileReader fileReader = new FileReader(transaktionManagerAccountFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");
                if (accountArray.length == 3) {
                    this.readAccountValuesSimple(accountArray);

                } else if (accountArray.length == 1) {
                    this.readAccountValuesWithDefault(accountArray);

                }
                //TODO ausbauen
                /** else if (accountArray.length == 5) {
                 this.readAccountValuesComplex(accountArray);
                 }
                 */

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultSettings() {
        log.info("Laden der Default Settings");
        try {
            FileReader fileReader = new FileReader(defaultSettingsFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");

                if (accountArray.length == 5) {
                    this.readDefaultValues(accountArray);

                } else if (accountArray.length == 6) {
                    this.readDefaultValuesWithCerfierAdd(accountArray);
                }

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Läd alle Accounts die im File eingetragen sind, erstellt Accounts und füllt
    diese in eine ArrayList und in eine Hashmap ein
     */
    public void loadAccounts() {
        try {
            FileReader fileReader = new FileReader(accountsFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");
                if (accountArray.length == 3) {
                    this.readAccountValuesSimple(accountArray);

                } else if (accountArray.length == 1) {
                    this.readAccountValuesWithDefault(accountArray);

                } else if (accountArray.length == 5) {
                    this.readAccountValuesComplex(accountArray);
                } else if (accountArray.length == 2 || accountArray.length == 4 || accountArray.length == 6) {
                    this.readDeletingAccounts(accountArray);
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
        defaultSettings = new DefaultSettings(fileInput[0], fileInput[1], fileInput[2], fileInput[3], fileInput[4]);

        log.info("******************DEFAULT WERTE***************");
        log.info("Connection Adresse " + fileInput[0]);
        log.info("ResetCounter Intervall wurde auf " + fileInput[1] + " Minuten gesetzt");
        log.info("Revoke Zeit wurde auf " + fileInput[2] + " Intervalle  gesetzt");
        log.info("Default Max Transaktionen wurden auf " + fileInput[3] + " gesetzt");
        log.info("Default Max Gas Used wurde auf " + fileInput[4] + " gesetzt");

    }

    /**
     * In dieser Methode wird ein DefaultSettings Objekt aus den ausgelesenen Attributen der Datei
     * erstellt. Hier ist die CertifierAdd dabei
     *
     * @param fileInput String Array mit den DefaultSettings Attributen
     */
    private void readDefaultValuesWithCerfierAdd(String[] fileInput) {
        defaultSettings = new DefaultSettings(fileInput[0], fileInput[1], fileInput[2], fileInput[3], fileInput[4]);
        if (fileInput.length > 5) {
            defaultSettings.setCertifyierAdress(fileInput[5]);
        }

        log.info("******************DEFAULT WERTE***************");
        log.info("Connection Adresse " + fileInput[0]);
        log.info("ResetCounter Intervall wurde auf " + fileInput[1] + " Minuten gesetzt");
        log.info("Revoke Zeit wurde auf " + fileInput[2] + " Intervalle  gesetzt");
        log.info("Default Max Transaktionen wurden auf " + fileInput[3] + " gesetzt");
        log.info("Default Max Gas Used wurde auf " + fileInput[4] + " gesetzt");

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
        account.setDefaultSettings(true);
        log.info("Folgender Account wurde geladen: " + account.getAdressValue()
                + " Es wurden die Default werde für max Transaktionen und max Gas Used gesetzt ");
        this.addAccountToZertifiedList(account);
    }

    /**
     * In dieser Methode wird ein Account Objekt aus den ausgelesenen Attributen der Datei
     * erstellt und in die Account Liste hinzugefügt.
     *
     * @param fileInput String Array mit 3 Attributen für das Account Objekt
     */
    private void readAccountValuesSimple(String[] fileInput) {
        Account account = new Account(fileInput[0], fileInput[1], fileInput[2], defaultSettings.getIntervalRevoke());
        account.setDefaultSettings(false);
        log.info("Folgender Account wurde geladen: " + account.getAdressValue() + " Mit Max Transaktionen: " + account
                .getMaxTransaktionCounter() + " und max GasUsed:" + account.getMaxGasUsed());
        this.addAccountToZertifiedList(account);
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
        log.info("Folgender Account wurde geladen: " + account.getAdressValue()
                + " Es wurden die Default werde für max Transaktionen und max Gas Used gesetzt ");
        if (account.getRevokePeriodCounter() == account.getRevokePeriod()) {
            this.addAccountToZertifiedList(account);
        } else {
            this.addAccountToRevokedList(account);
        }

    }

    //TODO JAVADOC
    private void readDeletingAccounts(String[] fileInput) {
        if(fileInput[0].equals("d")) {
            Account account = new Account(fileInput[1]);
            log.info("Folgender Account wird zur Löschung geladen: " + account.getAdressValue());
            this.deleteAccountList.add(account);
        }
      else{
            log.warn("Etwas stimmt im AccountFile nicht mit den zu löschenden Accounts");
        }

    }

    /**
     * Diese Methode Prüft ob der Account schon in der radressListe geführt wird oder nicht.
     * Wenn nicht, wird er in die AdressListe hinzugefügt
     *
     * @param account Objekt welches in die Liste hinzugefügt werden soll
     */
    public void addAccountToZertifiedList(Account account) {
        int counter = 0;
        for (Account existingAccount : accountArrayList) {
            if (existingAccount.getAdressValue().equals(account.getAdressValue())) {
                counter++;
            }
        }
        if (counter == 0) {
            accountArrayList.add(account);
        } else {
            log.info(account.getAdressValue() + " ist doppelt in Liste geführt");
        }

    }

    /**
     * Diese Methode Prüft ob der Account schon in der revokedListe geführt wird oder nicht.
     * Wenn nicht, wird er in die RevokedListe hinzugefügt
     *
     * @param account Objekt welches in die Liste hinzugefügt werden soll
     */
    public void addAccountToRevokedList(Account account) {
        int counter = 0;
        for (Account existingAccount : accountArrayList) {
            if (existingAccount.getAdressValue().equals(account.getAdressValue())) {
                counter++;
            }
        }
        if (counter == 0) {
            revokedAccountArrayList.add(account);
        } else {
            log.info(account.getAdressValue() + " ist doppelt in Liste geführt");
        }

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

    public void setCertifierAddress(String certfierAdd) {
        this.defaultSettings.setCertifyierAdress(certfierAdd);
    }

    public ArrayList<Account> getDeleteAccountList() {
        return deleteAccountList;
    }

    public void setDeleteAccountList(ArrayList<Account> deleteAccountList) {
        this.deleteAccountList = deleteAccountList;
    }
}

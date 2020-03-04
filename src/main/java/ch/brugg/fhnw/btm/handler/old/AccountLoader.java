package ch.brugg.fhnw.btm.handler.old;

import ch.brugg.fhnw.btm.pojo.Account;
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
    private DefaultSettingsLoader defaultSettingsLoader;

    private ArrayList<Account> accountArrayList;
    private ArrayList<Account> revokedAccountArrayList;
    private ArrayList<Account> deleteAccountList;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);

    private File accountsFile = new File("src/main/resources/whitelist/AccountList.txt");
    private File transaktionManagerAccountFile = new File("src/main/resources/whitelist/TransaktionManagerAccount.txt");

    private AccountLoader() {
        this.defaultSettingsLoader = DefaultSettingsLoader.getInstance();
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
        this.loadAccounts();
        //   this.loadTransaktionManagerAccount();
    }

    private void loadTransaktionManagerAccount() {
        log.info("Laden der Transaktion Manager Account");
        try {
            FileReader fileReader = new FileReader(this.transaktionManagerAccountFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");
                if (accountArray.length == 3) {
                    this.readAccountValuesWithDefaultValues(accountArray);

                } else if (accountArray.length == 1) {
                    this.readAccountStartValuesWithDefault(accountArray);

                }
                //TODO ausbauen
                /** else if (accountArray.length == 5) {
                 this.readAccountStartValuesNoDefaults(accountArray);
                 }
                 */

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
            FileReader fileReader = new FileReader(this.accountsFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");

                if (accountArray.length == 1) {
                    this.readAccountStartValuesWithDefault(accountArray);
                } else if (accountArray.length == 2) {
                    this.readAccountValuesWithDefaultValues(accountArray);
                } else if (accountArray.length == 3) {
                    this.readAccountStartValuesNoDefaults(accountArray);
                } else if (accountArray.length == 4) {
                    this.readAccountValuesNoDefaults(accountArray);
                } else if (accountArray.length > 4) {
                    this.readDeletingAccounts(accountArray);
                }

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO JAVADOC
    private void readAccountStartValuesWithDefault(String[] fileInput) {
        Account account = new Account(fileInput[0],
                this.defaultSettingsLoader.getDefaultSettings().getDefaultTransaktionCount(),
                this.defaultSettingsLoader.getDefaultSettings().getDefaultGasUsedCount(),
                this.defaultSettingsLoader.getDefaultSettings().getRevokeMultiplier());

        log.info("Folgender Account wurde geladen: " + account.getAddress()
                + " Es wurden die Default werde für max Transaktionen, max Gas Used und Revoke Period gesetzt ");
        if (account.getRevokeTime() == defaultSettingsLoader.getDefaultSettings().getRevokeMultiplier()) {
            this.addAccountToZertifiedList(account);
        } else {
            this.addAccountToRevokedList(account);
        }
    }

    //TODO JAVADOC
    private void readAccountValuesWithDefaultValues(String[] fileInput) {
        if (fileInput[0].equals("d")) {
            Account account = new Account(fileInput[1]);
            log.info("Folgender Account wird zur Löschung geladen: " + account.getAddress());
            this.deleteAccountList.add(account);
        } else {
            Account account = new Account(fileInput[0],
                    this.defaultSettingsLoader.getDefaultSettings().getDefaultTransaktionCount(),
                    this.defaultSettingsLoader.getDefaultSettings().getDefaultGasUsedCount(),
                    Integer.parseInt(fileInput[1]));

            log.info("Folgender Account wurde geladen: " + account.getAddress()
                    + " Es wurden die Default werde für max Transaktionen, max Gas Used gesetzt");
            if (account.getRevokeTime() == defaultSettingsLoader.getDefaultSettings().getRevokeMultiplier()) {
                this.addAccountToZertifiedList(account);
            } else {
                this.addAccountToRevokedList(account);
            }
        }
    }

    //TODO JAVADOC
    private void readAccountStartValuesNoDefaults(String[] fileInput) {
        if (fileInput[0].equals("d")) {
            Account account = new Account(fileInput[1]);
            log.info("Folgender Account wird zur Löschung geladen: " + account.getAddress());
            this.deleteAccountList.add(account);
        } else {
            Account account = new Account(fileInput[0], fileInput[1], fileInput[2],
                    this.defaultSettingsLoader.getDefaultSettings().getRevokeMultiplier());
            log.info("Folgender Account wurde geladen: " + account.getAddress()
                    + " Es wurden die Default werde für Revoke Period gesetzt");

            if (account.getRevokeTime() == defaultSettingsLoader.getDefaultSettings().getRevokeMultiplier()) {
                this.addAccountToZertifiedList(account);
            } else {
                this.addAccountToRevokedList(account);
            }
        }

    }

    //TODO JAVADOC
    private void readAccountValuesNoDefaults(String[] fileInput) {
        if (fileInput[0].equals("d")) {
            Account account = new Account(fileInput[1]);
            log.info("Folgender Account wird zur Löschung geladen: " + account.getAddress());
            this.deleteAccountList.add(account);
        } else {
            Account account = new Account(fileInput[0], fileInput[1], fileInput[2], fileInput[3]);
            log.info("Folgender Account wurde geladen: " + account.getAddress());

            if (account.getRevokeTime() == defaultSettingsLoader.getDefaultSettings().getRevokeMultiplier()) {
                this.addAccountToZertifiedList(account);
            } else {
                this.addAccountToRevokedList(account);
            }
        }
    }

    //TODO JAVADOC
    private void readDeletingAccounts(String[] fileInput) {
        if (fileInput[0].equals("d")) {
            Account account = new Account(fileInput[1]);
            log.info("Folgender Account wird zur Löschung geladen: " + account.getAddress());
            this.deleteAccountList.add(account);
        } else {
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
        for (Account existingAccount : this.accountArrayList) {
            if (existingAccount.getAddress().equals(account.getAddress())) {
                counter++;
            }
        }
        if (counter == 0) {
            this.accountArrayList.add(account);
        } else {
            log.info(account.getAddress() + " ist doppelt in Liste geführt");
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
        for (Account existingAccount : this.accountArrayList) {
            if (existingAccount.getAddress().equals(account.getAddress())) {
                counter++;
            }
        }
        if (counter == 0) {
            this.revokedAccountArrayList.add(account);
        } else {
            log.info(account.getAddress() + " ist doppelt in Liste geführt");
        }

    }

    //*******************GETTER und SETTER

    public ArrayList<Account> getAccountArrayList() {
        return this.accountArrayList;
    }

    public void setAccountArrayList(ArrayList<Account> accountArrayList) {
        this.accountArrayList = accountArrayList;
    }

    public ArrayList<Account> getRevokedAccountArrayList() {
        return this.revokedAccountArrayList;
    }

    public void setRevokedAccountArrayList(ArrayList<Account> revokedAccountArrayList) {
        this.revokedAccountArrayList = revokedAccountArrayList;
    }

    public ArrayList<Account> getDeleteAccountList() {
        return this.deleteAccountList;
    }

    public void setDeleteAccountList(ArrayList<Account> deleteAccountList) {
        this.deleteAccountList = deleteAccountList;
    }
}

package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.pojo.Account;
import ch.brugg.fhnw.btm.pojo.DefaultSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Address;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

//TODO Klasse und Methode in Kommentaren beschreiben
public class AccountLoader {

    private ArrayList<Account> accountArrayList;
    private ArrayList<Account> revokedAccountArrayList;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);
    private DefaultSettings defaultSettings;

    private File file = new File("src/main/resources/whitelist/Accounts.txt");

    private static final AccountLoader instance = new AccountLoader();

    public AccountLoader() {
        //   accounts = new HashMap<>();
        accountArrayList = new ArrayList();
        //TODO wieso bei initieren schon alle Laden? muss geprüft werden
        // loadAccounts();
    }

    //TODO für was brauch man das? Lieber Singleton Pattern anwenden.
    public static AccountLoader getInstance() {
        return instance;
    }

    /*
    Läd alle Accounts die im File eingetragen sind, erstellt Accounts und füllt
    diese in eine ArrayList und in eine Hashmap ein
     */
    public void loadAccounts() {

        //   accounts = new HashMap<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountArray = line.split(";");
                if (accountArray.length == 4) {
                    defaultSettings = new DefaultSettings(accountArray[0], accountArray[1], accountArray[2],
                            accountArray[3]);
                    System.out.println("******************DEFAULT WERTE***************");
                    System.out.println("ResetCounter Intervall wurde auf " + accountArray[0] + " Minuten gesetzt");
                    System.out.println("Revoke Zeit wurde auf " + accountArray[1] + " Intervalle  gesetzt");
                    System.out.println("Default Max Transaktionen wurden auf " + accountArray[2] + " gesetzt");
                    System.out.println("Default Max Gas Used wurde auf " + accountArray[3] + " gesetzt");
                }
                if (accountArray.length == 3) {

                    Account account = new Account(accountArray[0], accountArray[1], accountArray[2],defaultSettings.getIntervalRevoke());
                    System.out.println(
                            "Folgender Account wurde geladen: " + account.getAdressValue() + " Mit Max Transaktionen: "
                                    + account.getMaxTransaktionCounter() + " und max GasUsed:" + account
                                    .getMaxGasUsed());
                    this.accountArrayList.add(account);
                }
                if (accountArray.length == 1) {
                    Account account = new Account(accountArray[0], this.defaultSettings.getDefaultTransaktionCount(),
                            this.defaultSettings.getDefaultGasUsedCount(),defaultSettings.getIntervalRevoke());
                    System.out.println("Folgender Account wurde geladen: " + account.getAdressValue()
                            + " Es wurden die Default werde für max Transaktionen und max Gas Used gesetzt ");
                    this.accountArrayList.add(account);
                }

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO Explain java DOC // für Testzwecke
    public void addAccountToList(String address, String maxTransaktionen, String maxGasUsed, int revokePeriod) {
        Account account = new Account(address, maxTransaktionen, maxGasUsed,revokePeriod);
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

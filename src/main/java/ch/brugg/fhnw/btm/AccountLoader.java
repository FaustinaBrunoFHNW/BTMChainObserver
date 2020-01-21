package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Address;

import java.io.*;
import java.util.HashMap;


//TODO Klasse und Methode in Kommentaren beschreiben
public class AccountLoader {

    private HashMap<String, Account> accounts;
    private static Logger log = LoggerFactory.getLogger(AccountLoader.class);

    File file = new File("src/main/resources/whitelist/Accounts.txt");

    private static final AccountLoader instance = new AccountLoader();

    private AccountLoader(){
        loadAccounts();
    }

    public static AccountLoader getInstance() {
        return  instance;
    }


    private void loadAccounts(){
        Account temp;
        accounts = new HashMap<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                temp = new Account(line);
                accounts.put(line,temp);
            }
            bufferedReader.close();
            fileReader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Address address) {
        if (!accounts.containsKey(address.getValue())) {
            appendAccount(address.getValue().toLowerCase());
            loadAccounts();
        }

    }

    public void addAccount(String address) {
        if (! accounts.containsKey(address.trim().toLowerCase())) {
            appendAccount(address);
            loadAccounts();
        }
    }

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

    public void reset() {
        loadAccounts();
    }



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
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public  boolean increaseCounter(String address){
        log.info("increasing counter of " + address);
        if (accounts.containsKey(address)){
            log.info("account found");
            accounts.get(address).increaseCounter();
            return true;
        }
        log.info("unable to find account " + address);
        return false;
    }
}

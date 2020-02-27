package ch.brugg.fhnw.btm.loader;

import ch.brugg.fhnw.btm.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class BlockedCounterLoader {
    private File file = new File("src/main/resources/whitelist/BlockedCounterList.txt");
    private static Logger log = LoggerFactory.getLogger(BlockedCounterLoader.class);

    //TODO JAVADOC
    /*
     */
    public void loadBlockedInfo(ArrayList<Account> accountList) {

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] accountBlockedInfoArray = line.split(";");

                for (Account account : accountList) {

                    if (account.getAdressValue().equals(accountBlockedInfoArray[0])) {
                        account.setRevoked(Integer.parseInt(accountBlockedInfoArray[1]));
                    }
                }

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO JAVADOC

    /**
     * Diese Methode geht durch die Liste der Account und schreibt bei Accounts die mind 1 mal geblockt wurden,
     * wie oft die geblockt wurden in ein separates File
     *
     * @param accountsBlocked   Liste mit den momentan geblockten Accounts
     * @param certifyedAccounts Liste mit momentan certifyed Accounts
     * @throws IOException
     */
    public void writeInFile(ArrayList<Account> accountsBlocked, ArrayList<Account> certifyedAccounts)
            throws IOException {

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        this.log.info("Anzahl Blockierungen wird in File gespeichert.");

        for (Account account : accountsBlocked) {
            if (account.getRevoked() > 0) {
                bw.write(account.getAdressValue() + ";" + account.getRevoked());

            }
        }

        for (Account account : certifyedAccounts) {
            if (account.getRevoked() > 0) {
                bw.write(account.getAdressValue() + ";" + account.getRevoked());
            }
        }
        bw.close();
    }
}

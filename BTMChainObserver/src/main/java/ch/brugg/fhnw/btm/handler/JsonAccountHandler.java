package ch.brugg.fhnw.btm.handler;

import ch.brugg.fhnw.btm.pojo.JsonAccount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * In dieser Klasse werden Accounts aus dem JSON File geladen und wieder reingeschrieben
 *
 * @Author Faustina Bruno, Serge Jurij Maikoff
 */
public class JsonAccountHandler {

    private static JsonAccountHandler instance;
    private ArrayList<JsonAccount> jsonAccountList = new ArrayList<>();
    private ArrayList<JsonAccount> jsonAccountsToDelete= new ArrayList<>();
    private Logger log = LoggerFactory.getLogger(JsonAccountHandler.class);
    private int deletedAccounts = 0, revokedAccounts = 0;

    //    private String accountsFile = "src/main/resources/whitelist/AccountList.json";
    private String accountsFile = "./AccountList.json";

    /**
     * Getter zum einamlige Klassen Instanz zo holen
     *
     * @return JsonAccont Objekt
     */
    public static JsonAccountHandler getInstance() {

        if (JsonAccountHandler.instance == null) {
            JsonAccountHandler.instance = new JsonAccountHandler();
        }
        return JsonAccountHandler.instance;
    }

    /**
     * privater Constructor für Singleton
     */
    private JsonAccountHandler() {

    }

    /**
     * Diese Methode holt alle Accounts aus der Account Datei und speichert sie als JsonAccount Objekte in einer Liste ab
     */
    public void loadAccounts() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(this.accountsFile));
            Type accountListType = new TypeToken<ArrayList<JsonAccount>>() {
            }.getType();
            this.jsonAccountList = gson.fromJson(reader, accountListType);
            this.log.info("Accounts wurden aus Datei geladen. Es sind " + this.jsonAccountList.size()
                    + " Accounts geladen worden. ");

            Iterator<JsonAccount> itrAccount = this.jsonAccountList.iterator();
            while (itrAccount.hasNext()) {
                JsonAccount accountToDelete = itrAccount.next();
                if (accountToDelete.deleteMe) {
                    this.log.info(
                            "Account zum Löschen wurde gefunden. Folgender Account wird gelöscht: " + accountToDelete
                                    .getAddress());
                    this.deletedAccounts++;
                    this.jsonAccountsToDelete.add(accountToDelete);
                    itrAccount.remove();
                }

            }
        } catch (FileNotFoundException e) {
            this.log.error("Es gab Probleme beim Laden der Accounts aud der Daei");
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode schreibt alle Accounts in die Datei
     */
    public void writeAccountList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(this.accountsFile));
            gson.toJson(this.jsonAccountList, writer);
            writer.close();
            this.log.info("Accounts wurden in die Datei geschrieben");
        } catch (IOException e) {
            this.log.error("Es ist Fehler aufgetreten beim Schreiben der Accounts in die Datei");
            e.printStackTrace();

        }
    }

    /**
     * Diese Methode itiriert durch alle Accounts und gibt den Account mit der übergebenen Adresse zurück
     *
     * @param address Adresse des gewünschten Accounts
     * @return Account passent zur Adresse
     */
    public JsonAccount getAccount(String address) {
        for (JsonAccount account : this.jsonAccountList) {
            if (account.getAddress().equalsIgnoreCase(address)) {
                return account;
            }
        }
        return null;
    }

    /**
     * in dieser Methode wird vom Account der zur übergebenen Adresse passt,
     * der Transaktionscounter um eins runter gezählt und
     * das übergebene Gas vom Gas Counter abgezogen.
     * Am Ende wird der Gas und Transaktions Stand des Accounts ausgegeben.
     *
     * @param address Adresse des Accounts
     * @param gasUsed eine Gas Value
     * @return ein JsonAccount Objekt das zur übergebenen Adresse passt und aktuelisiert ist
     */
    public JsonAccount processAccount(String address, BigInteger gasUsed) {
        JsonAccount accountToProcess = this.getAccount(address);
        if (accountToProcess != null) {
            accountToProcess.decraseTransactionCounter();
            accountToProcess.decreaseGasUsedCounter(gasUsed.longValue());

            this.log.info("Account: " + accountToProcess.getAddress() + " hat noch " + accountToProcess.getRemainingTransactions()
                    + " Transaktionen auf dem Counter und noch so viel Gas zum verbauchen " + accountToProcess
                    .getRemainingGas());
        }
            return accountToProcess;

    }

    //******************GETTER und SETTER*******************************

    public ArrayList<JsonAccount> getJsonAccountList() {
        return this.jsonAccountList;
    }

    public void setJsonAccountList(ArrayList<JsonAccount> jsonAccountList) {
        this.jsonAccountList = jsonAccountList;
    }

    public int getDeletedAccounts() {
        return this.deletedAccounts;
    }

    public int getRevokedAccounts() {
        return this.revokedAccounts;
    }

    public ArrayList<JsonAccount> getJsonAccountsToDelete() {
        return jsonAccountsToDelete;
    }

    public void setJsonAccountsToDelete(ArrayList<JsonAccount> jsonAccountsToDelete) {
        this.jsonAccountsToDelete = jsonAccountsToDelete;
    }
}

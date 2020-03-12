package ch.brugg.fhnw.btm.pojo;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Default Settings Klasse um die Default Settings der Chain aus einem File in ein Objekt zu speichern
 * @Author Faustina Bruno, Serge Jurij Maikoff
 */
public class JsonDefaultSettings {

    private static JsonDefaultSettings instance;
    private JsonDefaultSettings(){};
    private String connectionAddress;
    private int resetInterval;
    private BigInteger defaultTxLimit;
    private BigInteger defaultGasLimit;
    private String certifierAddress;
    private Timestamp timestampLastReset;
    private String nameRegistryAddress;
    private  BigInteger defaultRevokeTime;



    private String masterKeyAddress;

    /**
     * Getter um Singleton Instanz der Klasse zu holen
     * @return instanzierte Instanz der Klasse
     */
    public static JsonDefaultSettings getInstance() {

        if (JsonDefaultSettings.instance == null) {
            JsonDefaultSettings.instance = new JsonDefaultSettings();
        }
        return JsonDefaultSettings.instance;
    }



//**************************GETTER und SETTER*********************************


    public String getConnectionAddress() {
        return this.connectionAddress;
    }

    public int getResetInterval() {
        return this.resetInterval;
    }


    public BigInteger getDefaultTxLimit() {
        return this.defaultTxLimit;
    }

    public BigInteger getDefaultGasLimit() {
        return this.defaultGasLimit;
    }

    public String getCertifierAddress() {
        return this.certifierAddress;
    }

    public Timestamp getTimestampLastReset() {
        return this.timestampLastReset;
    }

    public String getNameRegistryAddress() {
        return this.nameRegistryAddress;
    }

    public void setCertifierAddress(String certifierAddress) {
        this.certifierAddress = certifierAddress;
    }

    public void setTimestampLastReset(Timestamp timestampLastReset) {
        this.timestampLastReset = timestampLastReset;
    }

    public BigInteger getDefaultRevokeTime() {
        return this.defaultRevokeTime;
    }

    public void setConnectionAddress(String connectionAddress) {
        this.connectionAddress = connectionAddress;
    }

    public void setResetInterval(int resetInterval) {
        this.resetInterval = resetInterval;
    }

    public void setDefaultTxLimit(BigInteger defaultTxLimit) {
        this.defaultTxLimit = defaultTxLimit;
    }

    public void setDefaultGasLimit(BigInteger defaultGasLimit) {
        this.defaultGasLimit = defaultGasLimit;
    }

    public void setNameRegistryAddress(String nameRegistryAddress) {
        this.nameRegistryAddress = nameRegistryAddress;
    }

    public void setDefaultRevokeTime(BigInteger defaultRevokeTime) {
        this.defaultRevokeTime = defaultRevokeTime;
    }
    public String getMasterKeyAddress() {
        return masterKeyAddress;
    }

    public void setMasterKeyAddress(String masterKeyAddress) {
        this.masterKeyAddress = masterKeyAddress;
    }
}

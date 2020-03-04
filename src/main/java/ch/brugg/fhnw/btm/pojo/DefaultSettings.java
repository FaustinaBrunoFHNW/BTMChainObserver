package ch.brugg.fhnw.btm.pojo;

public class DefaultSettings {
    //TODO impl
    //intervalResetCounter in min

    private String connectionAddress;

    private int resetIntervall;
    //intervalRevoke wieviel resetInervalle Account gesperrt ist
    private int revokeMultiplier;
    //defaultTransaktionCount
    private String defaultTransaktionCount;
    //defaultGasCount
    private String defaultGasUsedCount;

    private String certifyierAddress;

    private long timestampLastReset;
    private String registerAddress;

    public DefaultSettings(){}

    public DefaultSettings(String connectionAddress, String resetIntervall, String revokeMultiplier, String defaultTransaktionCount,
                           String defaultGasUsedCount, String registerAddress) {
        this.connectionAddress=connectionAddress;
        this.resetIntervall = Integer.parseInt(resetIntervall);
        this.revokeMultiplier = Integer.parseInt(revokeMultiplier);
        this.defaultTransaktionCount =defaultTransaktionCount;
        this.defaultGasUsedCount = defaultGasUsedCount;
        this.registerAddress=registerAddress;
    }

    public int getResetIntervall() {
        return resetIntervall;
    }

    public void setResetIntervall(int resetIntervall) {
        this.resetIntervall = resetIntervall;
    }

    public int getRevokeMultiplier() {
        return revokeMultiplier;
    }

    public void setRevokeMultiplier(int revokeMultiplier) {
        this.revokeMultiplier = revokeMultiplier;
    }

    public String getDefaultTransaktionCount() {
        return defaultTransaktionCount;
    }

    public void setDefaultTransaktionCount(String defaultTransaktionCount) {
        this.defaultTransaktionCount = defaultTransaktionCount;
    }

    public String getDefaultGasUsedCount() {
        return defaultGasUsedCount;
    }

    public void setDefaultGasUsedCount(String defaultGasUsedCount) {
        this.defaultGasUsedCount = defaultGasUsedCount;
    }

    public String getCertifyierAddress() {
        return certifyierAddress;
    }

    public void setCertifyierAddress(String certifyierAddress) {
        this.certifyierAddress = certifyierAddress;
    }

    public String getConnectionAddress() {
        return connectionAddress;
    }

    public void setConnectionAddress(String connectionAddress) {
        this.connectionAddress = connectionAddress;
    }

    public long getTimestampLastReset() {
        return timestampLastReset;
    }

    public void setTimestampLastReset(long timestampLastReset) {
        this.timestampLastReset = timestampLastReset;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }
}

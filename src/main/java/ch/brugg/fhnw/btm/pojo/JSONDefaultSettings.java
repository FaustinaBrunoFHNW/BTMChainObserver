package ch.brugg.fhnw.btm.pojo;

public class JSONDefaultSettings {

    private String connectionAddress;

    private int resetIntervall;
    //intervalRevoke wieviel resetInervalle Account gesperrt ist
    private int revokeMultiplier;
    //defaultTransaktionCount
    private int defaultTransaktionCount;
    //defaultGasCount
    private long defaultGasUsedCount;

    private String certifyierAddress;

    private String timestampLastReset;
    private String registerAddress;

    public JSONDefaultSettings(){}

    public String getConnectionAddress() {
        return connectionAddress;
    }

    public int getResetIntervall() {
        return resetIntervall;
    }

    public int getRevokeMultiplier() {
        return revokeMultiplier;
    }

    public int getDefaultTransaktionCount() {
        return defaultTransaktionCount;
    }

    public Long getDefaultGasUsedCount() {
        return defaultGasUsedCount;
    }

    public String getCertifyierAddress() {
        return certifyierAddress;
    }

    public String getTimestampLastReset() {
        return timestampLastReset;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setCertifyierAddress(String certifyierAddress) {
        this.certifyierAddress = certifyierAddress;
    }

    public void setTimestampLastReset(String timestampLastReset) {
        this.timestampLastReset = timestampLastReset;
    }
}

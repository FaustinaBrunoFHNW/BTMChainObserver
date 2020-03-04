package ch.brugg.fhnw.btm.pojo;

public class DefaultSettings {
    //TODO impl
    //intervalResetCounter in min

    private String connectionAddress;

    private int intervalResetCounter;
    //intervalRevoke wieviel resetInervalle Account gesperrt ist
    private int intervalRevoke;
    //defaultTransaktionCount
    private String defaultTransaktionCount;
    //defaultGasCount
    private String defaultGasUsedCount;

    private String certifyierAddress;

    private long timestampLastReset;
    private String registerAddress;

    public DefaultSettings(String connectionAddress,String intervalResetCounter, String intervalRevoke, String defaultTransaktionCount,
            String defaultGasUsedCount,String registerAddress) {
        this.connectionAddress=connectionAddress;
        this.intervalResetCounter = Integer.parseInt(intervalResetCounter);
        this.intervalRevoke = Integer.parseInt(intervalRevoke);
        this.defaultTransaktionCount =defaultTransaktionCount;
        this.defaultGasUsedCount = defaultGasUsedCount;
        this.registerAddress=registerAddress;
    }

    public int getIntervalResetCounter() {
        return intervalResetCounter;
    }

    public void setIntervalResetCounter(int intervalResetCounter) {
        this.intervalResetCounter = intervalResetCounter;
    }

    public int getIntervalRevoke() {
        return intervalRevoke;
    }

    public void setIntervalRevoke(int intervalRevoke) {
        this.intervalRevoke = intervalRevoke;
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

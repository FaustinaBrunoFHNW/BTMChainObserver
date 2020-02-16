package ch.brugg.fhnw.btm.pojo;

public class DefaultSettings {
    //TODO impl
    //intervalResetCounter in min

    private int intervalResetCounter;
    //intervalRevoke wieviel resetInervalle Account gesperrt ist
    private int intervalRevoke;
    //defaultTransaktionCount
    private int defaultTransaktionCount;
    //defaultGasCount
    private int defaultGasUsedCount;

    public DefaultSettings(int intervalResetCounter, int intervalRevoke, int defaultTransaktionCount,
            int defaultGasUsedCount) {
        this.intervalResetCounter = intervalResetCounter;
        this.intervalRevoke = intervalRevoke;
        this.defaultTransaktionCount = defaultTransaktionCount;
        this.defaultGasUsedCount = defaultGasUsedCount;
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

    public int getDefaultTransaktionCount() {
        return defaultTransaktionCount;
    }

    public void setDefaultTransaktionCount(int defaultTransaktionCount) {
        this.defaultTransaktionCount = defaultTransaktionCount;
    }

    public int getDefaultGasUsedCount() {
        return defaultGasUsedCount;
    }

    public void setDefaultGasUsedCount(int defaultGasUsedCount) {
        this.defaultGasUsedCount = defaultGasUsedCount;
    }
}

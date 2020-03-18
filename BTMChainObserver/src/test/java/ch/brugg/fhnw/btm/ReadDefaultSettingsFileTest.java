package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;

public class ReadDefaultSettingsFileTest extends BaseTest {

    @After
    public void reset() throws InterruptedException {
        Thread.sleep(1000);
        resetHelper.setAccountsCountersToMax();
    }



    @Test public void readDefaultSettingsFileTest() throws InterruptedException {
        Thread.sleep(2000);
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler=JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        //wenn die Blockchain neu
        Assert.assertEquals("0x731a10897d267e19b34503ad902d0a29173ba4b1",jsonDefaultSettingsHandler.getDefaultSettings().getCertifierAddress());
        Assert.assertEquals("http://jurijnas.myqnapcloud.com:8545/",jsonDefaultSettingsHandler.getDefaultSettings().getConnectionAddress());
        Assert.assertEquals("0x0000000000000000000000000000000000001337",jsonDefaultSettingsHandler.getDefaultSettings().getNameRegistryAddress());
        Assert.assertEquals(2,jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());
        Assert.assertEquals(new BigInteger("50000000"),jsonDefaultSettingsHandler.getDefaultSettings().getDefaultGasLimit());
        Assert.assertEquals(new BigInteger("3"),jsonDefaultSettingsHandler.getDefaultSettings().getDefaultRevokeTime());
        Assert.assertEquals(new BigInteger("2"),jsonDefaultSettingsHandler.getDefaultSettings().getDefaultTxLimit());
    }


}

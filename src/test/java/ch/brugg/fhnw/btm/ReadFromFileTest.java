package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.tree.BaseType;

import java.math.BigInteger;
import java.util.ArrayList;

public class ReadFromFileTest extends BaseTest {

    @After
    @Before
    public void reset() throws InterruptedException {
        Thread.sleep(5000);
        resetHelper.setAccountsCountersToMax();
    }



    @Test public void readDefaultSettingsFileTest() throws InterruptedException {
        Thread.sleep(2000);
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler=JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        Assert.assertEquals("0xee35211c4d9126d520bbfeaf3cfee5fe7b86f221",jsonDefaultSettingsHandler.getDefaultSettings().getCertifierAddress());
        Assert.assertEquals("http://jurijnas.myqnapcloud.com:8545/",jsonDefaultSettingsHandler.getDefaultSettings().getConnectionAddress());
        Assert.assertEquals("0x0000000000000000000000000000000000001337",jsonDefaultSettingsHandler.getDefaultSettings().getNameRegistryAddress());
        Assert.assertEquals(2,jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval());
        Assert.assertEquals(new BigInteger("50000000"),jsonDefaultSettingsHandler.getDefaultSettings().getDefaultGasLimit());
        Assert.assertEquals(new BigInteger("3"),jsonDefaultSettingsHandler.getDefaultSettings().getDefaultRevokeTime());
        Assert.assertEquals(new BigInteger("5"),jsonDefaultSettingsHandler.getDefaultSettings().getDefaultTxLimit());
    }

    @Test public void readAccountFileTest() throws Exception {
        Thread.sleep(2000);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.setJsonAccountList(new ArrayList<>());
        Assert.assertEquals(0, jsonAccountHandler.getJsonAccountList().size());
        jsonAccountHandler.loadAccounts();
        Assert.assertEquals(5, jsonAccountHandler.getJsonAccountList().size());
        JsonAccount account1 = jsonAccountHandler.getAccount("0xaf02DcCdEf3418F8a12f41CB4ed49FaAa8FD366b");
        Assert.assertEquals("0xaf02DcCdEf3418F8a12f41CB4ed49FaAa8FD366b", account1.getAddress());
        Assert.assertEquals(50000000, account1.getRemainingGas());
        Assert.assertEquals(null,account1.getTransactionLimit());
        Assert.assertEquals(5,account1.getRemainingTransactions());
        Assert.assertNull(account1.getRevokeTime());
        Assert.assertNull(account1.getGasLimit());
        Assert.assertNull(account1.getTimeStamp());
        Assert.assertFalse(account1.isDeleteMe());

        JsonAccount account2 = jsonAccountHandler.getAccount("0xf13264C4bD595AEbb966E089E99435641082ff24");
        Assert.assertEquals("0xf13264C4bD595AEbb966E089E99435641082ff24", account2.getAddress());
        Assert.assertEquals(50000000, account2.getRemainingGas());
        Assert.assertNull(account2.getTransactionLimit());
        Assert.assertEquals(5, account2.getRemainingTransactions());
        Assert.assertEquals(null, account2.getRevokeTime());
        Assert.assertEquals(null, account2.getGasLimit());
        Assert.assertEquals(null, account2.getTimeStamp());
        Assert.assertEquals(false,account2.isDeleteMe());

        JsonAccount account3 = jsonAccountHandler.getAccount("0x00a329c0648769A73afAc7F9381E08FB43dBEA72");
        Assert.assertEquals("0x00a329c0648769A73afAc7F9381E08FB43dBEA72", account3.getAddress());
        Assert.assertEquals(111111, account3.getRemainingGas());
        Assert.assertEquals(new BigInteger("2"), account3.getTransactionLimit());
        Assert.assertEquals(2, account3.getRemainingTransactions());
        Assert.assertEquals(new BigInteger("3"), account3.getRevokeTime());
        Assert.assertEquals(new BigInteger("111111"), account3.getGasLimit());
        Assert.assertEquals(null, account3.getTimeStamp());
        Assert.assertEquals(false,account3.isDeleteMe());

        JsonAccount account4 = jsonAccountHandler.getAccount("0x3e7Beee9585bA4526e8a7E41715D93B2bE014B34");
        Assert.assertEquals("0x3e7Beee9585bA4526e8a7E41715D93B2bE014B34", account4.getAddress());
        Assert.assertEquals(42000, account4.getRemainingGas());
        Assert.assertEquals(new BigInteger("5"), account4.getTransactionLimit());
        Assert.assertEquals(5, account4.getRemainingTransactions());
        Assert.assertEquals(new BigInteger("1"), account4.getRevokeTime());
        Assert.assertEquals(new BigInteger("42000"), account4.getGasLimit());
        Assert.assertEquals(null, account4.getTimeStamp());
        Assert.assertEquals(false,account4.isDeleteMe());

    }
}

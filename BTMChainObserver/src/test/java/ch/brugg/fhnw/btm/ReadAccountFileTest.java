package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;

public class ReadAccountFileTest {

    @Test public void readAccountFileTest() throws Exception {
        Thread.sleep(2000);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.setJsonAccountList(new ArrayList<>());
        Assert.assertEquals(0, jsonAccountHandler.getJsonAccountList().size());
        jsonAccountHandler.loadAccounts();
        Assert.assertEquals(7, jsonAccountHandler.getJsonAccountList().size());
        JsonAccount account1 = jsonAccountHandler.getAccount("0xaf02DcCdEf3418F8a12f41CB4ed49FaAa8FD366b");
        Assert.assertEquals("0xaf02DcCdEf3418F8a12f41CB4ed49FaAa8FD366b", account1.getAddress());
        Assert.assertEquals(50000000, account1.getRemainingGas());
        Assert.assertEquals(null,account1.getTransactionLimit());
        Assert.assertEquals(2,account1.getRemainingTransactions());
        Assert.assertNull(account1.getRevokeTime());
        Assert.assertNull(account1.getGasLimit());
        Assert.assertNull(account1.getTimeStamp());
        Assert.assertFalse(account1.isDeleteMe());

        JsonAccount account2 = jsonAccountHandler.getAccount("0xf13264C4bD595AEbb966E089E99435641082ff24");
        Assert.assertEquals("0xf13264C4bD595AEbb966E089E99435641082ff24", account2.getAddress());
        Assert.assertEquals(50000000, account2.getRemainingGas());
        Assert.assertNull(account2.getTransactionLimit());
        Assert.assertEquals(2, account2.getRemainingTransactions());
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
        Assert.assertEquals(new BigInteger("3"), account4.getRevokeTime());
        Assert.assertEquals(new BigInteger("42000"), account4.getGasLimit());
        Assert.assertEquals(null, account4.getTimeStamp());
        Assert.assertEquals(false,account4.isDeleteMe());

    }
}

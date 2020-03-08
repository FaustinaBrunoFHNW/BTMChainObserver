package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntervallResetterTest extends BaseTest {

    @Test public void transactionReset() throws Exception {

        this.setUpChain();
        Thread.sleep(1000);
        JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
        JsonAccount account = accountHandler.getAccount(ADDRESS);
        BigDecimal ether = new BigDecimal("1");
        int anzahltTransaktionen = 2;
        Assert.assertEquals(account.getTransactionLimit().intValue(), account.getRemainingTransactions());
        sendEtherHelper.txLoop(anzahltTransaktionen, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Thread.sleep(15000);
        Assert.assertEquals(account.getTransactionLimit().intValue() - anzahltTransaktionen,
                account.getRemainingTransactions());
        Thread.sleep(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval() * 60000);
        Assert.assertEquals(account.getTransactionLimit().intValue(), account.getRemainingTransactions());
    }

    @Test public void gasReset() throws Exception {

        this.setUpChain();
        Thread.sleep(1000);
        JsonAccountHandler accountHandler = JsonAccountHandler.getInstance();
        JsonAccount account = accountHandler.getAccount(ADDRESS);
        BigDecimal ether = new BigDecimal("100");
        int anzahltTransaktionen = 1;
        Assert.assertEquals(42000, account.getRemainingGas());
        sendEtherHelper.txLoop(anzahltTransaktionen, account.getAddress(), ether, GASPRICEZERO, GASLIMIT);
        Thread.sleep(15000);
        Assert.assertEquals(21000, account.getRemainingGas());
        Thread.sleep(jsonDefaultSettingsHandler.getDefaultSettings().getResetInterval() * 60000);
        Assert.assertEquals(42000, account.getRemainingGas());
    }
}

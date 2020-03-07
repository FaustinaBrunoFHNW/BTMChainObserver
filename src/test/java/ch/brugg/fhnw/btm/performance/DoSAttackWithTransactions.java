package ch.brugg.fhnw.btm.performance;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.Main;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DoSAttackWithTransactions {
    private SendEtherHelper sendEtherHelper;
    private ChainInteractions chainInteractions;

    @BeforeClass public void setUpChain() throws Exception {
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        jsonDefaultSettingsHandler.loadDefaultSettings();
        ChainSetup chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
         chainInteractions = new ChainInteractions(chainSetup);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();
        jsonAccountHandler.writeAccountList();
    }

    @Test public void txAttack100000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("543");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, addressTo, ether, gasPrice, gasLimit);
    }

    @Test public void txAttack1000000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("56");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, addressTo, ether, gasPrice, gasLimit);
    }

    @Test public void txAttack10000000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("34");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, addressTo, ether, gasPrice, gasLimit);
    }

    @Test public void txAttack100000000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("23");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(100000, addressTo, ether, gasPrice, gasLimit);
    }
    private void certifyAddress(String address){
        this.chainInteractions.certifyAccount(address);
    }
}

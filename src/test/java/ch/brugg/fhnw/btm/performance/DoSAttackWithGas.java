package ch.brugg.fhnw.btm.performance;

import ch.brugg.fhnw.btm.AccountCertifyTest;
import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.Main;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import ch.brugg.fhnw.btm.helper.SendEtherHelper;
import ch.brugg.fhnw.btm.pojo.JsonAccount;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DoSAttackWithGas {

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
        sendEtherHelper = new SendEtherHelper();

    }

    @Test public void gasAttack100000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("100000");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(2,addressTo,ether,gasPrice,gasLimit);
    }

    @Test public void gasAttack1000000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("1000000");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(2,addressTo,ether,gasPrice,gasLimit);
    }

    @Test public void gasAttack10000000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("10000000");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(2,addressTo,ether,gasPrice,gasLimit);
    }

    @Test public void gasAttack100000000() throws Exception {
        String addressTo = "0xf13264C4bD595AEbb966E089E99435641082ff24";
        this.certifyAddress(addressTo);
        BigDecimal ether = new BigDecimal("100000000");
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("21000");
        sendEtherHelper.txLoop(2,addressTo,ether,gasPrice,gasLimit);
    }



    private void certifyAddress(String address){
        this.chainInteractions.certifyAccount(address);
    }
}

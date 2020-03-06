package ch.brugg.fhnw.btm.performance;

import ch.brugg.fhnw.btm.ChainInteractions;
import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.Main;
import ch.brugg.fhnw.btm.handler.JsonAccountHandler;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.junit.BeforeClass;
import org.junit.Test;

public class DoSAttackWithGas {


    @BeforeClass public void setUpChain() throws Exception {
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        jsonDefaultSettingsHandler.loadDefaultSettings();
        ChainSetup chainSetup = ChainSetup.getInstance();
        ChainSetup.getInstance().setUpAfterChainStart();
        ChainInteractions chainInteractions = new ChainInteractions(chainSetup);
        JsonAccountHandler jsonAccountHandler = JsonAccountHandler.getInstance();
        jsonAccountHandler.loadAccounts();
        jsonAccountHandler.writeAccountList();
    }


    @Test
    public void gasAttack100000(){
       // Transaction transaction = Transaction.createContractTransaction( <from address>,<nonce>, BigInteger.valueOf(<gas price>),  // we use default gas limit"0x...<smart contract code to execute>");

       // org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse = parity.ethSendTransaction(ethSendTransaction).send();

       // String transactionHash = transactionResponse.getTransactionHash();
    }
    @Test
    public void gasAttack1000000(){}
    @Test
    public void gasAttack10000000(){}
    @Test
    public void gasAttack100000000(){}
}

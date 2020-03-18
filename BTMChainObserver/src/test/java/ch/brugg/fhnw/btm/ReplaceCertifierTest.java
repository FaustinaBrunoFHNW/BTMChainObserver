package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class ReplaceCertifierTest {
    private static String PRIVATE_KEY = "2D4E070C7586E271AE9DB6C2F44E154A5BCBEDFBC6AA7BEFD2A7E9EACAC2BFFE";
    private SimpleRegistry simpleRegistry;
    private SimpleCertifier simpleCertifier;
    private static BigInteger REGISTRATION_FEE = BigInteger.valueOf(1000000000000000000L);
    private static String ADDRESS_TXLIMITHIGH = "0x2063Cfc6a737a9033459014C0ad444a1ae02d2DB";

    private static String CERTIFYER_ADD = "0xee35211c4d9126d520bbfeaf3cfee5fe7b86f221";

    private static String NAMEREGADDCORRECT = "0x0000000000000000000000000000000000001337";


    @Test
    public void replaceCertifierTest() throws Exception {
        JsonDefaultSettingsHandler jsonDefaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();
        jsonDefaultSettingsHandler.loadDefaultSettings();
        Web3j web3j = Web3j.build(new HttpService(jsonDefaultSettingsHandler.getDefaultSettings().getConnectionAddress()));

        TransactionManager tm = new RawTransactionManager(web3j, this.getCredentialsFromPrivateKey());
        String str_hash = "6d3815e6a4f3c7fcec92b83d73dda2754a69c601f07723ec5a2274bd6e81e155";
        byte[] hash = new BigInteger(str_hash, 16).toByteArray();

        simpleRegistry = SimpleRegistry.load(
                jsonDefaultSettingsHandler.getDefaultSettings().getNameRegistryAddress(),
                web3j,
                this.getCredentialsFromPrivateKey(),
                new DefaultGasProvider()
        );

        Assert.assertEquals(REGISTRATION_FEE, simpleRegistry.fee().send());

        simpleCertifier = SimpleCertifier.deploy(
                web3j,
                tm,
                new DefaultGasProvider()
        ).send();

        TransactionReceipt receiptReserve = simpleRegistry.reserve(hash, REGISTRATION_FEE).send();
        System.out.println(receiptReserve.toString());

        TransactionReceipt receiptSetAdd = simpleRegistry.setAddress(hash, "A", simpleCertifier.getContractAddress()).send();
        System.out.println(receiptSetAdd);

        Assert.assertTrue("Der neue Certifier konnte registriert werden", !simpleRegistry.getAddress(hash, "A").equals(jsonDefaultSettingsHandler.getDefaultSettings().getCertifierAddress()));

    }

    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }
}

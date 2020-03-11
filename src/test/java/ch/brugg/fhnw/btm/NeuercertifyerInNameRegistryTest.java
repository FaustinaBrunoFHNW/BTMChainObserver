package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
import ch.brugg.fhnw.btm.handler.JsonDefaultSettingsHandler;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

public class NeuercertifyerInNameRegistryTest {
    private static String PRIVATE_KEY = "2D4E070C7586E271AE9DB6C2F44E154A5BCBEDFBC6AA7BEFD2A7E9EACAC2BFFE";
    private static String ADDRESS_TXLIMITHIGH = "0x2063Cfc6a737a9033459014C0ad444a1ae02d2DB";

    private static String CERTIFYER_ADD = "0xee35211c4d9126d520bbfeaf3cfee5fe7b86f221";

    private static String NAMEREGADDCORRECT = "0x0000000000000000000000000000000000001337";
    private static String NAMEREGADFALSE = "0x0000000000000000000000000000000000009999";

    /**
    @Test public void registrierungNameRegMitNeuemCertifyer() {
        JsonDefaultSettingsHandler defaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        defaultSettingsHandler.loadDefaultSettings();
        defaultSettingsHandler.getDefaultSettings().setCertifierAddress(ADDRESS_TXLIMITHIGH);
        ChainSetup chainSetup = ChainSetup.getInstance();

        //Wird aber schon aus dem File ausgelesen
        chainSetup.setPrivateKey(PRIVATE_KEY);
        chainSetup.loadCertifier(ADDRESS_TXLIMITHIGH);
    }
*/
    @Test public void registrierungInNameRegVonNeuemcertifier() throws Exception {

        JsonDefaultSettingsHandler defaultSettingsHandler = JsonDefaultSettingsHandler.getInstance();

        defaultSettingsHandler.loadDefaultSettings();
        ChainSetup chainSetup = ChainSetup.getInstance();
        chainSetup.setUpAfterChainStart();

        //NEUER TM
         chainSetup.setPrivateKey(PRIVATE_KEY);
         chainSetup.setCertifierAddress(ADDRESS_TXLIMITHIGH);

        TransactionManager tm = new RawTransactionManager(chainSetup.getWeb3j(), this.getCredentialsFromPrivateKey());
          chainSetup.setTransactionManager(tm);

          chainSetup.loadSimpleRegistry();
        //DEPLOYEMT CERTIFIER
        chainSetup.deployCertifier(tm);
        //INSTANZIERT CERTIFIER MIT NEUEM CERTIFIER und neuen ACCOUNT
        chainSetup.loadCertifier(ADDRESS_TXLIMITHIGH);
        System.out.println("Certifier Add" + chainSetup.getCertifierAddress());
        System.out.println("TM Add" + chainSetup.getTransactionManager().getFromAddress());

       //  chainSetup.loadSimpleRegistry();
        // chainSetup.setSimpleRegistry(chainSetup.getSimpleRegistry());
        chainSetup.registerCertifier();
    }

    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }
}

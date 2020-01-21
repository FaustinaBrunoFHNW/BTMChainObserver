package ch.brugg.fhnw.btm;

import org.junit.Test;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

public class AccountCertifyTest {

    private final static String PRIVATE_KEY = "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7";

    private final static String CERTIFIED_ADD = "0x00a329c0648769a73afac7f9381e08fb43dbea72";
    private static BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
    private static BigInteger GAS_PRICE = BigInteger.valueOf(1000000000); //1 GWEI
   private TransactionManager transactionManager;
    // private static BigInteger GAS_PRICE = BigInteger.valueOf(0);

    private ChainSetUp chainSetUp = new ChainSetUp(PRIVATE_KEY);

    @Test
    public static void main(String[] args) throws Exception {

/**
        //Allgemeine Abfrage zur Chain ob alles funktioniert etc
        getInfo(web3j);

        loadSimpleRegistry();

        //Deploy Simple Cert
        //Entweder SetUp oder Load
        //Dieses wird benutzt wenn Chain noch nie gelaufen ist

        setUpCertifier();

        //Load Simple Cert
        //Entweder SetUp oder Load
        //Dieses wird benutzt wenn Chain schonmal gelaufen ist
        //Parameter wird aus Info vom First Run ausgegeben

        // loadCertifier("0xd5f051401ca478b34c80d0b5a119e437dc6d9df5");

        loadCertifier("0x731a10897d267e19b34503ad902d0a29173ba4b1");

        SubscriptionTX subscriptionTX = new SubscriptionTX(web3j);
        subscriptionTX.run();

        //Verify

        log.info("Certified Adress is certified: " + String.valueOf(isCertified(P_ADD)));
        log.info("Uncertified Adress is certified: " + String.valueOf(isCertified(TEST_ADD)));

        //doTestTransactions();

        log.info("Size of Account -List = " + Loader.getInstance().getAccounts().size());

        Loader.getInstance().getAccounts().forEach((key, value) -> {
            log.info("check if certified " + key);
            if (isCertified(key)) {
                log.info("true");
            } else {
                log.info("certifying: " + key);
                certifyAccount(key);
            }
        });

        doTestTransactionsCertified();
        doTestTransactionsCertified();
        doTestTransactionsUnCertified();
 **/
    }

}

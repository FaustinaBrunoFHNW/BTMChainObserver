package ch.brugg.fhnw.btm;

import ch.brugg.fhnw.btm.contracts.SimpleCertifier;
import ch.brugg.fhnw.btm.contracts.SimpleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

public class ChainSetUp {

    // Tutorial can be found here: https://kauri.io/connecting-to-an-ethereum-client-with-java-eclipse-and-web3j/b9eb647c47a546bc95693acc0be72546/a
    // Video Tutorial https://www.youtube.com/watch?v=kJ905hVbQ_E

    private final static String PRIVATE_KEY = "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7";
    private final static String PRIVATE_KEY_TEST = "080BACEA441C50059857889AFD35D22E91C5F27C1639BE441C8E456DF678B28D";
    private static BigInteger REGISTRATION_FEE = BigInteger.valueOf(1000000000000000000L);
    private static BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
    private static BigInteger GAS_PRICE = BigInteger.valueOf(1000000000); //1 GWEI
    // private static BigInteger GAS_PRICE = BigInteger.valueOf(0);
    private static Web3j web3j;
    private static TransactionManager transactionManager;
    private static TransactionManager transactionManagerTest;
    private static SimpleCertifier simpleCertifier;
    private static SimpleRegistry simpleRegistry;
    private static byte[] hash;
    private static Logger log = LoggerFactory.getLogger(ChainSetUp.class);

    public ChainSetUp() {

        log.info("connecting");

        //Verbindungsaufbau zur Chain
        web3j = Web3j.build(new HttpService("http://192.168.99.100:8545/"));

        //TODO in Test auslagern
        //TM von dem Account mit dem PK
      //  transactionManager = new RawTransactionManager(web3j, getCredentialsFromPrivateKey());
        //transactionManagerTest = new RawTransactionManager(web3j, getCredentialsFromPrivateKeyTest());

        //sha3(service_transaction_checker) = 0x6d3815e6a4f3c7fcec92b83d73dda2754a69c601f07723ec5a2274bd6e81e155
        String str_hash = "6d3815e6a4f3c7fcec92b83d73dda2754a69c601f07723ec5a2274bd6e81e155";

        //Umwandeln in Bytearray Hexadecimal
        hash = new BigInteger(str_hash, 16).toByteArray();



    }





}

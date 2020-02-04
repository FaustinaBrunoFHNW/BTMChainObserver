package ch.brugg.fhnw.btm;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

public class AccountCertifyTest {

    private final static String PRIVATE_KEY = "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7";

    private final static String CERTIFIED_ADD = "0x00a329c0648769a73afac7f9381e08fb43dbea72";
    private static BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
    private static BigInteger GAS_PRICE = BigInteger.valueOf(1000000000); //1 GWEI
    // private static BigInteger GAS_PRICE = BigInteger.valueOf(0);


    public ChainSetUp chainSetUp = new ChainSetUp(PRIVATE_KEY);
    private ChainInteractions chainInteractions = new ChainInteractions(chainSetUp);

    @BeforeClass public void setUpChain() throws Exception {
       // chainSetUp.setUpAfterChaiStart(CERTIFIED_ADD, PRIVATE_KEY);
    }

    @Test public void main(String[] args) throws Exception {
         String adress="0x84b51678f9a4869e384f737ed2a5d56c8ca16c81";
        //Load Simple Cert
        //Entweder SetUp oder Load
        //Dieses wird benutzt wenn Chain schonmal gelaufen ist
        //Parameter wird aus Info vom First Run ausgegeben
        this.chainSetUp.loadCertifier(adress);


        //Verify
      //  System.out.println("Certified Adress is certified: "+ String.valueOf(this.chainSetUp.isCertified(CERTIFIED_ADD) ));
        //TODO für uncertified Account
     //   log.info("Uncertified Adress is certified: " + String.valueOf(isCertified(TEST_ADD)));


        doTestTransactionsCertified();
        doTestTransactionsCertified();
       //TODO für uncertified Account
        // doTestTransactionsUnCertified();

    }


    private  void doTestTransactionsCertified() {
        System.out.println("Certified Transaction should work");
        try {
           boolean status = this.chainInteractions.sendEtherToAccount(GAS_PRICE,GAS_LIMIT,CERTIFIED_ADD).isStatusOK();
            if (status == false) {
                System.out.println("But did not work. Status: " + status);
            } else {
                System.out.println("and it did work. Status:" + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

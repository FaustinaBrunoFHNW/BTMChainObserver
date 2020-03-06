package ch.brugg.fhnw.btm.performance;

import ch.brugg.fhnw.btm.ChainSetup;
import ch.brugg.fhnw.btm.Main;
import org.junit.BeforeClass;
import org.junit.Test;

public class DoSAttackWithGas {

    Main main = new Main();

    @BeforeClass public void setUpChain() throws Exception {
      main.run();
    }


    @Test
    public void gasAttack100000(){

    }
    @Test
    public void gasAttack1000000(){}
    @Test
    public void gasAttack10000000(){}
    @Test
    public void gasAttack100000000(){}
}

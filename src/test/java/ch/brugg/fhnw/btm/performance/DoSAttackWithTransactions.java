package ch.brugg.fhnw.btm.performance;

import ch.brugg.fhnw.btm.Main;
import org.junit.BeforeClass;
import org.junit.Test;

public class DoSAttackWithTransactions {

    Main main = new Main();

    @BeforeClass public void setUpChain() throws Exception {
        main.run();
    }

    @Test
    public void txAttack100000(){}
    @Test
    public void txAttack1000000(){}
    @Test
    public void txAttack10000000(){}
    @Test
    public void txAttack100000000(){}
}

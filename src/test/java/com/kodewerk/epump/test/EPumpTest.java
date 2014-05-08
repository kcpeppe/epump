package com.kodewerk.epump.test;

import com.kodewerk.epump.EventPump;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class EPumpTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void queriesProduceExpectedResults() {
        try {
            DoubleSumQuery doubleSumQuery = new DoubleSumQuery();
            IntegerSumQuery integerSumQuery = new IntegerSumQuery();
            LongSumQuery longSumQuery = new LongSumQuery();
            EventPump<Query> pump = new EventPump(new DataSource());
            pump.registerSinkPoint(doubleSumQuery);
            pump.registerSinkPoint(integerSumQuery);
            //should not and does not compile!
            // pump.registerSinkPoint(longSumQuery);
            pump.start();
            pump.waitForClosing();
            assertTrue( doubleSumQuery.toString().equals("22.5"));
            assertTrue( integerSumQuery.toString().equals("45"));
            assertTrue( longSumQuery.toString().equals("0"));
        } catch(Throwable t) {
            System.out.println(t.getMessage());
            t.printStackTrace();
            fail();
        }
    }

    @Test
    public void forgotToRegisterASink() {
        EventPump pump = new EventPump(new DataSource());
        pump.start();
        pump.waitForClosing();
        assertTrue(true);
    }

    /**
     * the guard reports on and then eats the exception.. no exceptions are thrown
     * the exceptions should some how be retrievable from the pump.
     */
    @Test
    public void SinkThrowsException() throws Throwable {
        //exception.expect(ArithmeticException.class);
        ThrowsDivideByZeroQuery badQuery = new ThrowsDivideByZeroQuery();
        EventPump pump = new EventPump(new DataSource());
        pump.registerSinkPoint(badQuery);
        pump.start();
        pump.waitForClosing();
        assertTrue(true); //if we've made it here no exceptions leaked into the test
//        if ( pump.encounteredErrors())
//            throw pump.getLastError( badQuery);
    }
}

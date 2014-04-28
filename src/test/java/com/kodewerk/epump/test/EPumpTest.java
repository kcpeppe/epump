package com.kodewerk.epump.test;

import com.kodewerk.epump.EventPump;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;

public class EPumpTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void queriesProduceExpectedResults() throws IOException {
        DoubleSumQuery doubleSumQuery = new DoubleSumQuery();
        IntegerSumQuery integerSumQuery = new IntegerSumQuery();
        EventPump pump = new EventPump(new DataSource());
        pump.registerSinkPoint(doubleSumQuery);
        pump.registerSinkPoint(integerSumQuery);
        pump.start();
        pump.waitForClosing();
        assertTrue( doubleSumQuery.toString().equals("22.5"));
        assertTrue( integerSumQuery.toString().equals("45"));
    }

    @Test
    public void forgotToRegisterASink() {
        EventPump pump = new EventPump(new DataSource());
        pump.start();
        pump.waitForClosing();
        assertTrue(true);
    }

    @Test
    public void SinkThrowsException() throws Throwable {
        exception.expect(ArithmeticException.class);
        ThrowsDivideByZeroQuery badQuery = new ThrowsDivideByZeroQuery();
        EventPump pump = new EventPump(new DataSource());
        pump.registerSinkPoint(badQuery);
        pump.start();
        pump.waitForClosing();
        if ( pump.encounteredErrors())
            throw pump.getLastError( badQuery);
    }
}

package com.kodewerk.epump.test;

import com.kodewerk.epump.EventPump;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;

public class EPumpTest {

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
}

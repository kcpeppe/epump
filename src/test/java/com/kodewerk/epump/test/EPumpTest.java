package com.kodewerk.epump.test;

/**
 Copyright [2014] [Kirk Pepperdine]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

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
            assertTrue( pump.getLastException() == null);
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
        assertTrue(pump.getLastException() == null);
    }

    /**
     * the guard reports on and then eats the exception.. no exceptions are thrown
     * the exceptions should some how be retrievable from the pump.
     */
    @Test
    public void SinkThrowsException() {
        ThrowsDivideByZeroQuery badQuery = new ThrowsDivideByZeroQuery();
        DataSource dataSource = new DataSource();
        EventPump pump = new EventPump(dataSource);
        pump.registerSinkPoint(badQuery);
        pump.start();
        pump.waitForClosing();
        assertTrue(pump.getLastException() != null);
    }

    @Test
    public void slowDataSource() {
        try {
            DoubleSumQuery doubleSumQuery = new DoubleSumQuery();
            IntegerSumQuery integerSumQuery = new IntegerSumQuery();
            LongSumQuery longSumQuery = new LongSumQuery();
            EventPump<Query> pump = new EventPump(new SlowDataSource());
            pump.registerSinkPoint(doubleSumQuery);
            pump.registerSinkPoint(integerSumQuery);
            pump.start();
            pump.waitForClosing();
            assertTrue( doubleSumQuery.toString().equals("22.5"));
            assertTrue( integerSumQuery.toString().equals("45"));
            assertTrue( longSumQuery.toString().equals("0"));
            assertTrue( pump.getLastException() == null);
        } catch(Throwable t) {
            System.out.println(t.getMessage());
            t.printStackTrace();
            fail();
        }
    }
}

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

import com.kodewerk.epump.Event;
import com.kodewerk.epump.EventSource;

public class SlowDataSource implements EventSource {

    private int intCount = 0;
    private int doubleCount = 0;
    private int longCount = 0;

    public boolean endOfStream() { return ( intCount == 10) && (doubleCount == 10) && (longCount == 10); }

    public Event read() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {}
        if ( intCount != doubleCount)
            return new IntegerEvent(intCount++);
        else if ( doubleCount != longCount) {
            DoubleEvent event =  new DoubleEvent( (double)doubleCount / 2.0d);
            doubleCount++;
            return event;
        } else {
            return new LongEvent( longCount++);
        }
    }
}

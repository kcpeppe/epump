package com.kodewerk.epump.test;

import com.kodewerk.epump.Event;
import com.kodewerk.epump.EventSource;

public class DataSource implements EventSource {

    private int intCount = 0;
    private int doubleCount = 0;
    private int longCount = 0;

    public boolean endOfStream() { return ( intCount == 10) && (doubleCount == 10) && (longCount == 10); }

    public Event read() {
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

package com.kodewerk.epump.test;

import com.kodewerk.epump.Event;
import com.kodewerk.epump.SinkPoint;

public class IntegerEvent implements Event {

    private int value;

    public IntegerEvent( int value) {
        this.value = value;
    }

    public int getValue() { return this.value; }

    public void execute(Query query) {
        query.processEvent(this);
    }

    public void writeTo(SinkPoint sinkPoint) {
        this.execute((Query) sinkPoint);
    }
}

package com.kodewerk.epump.test;

import com.kodewerk.epump.Event;
import com.kodewerk.epump.SinkPoint;

public class DoubleEvent implements Event {

    private double value;

    public DoubleEvent( double value) {
        this.value = value;
    }

    public double getValue() { return value; }

    public void execute(Query query) {
        query.processEvent(this);
    }

    @Override
    public void writeTo(SinkPoint sinkPoint) {
        this.execute((Query)sinkPoint);
    }
}

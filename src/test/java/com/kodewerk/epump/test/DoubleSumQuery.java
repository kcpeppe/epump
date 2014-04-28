package com.kodewerk.epump.test;

import com.kodewerk.epump.Event;

public class DoubleSumQuery extends Query {

    private double sum = 0.0d;

    public void processEvent(DoubleEvent event) {
        sum += event.getValue();
    }

    public String toString() {
        return Double.toString(sum);
    }

    @Override
    public void accept(Event event) {
        event.writeTo(this);
    }
}

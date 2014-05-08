package com.kodewerk.epump.test;

import com.kodewerk.epump.SinkPoint;
import com.kodewerk.epump.Event;


public class LongSumQuery implements SinkPoint {

    private long sum = 0;

    public void processEvent(LongEvent event) {
        sum += event.getValue();
    }

    @Override
    public void accept(Event event) {
        event.writeTo(this);
    }

    public String toString() {
        return Long.toString(sum);
    }
}

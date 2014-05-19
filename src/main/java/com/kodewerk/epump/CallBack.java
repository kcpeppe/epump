package com.kodewerk.epump;

import java.util.concurrent.LinkedTransferQueue;

final public class CallBack<T extends SinkPoint> {

    final private LinkedTransferQueue<Event> events = new LinkedTransferQueue<Event>();

    private T sink;
    private volatile boolean running;

    public CallBack( T sink) {
        this.sink = sink;
    }

    public T getSinkPoint() { return this.sink; }

    void callBack(Event event) {
        sink.accept( event);
    }

}

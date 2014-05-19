package com.kodewerk.epump;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.Queue;

final public class CallBack<T extends SinkPoint> {

    final private Queue<Event> events = new LinkedBlockingQueue<Event>();

    private T sink;

    public CallBack( T sink) {
        this.sink = sink;
    }

    void callBack(Event event) {
        sink.accept( event);
    }

}

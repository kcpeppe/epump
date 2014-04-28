package com.kodewerk.epump;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventPump implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(EventPump.class.getName());

    //todo: not concurrent
    private Map<SinkPoint,CallBack> callBacks = new ConcurrentHashMap<>();
    private EventSource eventSource;
    private Thread pump;
    volatile private boolean running = false;

    public EventPump( EventSource source) {
        this.eventSource = source;
    }
    public void registerSinkPoint( SinkPoint sinkPoint) {
        this.callBacks.put(sinkPoint, new CallBack(sinkPoint));
    }
    public void unregisterCallBack( SinkPoint sinkPoint) {
        this.callBacks.remove(sinkPoint);
    }

    public void run() {
        running = true;
        while( running && ! eventSource.endOfStream()) {
            Event event = eventSource.read();
            for ( CallBack callBack : callBacks.values())
                callBack.callBack(event);
        }
    }

    public void start() {
        pump = new Thread( this);
        pump.start();
    }

    public void waitForClosing() {
        try {
            pump.join();
        } catch (InterruptedException ie) {
            LOGGER.log(Level.WARNING, ie.getMessage(), ie);
        }
        running = false;
    }

    //todo: ugly and won't work in edge cases.
    public void shutdown() {
        if ( running == false) return;

        running = false;
        try {
            pump.join();
        } catch (InterruptedException ie) {
            LOGGER.log(Level.WARNING, ie.getMessage(), ie);
        }
    }

}

package com.kodewerk.epump;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Crude implementation of a class that will pump events to all registered SinkPoints.
 */
public class EventPump< S extends SinkPoint> implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(EventPump.class.getName());
    private static AtomicInteger threadId= new AtomicInteger(0);


    private Map<S,Guard> guards = new ConcurrentHashMap<S,Guard>();
    private volatile Throwable exception = null;

    private EventSource eventSource;
    private Thread pump;
    volatile private boolean running = false;

    public EventPump( EventSource source) {
        this.eventSource = source;
    }
    public void registerSinkPoint( S sinkPoint) {
        this.guards.put(sinkPoint, new Guard(new CallBack(sinkPoint)));
    }

    public void unregisterCallBack( S sinkPoint) {
        Guard guard = this.guards.remove(sinkPoint);
        guard.shutdown();
    }

    public void run() {

        running = true;
        while( running && ! eventSource.endOfStream()) {
            Event event = eventSource.read();
            if ( event != null)
                for ( Guard guard : guards.values())
                    try {
                        guard.accept(event);
                    } catch (Throwable t) {
                        LOGGER.log(Level.WARNING, "CallBack throws an Exception", t);
                        exception = t;
                    }
        }
    }

    /**
     * Start the pump.
     *
     */
    public void start() {
        pump = new Thread( this, "epump-" + threadId.getAndIncrement());
        pump.start();
    }

    public void waitForClosing() {
        try {
            pump.join();
            for ( S sinkPoint : this.guards.keySet()) {
                setException(this.guards.get( sinkPoint).getLastException());
                unregisterCallBack( sinkPoint);
            }
        } catch (InterruptedException ie) {
            LOGGER.log(Level.WARNING, ie.getMessage(), ie);
        }
        running = false;
    }

    //todo: ugly and won't work in edge cases.
    public void shutdown() {
        running = false;
        waitForClosing();
    }

    private void setException( EventPumpException e) {
        if ( e == null) return;
        exception = e;
    }
    public Throwable getLastException() { return exception; }
}

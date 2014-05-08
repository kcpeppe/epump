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

    private static AtomicInteger threadId= new AtomicInteger(0);

    private final static Logger LOGGER = Logger.getLogger(EventPump.class.getName());


    private Map<S,CallBack> callBacks = new ConcurrentHashMap<>();
    private Map<SinkPoint,Throwable> errors = new ConcurrentHashMap<>();
    private EventSource eventSource;
    private Thread pump;
    volatile private boolean running = false;

    public EventPump( EventSource source) {
        this.eventSource = source;
    }
    public void registerSinkPoint( S sinkPoint) {
        this.callBacks.put(sinkPoint, new CallBack(sinkPoint));
    }
    public void unregisterCallBack( S sinkPoint) {
        this.callBacks.remove(sinkPoint);
    }

    /**
     * todo: Error maybe due to EventSource, or thread handling
     */
    public void run() {

        errors.clear();
        running = true;
        while( running && ! eventSource.endOfStream()) {
            Event event = eventSource.read();
            if ( event != null)
                for ( CallBack callBack : callBacks.values())
                    try {
                        callBack.callBack(event);
                    } catch (Throwable t) {
                        LOGGER.log(Level.WARNING, "CallBack throws an Exception", t);
                        errors.put(callBack.getSinkPoint(), t);
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

    public boolean encounteredErrors() {
        return errors.size() != 0;
    }

    /**
     * not putting too much effort here for the moment as the
     * prefered mechanisum is to use a guard thread. However,
     * errors should be passed back to the client.
     * @param sinkPoint
     * @return
     */
    public Throwable getLastError( S sinkPoint) {
        return errors.get(sinkPoint);
    }



}

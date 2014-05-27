package com.kodewerk.epump;

/**
 Copyright [2014] [Kirk Pepperdine]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

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

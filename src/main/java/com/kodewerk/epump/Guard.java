package com.kodewerk.epump;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Guard implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(Guard.class.getName());

    private LinkedBlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
    private CallBack callBack;
    //todo: setup ThreadFactory to inject meaningful thread names
    private ExecutorService threadPool = Executors.newSingleThreadExecutor();
    private EventPumpException exception = null;

    public Guard(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() { return callBack; }

    /**
     * Last exception encounters. Maybe not be the first exception
     * @return exception
     */
    public EventPumpException getLastException() { return exception; }

    /**
     *
     * @param event to be processed
     */
    public void accept( Event event) {

        events.offer(event);
        threadPool.submit(this);

    }

    public void shutdown() {
        threadPool.shutdown();
        try {
            if ( ! threadPool.awaitTermination( 30, TimeUnit.SECONDS))
                threadPool.shutdownNow();
        } catch (InterruptedException e) {
            exception = new EventPumpException("Exception raised while terminating Guard for " + callBack.toString(), e);
            threadPool.shutdownNow();
        }
    }
    /**
     * todo: robust error handling
     * setup a protocol with EventPump to respond to InterruptedException
     */
    public void run() {
        try {
            Event event = events.poll(1, TimeUnit.SECONDS);
            callBack.callBack(event);
        } catch( Throwable t) {
            exception = new EventPumpException("Exception thrown by " + callBack.toString(), t);
        }
    }
}

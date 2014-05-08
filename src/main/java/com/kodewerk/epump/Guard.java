package com.kodewerk.epump;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Guard implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(Guard.class.getName());

    LinkedTransferQueue<Event> events = new LinkedTransferQueue<>();
    CallBack callBack;
    //todo: setup ThreadFactory to inject meaningful thread names
    ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public Guard(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() { return callBack; }

    /**
     *
     * @param event
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
            LOGGER.log(Level.WARNING, "Exception while terminating Guard", e);
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
            LOGGER.log(Level.WARNING, "CallBack throws an Exception", t);
        }
    }
}

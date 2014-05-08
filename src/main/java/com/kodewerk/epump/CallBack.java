package com.kodewerk.epump;

final public class CallBack<T extends SinkPoint> {

    private T sink;
    public CallBack( T sink) {
        this.sink = sink;
    }

    public T getSinkPoint() { return this.sink; }

    void callBack(Event event) {
        sink.accept( event);
    }

    //todo: isolate the pump from unknown client behaviour with a guard thread.
    //the client should be able to kill or block the guard thread without disturbing
    //the pump.
    class Guard implements Runnable {
        public void run() {

        }
    }
}

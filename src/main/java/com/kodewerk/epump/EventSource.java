package com.kodewerk.epump;


public interface EventSource {

    public Event read();
    public boolean endOfStream();
}

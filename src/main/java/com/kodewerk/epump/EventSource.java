package com.kodewerk.epump;


/**
 * The EventPump must be provided with an EventSource.
 *
 * Todo: bulk up interface to make it easier to work with different feeds.
 */

public interface EventSource {

    public Event read();
    public boolean endOfStream();
}

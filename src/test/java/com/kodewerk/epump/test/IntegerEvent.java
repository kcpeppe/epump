package com.kodewerk.epump.test;

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

import com.kodewerk.epump.Event;
import com.kodewerk.epump.SinkPoint;

public class IntegerEvent implements Event {

    private int value;

    public IntegerEvent( int value) {
        this.value = value;
    }

    public int getValue() { return this.value; }

    public void execute(Query query) {
        query.processEvent(this);
    }

    public void writeTo(SinkPoint sinkPoint) {
        this.execute((Query) sinkPoint);
    }
}

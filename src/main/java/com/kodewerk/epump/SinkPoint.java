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


 * Terminating point of the call back. Clients must implement this interface in order to register
 * themselves with the event pump.
 *
 * todo: This will be called with a (per client) guard thread. Exceptions thrown here shouldn't
 * kill the main event pump loop.
 *
 */
public interface SinkPoint {

    public void accept(Event event);
}

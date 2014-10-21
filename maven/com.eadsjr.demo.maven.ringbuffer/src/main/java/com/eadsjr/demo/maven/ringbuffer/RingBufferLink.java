/*
 * Copyright 2014 Jason Randolph Eads
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eadsjr.demo.maven.ringbuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents a single LinkedList-style node for a ring buffer.
 *
 * @author Jason Randolph Eads <jeads442@gmail.com>
 */
public class RingBufferLink<T> {
    private static Logger logger = LoggerFactory.getLogger(RingBufferLink.class);
    
    private final T myValue;
    private RingBufferLink myNextElement;

    /**
     * @param value The value of this element
     */
    public RingBufferLink( T value ) {
        myValue = value;
        myNextElement = null;
    }
    
    /**
     * @param value The value of this element
     * @param nextElement The next element in the ring
     */
    public RingBufferLink( T value, RingBufferLink nextElement ) {
        myValue = value;
        myNextElement = nextElement;
    }
    
    /**
     * @return The value of this element
     */
    public T getValue() {
        return myValue;
    }
    
    /**
     * @param nextElement The new target element in the buffer
     */
    public void setNextElement( RingBufferLink nextElement ) {
        myNextElement = nextElement;
    }
    
    /**
     * @return The next element in the ring
     */
    public RingBufferLink getNextElement() {
        return myNextElement;
    }
}

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

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a circular buffer that exhibits FIFO / stack-like behavior and uses
 * LinkedList-style nodes to store generic values.
 *
 * @author Jason Randolph Eads <jeads442@gmail.com>
 */
public class RingBuffer {
    private static Logger logger = LoggerFactory.getLogger(RingBuffer.class);
    
    private RingBufferLink myFirstLink;
    private RingBufferLink myLastLink;
    private int mySize;
    
    public RingBuffer() {
        myFirstLink = null;
        myLastLink = null;
        mySize = 0;
        logger.debug("created new ringbuffer {}", this);
    }
    
    /**
     * Adds some new items to the buffer in iterator order.
     * 
     * @param values The new items to be added.
     */
    public void add( List values ) {
        for( Object val : values ) {
            push(val);
        }
    }
    
    /**
     * Removes some elements from the front of the buffer and returns their
     * values.
     * 
     * @param quantity The number of elements to return.
     * @return A List of the elements removed from the buffer.
     */
    public List remove( int quantity )
            throws IndexOutOfBoundsException, IllegalArgumentException {
        
        if(quantity < 0) {
            throw new IllegalArgumentException(
                    "Attempted to remove negative number of elements");
        }
        else if( quantity > mySize ) {
            throw new IndexOutOfBoundsException(
                    "Requested removal of more elements then the buffer"+
                            " contains");
        }
        
        ArrayList values = new ArrayList(mySize);
        for( int i = 0; i < quantity; i++ ) {
            values.add(pop());
        }
        return values;
    }
    
    /**
     * Push a single value on to the front of the buffer.
     * 
     * @param <T> The type of the value
     * @param value The value to store.
     */
    public <T> void push( T value ) {
        RingBufferLink<T> newLink = new RingBufferLink<T>(value);
        if(myLastLink != null) {
            // patch the value into the ring buffer
            newLink.setNextElement(myFirstLink);
            myLastLink.setNextElement(newLink);
            myFirstLink = newLink;
        }
        else {
            // If this is the only element it refers to itself
            myFirstLink = newLink;
            myLastLink = newLink;
            newLink.setNextElement(newLink);
        }
        logger.debug("added {} to ringbuffer", value);
        mySize += 1;
    }

    /**
     * Remove the first element in the ring buffer and return it.
     * 
     * @return the first value in the buffer
     */
    public Object pop() {
        if( myLastLink != null ) {
            RingBufferLink first = myLastLink.getNextElement();
            // if this is the last element, the buffer is empty
            if(myLastLink.equals(myFirstLink)) {
                myLastLink = null;
                myFirstLink = null;
            }
            // otherwise repair the loop
            else {
                RingBufferLink newFirst = first.getNextElement();
                myLastLink.setNextElement(newFirst);
                myFirstLink = newFirst;
            }
            mySize -= 1;
            logger.debug("removed {} from the ringbuffer", first.getValue());
            return first.getValue();
        }
        else throw new IndexOutOfBoundsException(
                "There are no more elements in the ring buffer.");
    }

    /**
     * Returns an ordered ArrayList starting with the first value and passing
     * through the entire buffer.
     * 
     * @return ordered list of buffer contents
     */
    public ArrayList unwind() {
        ArrayList values = new ArrayList();
        RingBufferLink nextLink = myLastLink;
        do {
            values.add(nextLink.getNextElement().getValue());
            nextLink = nextLink.getNextElement();
        }
        while( !nextLink.getNextElement().equals(myFirstLink) );
        logger.debug("unwind result: {}", values);
        return values;
    }
    
    /**
     * Returns a string containing the toString() contents of the buffer.
     * 
     * @return an ordered list of buffer contents as a String
     */
    @Override
    public String toString() {
        String values = "";
        RingBufferLink nextLink = myLastLink;
        if( nextLink == null ) return values;
        do {
            Object val = nextLink.getNextElement().getValue();
            if( val != null ) {
                values = values.concat(val.toString()).concat("\n");
            }
            else {
                values = values.concat("\n");
            }
            nextLink = nextLink.getNextElement();
        }
        while( !nextLink.getNextElement().equals(myFirstLink) );
        logger.debug("toString result: {}", values);
        return values;
    }
    
    /**
     * @return The number of elements in the buffer
     */
    public int getSize() {
        return mySize;
    }
}

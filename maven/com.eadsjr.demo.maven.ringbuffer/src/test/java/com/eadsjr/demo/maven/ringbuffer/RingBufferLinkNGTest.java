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

import static org.testng.Assert.*;

/**
 *
 * @author eadsjr
 */
public class RingBufferLinkNGTest {
    
    public RingBufferLinkNGTest() {
    }

    @org.testng.annotations.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.testng.annotations.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.testng.annotations.BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @org.testng.annotations.AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getValue method, of class RingBufferLink.
     */
    @org.testng.annotations.Test
    public void testGetValue() {
        System.out.println("getValue");
        Object expResult = "hello";
        RingBufferLink instance = new RingBufferLink(expResult);
        Object result = instance.getValue();
        assertEquals(result, expResult);
    }

    /**
     * Test of setNextElement method, of class RingBufferLink.
     */
    @org.testng.annotations.Test
    public void testSetNextElement() {
        System.out.println("setNextElement");
        RingBufferLink nextElement = new RingBufferLink("blah");
        RingBufferLink expResult = nextElement;
        RingBufferLink instance = new RingBufferLink(7);
        instance.setNextElement(nextElement);
        assertEquals(instance.getNextElement(),expResult);
    }

    /**
     * Test of getNextElement method, of class RingBufferLink.
     */
    @org.testng.annotations.Test
    public void testGetNextElement() {
        System.out.println("getNextElement");
        RingBufferLink instance = new RingBufferLink("blah");
        instance.setNextElement(instance);
        RingBufferLink expResult = instance;
        RingBufferLink result = instance.getNextElement();
        assertEquals(result, expResult);
    }
}

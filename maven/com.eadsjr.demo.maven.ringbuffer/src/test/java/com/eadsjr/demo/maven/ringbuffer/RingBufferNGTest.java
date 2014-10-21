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
import java.util.Arrays;
import java.util.List;
import static org.testng.Assert.*;

/**
 * TODO: use reflection or some other method to isolate the tests.
 *
 * @author eadsjr
 */
public class RingBufferNGTest {
    
    public RingBufferNGTest() {
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
     * Test of add method, of class RingBuffer.
     */
    @org.testng.annotations.Test
    public void testAdd() {
        System.out.println("add");
        List values = Arrays.asList("a,b,c".split(","));
        RingBuffer instance = new RingBuffer();
                
        instance.add(values);
        
        List x = instance.unwind();
        
        int expSize = 3;
        String expResultA = "a";
        String expResultB = "b";
        String expResultC = "c";
                
        assertEquals(x.size(), expSize);
        assertEquals(x.get(0), expResultC);
        assertEquals(x.get(1), expResultB);
        assertEquals(x.get(2), expResultA);
    }

    /**
     * Test of remove method, of class RingBuffer.
     */
    @org.testng.annotations.Test
    public void testRemove() {
        System.out.println("remove");
        int quantity1 = 1;
        RingBuffer instance = new RingBuffer();
        instance.add(Arrays.asList("xyz"));
        List expResult = Arrays.asList("xyz");
        List result = instance.remove(quantity1);
        assertEquals(result, expResult);
        
        int quantity0 = 0;
        instance = new RingBuffer();
        expResult = Arrays.asList();
        result = instance.remove(quantity0);
        assertEquals(result, expResult);
        
        // remove negative
        boolean isHandled = false;
        int quantityNegative1 = -1;
        instance = new RingBuffer();
        expResult = Arrays.asList();
        try {
            result = instance.remove(quantityNegative1);
        }
        catch( IllegalArgumentException e ) {
            isHandled = true;
        }
        assertTrue(isHandled);
        
        //remove to many
        isHandled = false;
        instance = new RingBuffer();
        expResult = Arrays.asList();
        try {
            result = instance.remove(quantity1);
        }
        catch( IndexOutOfBoundsException e ) {
            isHandled = true;
        }
        assertTrue(isHandled);
        
        
    }

    /**
     * Test of push method, of class RingBuffer.
     */
    @org.testng.annotations.Test
    public void testPush() {
        System.out.println("push");
        Object value = "  ";
        RingBuffer instance = new RingBuffer();
        instance.push(value);
        Object result = instance.pop();
        assertEquals(result, value);
    }

    /**
     * Test of pop method, of class RingBuffer.
     */
    @org.testng.annotations.Test
    public void testPop() {
        System.out.println("push");
        Object value = "  ";
        RingBuffer instance = new RingBuffer();
        instance.push(value);
        Object result = instance.pop();
        assertEquals(result, value);
    }

    /**
     * Test of unwind method, of class RingBuffer.
     */
    @org.testng.annotations.Test
    public void testUnwind() {
        System.out.println("unwind");
        RingBuffer instance = new RingBuffer();
        ArrayList values = new ArrayList(
                Arrays.asList("hello,maybe?,I dunno".split(",")));
        instance.add(values);
        List expResult = new ArrayList();
        expResult.add(values.get(2));
        expResult.add(values.get(1));
        expResult.add(values.get(0));
        ArrayList result = instance.unwind();
        assertEquals(result, expResult);
    }

    /**
     * Test of toString method, of class RingBuffer.
     */
    @org.testng.annotations.Test
    public void testToString() {
        System.out.println("toString");
        ArrayList values = new ArrayList(
                Arrays.asList("hello,maybe?,I dunno".split(",")));

        RingBuffer instance = new RingBuffer();
        instance.add(values);
        String expResult = "I dunno\nmaybe?\nhello\n";

        String result = instance.toString();
        assertEquals(result, expResult);
    }

    /**
     * Test of getSize method, of class RingBuffer.
     */
    @org.testng.annotations.Test
    public void testGetSize() {
        System.out.println("getSize");
        RingBuffer instance = new RingBuffer();
        int expResult = 0;
        int result = instance.getSize();
        assertEquals(result, expResult);

        System.out.println("getSize");
        instance = new RingBuffer();
        instance.add(Arrays.asList("a","b","c"));
        expResult = 3;
        result = instance.getSize();
        assertEquals(result, expResult);
    }
    
}

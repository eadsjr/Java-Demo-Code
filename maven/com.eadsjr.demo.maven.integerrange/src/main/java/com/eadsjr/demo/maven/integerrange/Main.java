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

package com.eadsjr.demo.maven.integerrange;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This executes functional tests of the IntegerRange class.
 *
 * @author Jason Randolph Eads <jeads442@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args)  throws Exception {
        
         /**
         * Constructor Tests
         */
        IntegerRange r0 = new IntegerRange((short)-1,(short)1);
        assert(r0.getRangeType() == IntegerRange.RangeType.SHORT);
        
        IntegerRange r1 = new IntegerRange(-1,1);
        assert(r1.getRangeType() == IntegerRange.RangeType.INTEGER);
        
        IntegerRange r2 = new IntegerRange((long)-1, (long)1);
        assert(r2.getRangeType() == IntegerRange.RangeType.LONG);
        
        IntegerRange r3 = new IntegerRange(
                BigInteger.valueOf(-1), BigInteger.valueOf(1));
        assert(r3.getRangeType() == IntegerRange.RangeType.BIGINT);
        
        IntegerRange r4 = new IntegerRange("[2,5)"); //Short
        assert(r4.getRangeType() == IntegerRange.RangeType.SHORT);
        
        IntegerRange r5 = new IntegerRange("[2,555556)"); //Integer
        assert(r5.getRangeType() == IntegerRange.RangeType.INTEGER);
        
        IntegerRange r6 = new IntegerRange("[2,55555666667)"); //Long
        assert(r6.getRangeType() == IntegerRange.RangeType.LONG);
        
        IntegerRange r7 = new IntegerRange("[2,555556666677777888889)"); //BigInteger
        assert(r7.getRangeType() == IntegerRange.RangeType.BIGINT);
        
        IntegerRange IRminus3to5 = new IntegerRange("[-3,5]");
        
        /**
         * Test Contains
         */
        TreeSet set = new TreeSet();
        set.add(Short.valueOf("-1"));
        set.add(Short.valueOf("0"));
        set.add(Short.valueOf("1"));
        assert(IRminus3to5.Contains(set));
        
        set.add(Short.valueOf("-3"));
        set.add(Short.valueOf("5"));
        assert(IRminus3to5.Contains(set));
        
        // does not contain
        set.add(Short.valueOf("6"));
        assert(!IRminus3to5.Contains(set));
        
        set.remove(Short.valueOf("6"));
        set.add(Short.valueOf("-4"));
        assert(!IRminus3to5.Contains(set));
        
        set.remove(Short.valueOf("-4"));
        set.add(Short.MAX_VALUE);
        assert(!IRminus3to5.Contains(set));
        
        set.remove(Short.MAX_VALUE);
        set.add(Short.MIN_VALUE);
        assert(!IRminus3to5.Contains(set));
        
        /**
         * Test getAllPoints
         */
        set.clear();
        set.add(Short.valueOf("-3"));
        set.add(Short.valueOf("-2"));
        set.add(Short.valueOf("-1"));
        set.add(Short.valueOf("0"));
        set.add(Short.valueOf("1"));
        set.add(Short.valueOf("2"));
        set.add(Short.valueOf("3"));
        set.add(Short.valueOf("4"));
        set.add(Short.valueOf("5"));
        
        Set returnSet = IRminus3to5.getAllPoints();
        
        assert( set.containsAll(returnSet) );
        returnSet.removeAll(set);
        assert( returnSet.isEmpty() );
        
        /**
         * Test ContainsRange
         */
        IntegerRange IR3to5 = new IntegerRange("(2,5]");
        assert(IRminus3to5.ContainsRange(IR3to5));
        
        IntegerRange IRminus3tominus2 = new IntegerRange("(-4,-1)");
        assert(IRminus3to5.ContainsRange(IRminus3tominus2));
        
        IntegerRange IRminus1to1 = new IntegerRange("[-1,2)");
        assert(IRminus3to5.ContainsRange(IRminus1to1));
        
        // does not contain
        IntegerRange IRminus5tominus2 = new IntegerRange("[-5,-2]");
        assert(!IRminus3to5.ContainsRange(IRminus5tominus2));

        IntegerRange IR2to6 = new IntegerRange("[2,6]");
        assert(!IRminus3to5.ContainsRange(IR2to6));
        
        IntegerRange IRminus7to9 = new IntegerRange("[-7,9]");
        assert(!IRminus3to5.ContainsRange(IRminus7to9));
        
        /**
         * Test endPoints
         */
        List endpoints = IR2to6.endPoints();
        assert(endpoints.size() == 2);
        assert(endpoints.contains(Short.valueOf("2")));
        assert(endpoints.contains(Short.valueOf("6")));
        
        endpoints = IRminus3to5.endPoints();
        assert(endpoints.size() == 2);
        assert(endpoints.contains(Short.valueOf("-3")));
        assert(endpoints.contains(Short.valueOf("5")));
        
        /**
         * Test OverlapsRange
         */
        assert(IRminus3to5.overlapsRange(IRminus1to1));
        assert(IRminus3to5.overlapsRange(IRminus5tominus2));
        assert(IRminus3to5.overlapsRange(IR2to6));
        assert(IRminus3to5.overlapsRange(IRminus7to9));
        
        // does not overlap
        IntegerRange IRminus7tominus5 = new IntegerRange("[-7,-5]");
        assert(!IRminus3to5.overlapsRange(IRminus7tominus5));
        
        IntegerRange IR7to9 = new IntegerRange("[7,9]");
        assert(!IRminus3to5.overlapsRange(IR7to9));
        
        
        /**
         * Test Equals
         */
        assert(IRminus3to5.Equals(new IntegerRange((short)-3,(short)5)));
        
        System.out.println("IntegerRangeTestComplete");
    }
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This generic implementation of integer range stores its components in roughly
 * the smallest appropriate type. It can accept a range as a string or stored in
 * any of its accepted types. In its current form only IntegerRanges of the same
 * type may be compared in any fashion.
 * 
 * @author Jason Randolph Eads <jeads442@gmail.com>
 * @param <T>
 */
public class IntegerRange<T extends Number & Comparable<T>> {
    /**
     * Represents the lowest value included in the range
     */
    private T lowestValue;
    
    /**
     * Represents the highest value included in the range
     */
    private T highestValue;
    
    public T getLowestValue() { return lowestValue; }    
    public T getHighestValue() { return highestValue; }
    
    public enum RangeType { SHORT, INTEGER, LONG, BIGINT };
    private RangeType rangeType;
    
    private static String MALFORMED_RANGE_STRING = "malformed range string";
    private static String RANGE_STRING_NUMBERS_INVERTED = "range string numbers inverted";
    private static String RANGE_STRING_DEFINES_EMPTY_SET = "range string defines empty set";
    private static String PROVIDED_TYPE_NOT_OF_KNOWN_INTERGER_TYPE = "provided type not of known integer type";
    private static String ENDPOINTS_MUST_BE_OF_SAME_TYPE = "endpoints must be of same type";
    private static String INCOMPATIBLE_ARGUMENT_TYPE = "incompatible argument type";
    
    public RangeType getRangeType() { return rangeType; }

    private RangeType determineTypeFromObject( T theObject ) throws Exception {
        RangeType type;
        switch (theObject.getClass().getName()) {
            case "java.lang.Integer":
                type = RangeType.INTEGER;
                break;
            case "java.lang.Short":
                type = RangeType.SHORT;
                break;
            case "java.lang.Long":
                type = RangeType.LONG;
                break;
            case "java.math.BigInteger":
                type = RangeType.BIGINT;
                break;
            default:
                throw new Exception(PROVIDED_TYPE_NOT_OF_KNOWN_INTERGER_TYPE);
        }
        return type;
    }
    
    /**
     * Constructs the Integer range, using the two endpoints provided.
     * 
     * @param firstpoint
     * @param secondpoint
     * @throws Exception 
     */
    private void construct( T firstpoint, T secondpoint ) throws Exception {
        if( firstpoint.compareTo(secondpoint) > 0 ) {
            lowestValue = secondpoint;
            highestValue = firstpoint;
        }
        else {
            lowestValue = firstpoint;
            highestValue = secondpoint;
        }
    }
    
    /**
     * Creates a range from zero to given value.
     * 
     * @param range endpoint of the range
     */
    public IntegerRange ( T range ) throws Exception {
        rangeType = determineTypeFromObject(range);
        switch(rangeType) {
            case SHORT:
                construct((T)Short.valueOf((short)0), range);
                break;
            case INTEGER:
                construct((T)Integer.valueOf(0), range);
                break;
            case LONG:
                construct((T)Long.valueOf(0), range);
                break;
            case BIGINT:
                construct((T)BigInteger.ZERO, range );
        }
    }

    
    /**
     * Creates a range with the given endpoints, inclusive
     * 
     * @param firstpoint an endpoint of the range
     * @param secondpoint an endpoint of the range 
     * @throws java.lang.Exception 
     * @throws java.lang.IllegalArgumentException
     */
    public IntegerRange( T firstpoint, T secondpoint ) throws IllegalArgumentException, Exception {
       if(!firstpoint.getClass().equals(secondpoint.getClass()))
           throw new IllegalArgumentException(ENDPOINTS_MUST_BE_OF_SAME_TYPE);
       rangeType = determineTypeFromObject(firstpoint);
       construct(firstpoint, secondpoint); 
    }
    
    /**
     * Accepts a string that contains an integer range in mathematical notation.
     * The string is used to determine if each endpoint is inclusive, as denoted
     * by a square bracket, or exclusive as denoted by a parentheses. It also
     * contains the numbers themselves, separated by a comma. The first number
     * should be of lower or equal value to the second.
     * 
     * An example string "[12,19)" would represent all integers from twelve to 
     * nineteen, not including nineteen.
     * 
     * @param range string with math notation example of integer range
     * @throws Exception 
     * @throws IllegalArgumentException 
     */
    public IntegerRange( String range ) throws IllegalArgumentException, Exception {
        // minimal size check
        if( range.length() < 5 )
            throw new IllegalArgumentException(MALFORMED_RANGE_STRING);

        // check lowest point inclusiveness
        boolean lowestValueInclusive;
        if( range.charAt(0) == '[' )
            lowestValueInclusive = true;
        else if ( range.charAt(0) == '(' )
            lowestValueInclusive = false;
        else throw new IllegalArgumentException(MALFORMED_RANGE_STRING);

        // check highest point inclusiveness
        boolean highestValueInclusive;
        if( range.charAt(range.length()-1) == ']' )
            highestValueInclusive = true;
        else if ( range.charAt(range.length()-1) == ')' )
            highestValueInclusive = false;
        else throw new IllegalArgumentException(MALFORMED_RANGE_STRING);

        // collect endpoint numbers
        try {
            String [] rangeNumbers = range.split(",");
            String firstRangeNumberString = rangeNumbers[0].substring(1);
            String secondRangeNumberString = rangeNumbers[1].substring(0, rangeNumbers[1].length()-1);
            
            // Get longest string's length to determine appropriate integer
            int longestRangeNumberString;
            if(firstRangeNumberString.length() < secondRangeNumberString.length())
                longestRangeNumberString = secondRangeNumberString.length();
            else longestRangeNumberString = firstRangeNumberString.length();
            
            /**
             * At most, how many characters represent each type? ...
             * 
             * 6 max represent a short integer @ -32,768
             * 11 max represent an integer @ -2,147,483,648
             * 20 max represent a long integer @ -9,223,372,036,854,775,808
             * More than twenty characters are BigInteger only
             * 
             * With this in mind there is overlap, 99999 will not fit in a
             * short integer. Clipping two down from the max, one for the minus
             * sign and one for the largest digit, will ensure the value can be
             * stored in the selected type.
             * 
             * More code would be needed to more precisely choose types, but
             * would be largely unnecessary and wasteful in this case.
             */
            if( longestRangeNumberString > 20 ) rangeType = RangeType.BIGINT;
            else if( longestRangeNumberString > 9 ) rangeType = RangeType.LONG;
            else if( longestRangeNumberString > 4 ) rangeType = RangeType.INTEGER;
            else rangeType = RangeType.SHORT;
            
            T firstRangeNumber;
            T secondRangeNumber;
            switch(rangeType) {
            case SHORT:
                firstRangeNumber = (T)Short.valueOf(firstRangeNumberString);
                secondRangeNumber = (T)Short.valueOf(secondRangeNumberString);
                break;
            case INTEGER:
                firstRangeNumber = (T)Integer.valueOf(firstRangeNumberString);
                secondRangeNumber = (T)Integer.valueOf(secondRangeNumberString);
                break;
            case LONG:
                firstRangeNumber = (T)Long.valueOf(firstRangeNumberString);
                secondRangeNumber = (T)Long.valueOf(secondRangeNumberString);
                break;
            case BIGINT:
                firstRangeNumber = (T)(new BigInteger(firstRangeNumberString));
                secondRangeNumber = (T)(new BigInteger(secondRangeNumberString));
                break;
            default:
                throw new Exception(PROVIDED_TYPE_NOT_OF_KNOWN_INTERGER_TYPE);
            }

            if( firstRangeNumber.compareTo(secondRangeNumber) > 0)
                throw new IllegalArgumentException(RANGE_STRING_NUMBERS_INVERTED);
            else {
                lowestValue = firstRangeNumber;
                highestValue = secondRangeNumber;
            }
        }
        catch ( NumberFormatException e ) { throw new IllegalArgumentException(MALFORMED_RANGE_STRING); }
        
        // adjust for inclusiveness at ends, abort if empty set results
        switch(rangeType) {
            case SHORT:
                if( !lowestValueInclusive )
                    lowestValue = (T)Short.valueOf((short)(lowestValue.shortValue() + (short)1));
                if( !highestValueInclusive )
                    highestValue = (T)Short.valueOf((short)(highestValue.shortValue() - (short)1));
                if( lowestValue.shortValue() > highestValue.shortValue() )
                    throw new IllegalArgumentException(RANGE_STRING_DEFINES_EMPTY_SET);
                break;
            case INTEGER:
                if( !lowestValueInclusive )
                    lowestValue = (T)Integer.valueOf(lowestValue.intValue() + 1);
                if( !highestValueInclusive )
                    highestValue = (T)Integer.valueOf(highestValue.intValue() - 1);
                if( lowestValue.intValue()> highestValue.intValue() )
                    throw new IllegalArgumentException(RANGE_STRING_DEFINES_EMPTY_SET);
                break;
            case LONG:
                if( !lowestValueInclusive )
                    lowestValue = (T)Long.valueOf(lowestValue.longValue() + 1);
                if( !highestValueInclusive )
                    highestValue = (T)Long.valueOf(highestValue.longValue() - 1);
                if( lowestValue.longValue()> highestValue.longValue() )
                    throw new IllegalArgumentException(RANGE_STRING_DEFINES_EMPTY_SET);
                break;
            case BIGINT:
                if( !lowestValueInclusive )
                    lowestValue = (T)((BigInteger)lowestValue).add(BigInteger.ONE);
                if( !highestValueInclusive )
                    highestValue = (T)((BigInteger)highestValue).subtract(BigInteger.ONE);
                if( lowestValue.compareTo(highestValue) > 0 )
                    throw new IllegalArgumentException(RANGE_STRING_DEFINES_EMPTY_SET);
                break;
            default:
                throw new Exception(PROVIDED_TYPE_NOT_OF_KNOWN_INTERGER_TYPE);
        }
        // Does not require call to construct()
    }
    
    /**
     * Determines if the elements of the provided set are all within the bounds
     * of the range.
     * 
     * @param set a set of values to check, must be same type
     * @return returns true is set is fully contained in range
     */
    public boolean Contains( Set<T> set ) {
        for( T item: set ) {
            if( lowestValue.compareTo(item) > 0 ) return false;
            if( item.compareTo(highestValue) > 0 ) return false;
        }
        return true;
    }
    
    /**
     * Determines if the the provided range is completely within the bounds of
     * this range.
     * 
     * @param range another integer range of the same type
     * @return returns true if range is fully contained
     * @throws java.lang.IllegalArgumentException
     */
    public boolean ContainsRange( IntegerRange<T> range ) throws IllegalArgumentException {
        if( range.rangeType != rangeType )
            throw new IllegalArgumentException(INCOMPATIBLE_ARGUMENT_TYPE);
        if( range.highestValue.compareTo(highestValue) > 0 ) return false;
        if( lowestValue.compareTo(range.lowestValue) > 0 ) return false;
        return true;
    }
    
    /**
     * Returns a list of all the points contained in the range. Not recommended
     * for use with BigIntegers.
     * 
     * @return a set of all contained points
     */
    public Set getAllPoints() throws Exception {
        TreeSet points = new TreeSet();
        T countIndex = lowestValue;
        while( countIndex.compareTo(highestValue) <= 0 ) {
            points.add(countIndex);
            switch(rangeType) {
                case SHORT:
                    countIndex = (T)Short.valueOf((short)(countIndex.shortValue() + (short)1));
                    break;
                case INTEGER:
                    countIndex = (T)Integer.valueOf(countIndex.intValue() + 1);
                    break;
                case LONG:
                    countIndex = (T)Long.valueOf(countIndex.longValue() + 1);
                    break;
                case BIGINT:
                    countIndex = (T)((BigInteger)countIndex).add(BigInteger.ONE);
                    break;
                default:
                    throw new Exception(PROVIDED_TYPE_NOT_OF_KNOWN_INTERGER_TYPE);
            }
        }
        return points;
    }
    
    /**
     * Returns the endpoints of the range.
     * 
     * @return List containing the range endpoints
     */
    public List<T> endPoints() {
        ArrayList<T> pair = new ArrayList<>(2);
        pair.add(lowestValue);
        pair.add(highestValue);
        return pair;
    }
    
    /**
     * Determines whether the ranges overlap at any point.
     * 
     * @param range Another range of the same type
     * @return true if any point is shared between the ranges
     */
    public boolean overlapsRange( IntegerRange<T> range ) throws IllegalArgumentException {
        if( range.rangeType != rangeType )
            throw new IllegalArgumentException(INCOMPATIBLE_ARGUMENT_TYPE);
        if( lowestValue.compareTo(range.getHighestValue()) > 0 ) return false;
        if( range.getLowestValue().compareTo(highestValue) > 0 ) return false;
        return true;
    }
    
    /**
     * Determines if two ranges represent the same range
     * 
     * @param range another range of the same type
     * @return true if the ranges are equivalent 
     */
    public boolean Equals( IntegerRange<T> range ) throws IllegalArgumentException {
        if( range.rangeType != rangeType )
            throw new IllegalArgumentException(INCOMPATIBLE_ARGUMENT_TYPE);
        if( range.getHighestValue().equals(highestValue) &&
            range.getLowestValue().equals(lowestValue) )
            return true;
        else return false;
    }
}
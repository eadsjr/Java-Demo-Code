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

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * This program parses a file and operates a RingBuffer in response.
 *
 * @author Jason Randolph Eads <jeads442@gmail.com>
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    
    static RingBuffer buffer = new RingBuffer();
    static int lineCount = 0; // for error messages only

    /**
     * @param args the command line arguments: input filename
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // File Args Quick ref: 
        //   http://www.cs.swarthmore.edu/~newhall/unixhelp/Java_files.html
        // first check to see if the program was run with the command line
        //   argument
        if(args.length < 1) {
            System.err.println("Error, usage: java ClassName inputfile");
            System.exit(1);
        }

        Scanner reader = new Scanner(new FileInputStream(args[0]));
        boolean isQuitting = false;
        while( reader.hasNextLine() ) {
            String command = reader.nextLine();
            // Handle the 'Add' case
            if( command.startsWith("A") ) {
                lineCount += caseA(reader, command);
                logger.info("Executed add");
            }
            // Handle the 'Remove' case
            else if ( command.startsWith("R")) {
                caseR(command);
                lineCount++;
                logger.info("Executed remove");
            }
            // List the buffer contents
            else if ( command.startsWith("L")) {
                System.out.println(buffer.toString());
                lineCount++;
                logger.info("Executed list");
            }
            // Exit the program
            else if ( command.startsWith("Q")) {
                isQuitting = true;
                lineCount++;
                logger.info("Executing quit...");
                break;
            }
            else {
                throw new InvalidInputException(
                    String.format(
                        "Expected a command character (A,R,L or Q) @ line %d!" +
                                " Malformed input file.",
                        lineCount));
            }
        }
        if(!isQuitting)
            throw new InvalidInputException(
                    "reached end of file without recieving termination signal");
    }

    /**
     * Removes several elements from buffer.
     * 
     * @param reader The input file scanner
     * @throws com.eadsjr.demo.maven.ringbuffer.Main.InvalidInputException 
     */
    private static void caseR(String command)
            throws InvalidInputException {
        try {
            int count = Integer.parseInt(command.substring(1).trim());
            buffer.remove(count);
        }
        catch(NumberFormatException e) {
            throw new InvalidInputException(
                String.format(
                        "Expected a number value @ line %d!" +
                                " Malformed input file.",
                        lineCount),
                e);
        }
    }

    /**
     * Extracts several lines to insert into buffer.
     * 
     * @param reader The input file scanner
     * @return the number of lines consumed
     * @throws com.eadsjr.demo.maven.ringbuffer.Main.InvalidInputException 
     */
    private static int caseA(Scanner reader, String command)
            throws InvalidInputException {
        
        try {
            int count = Integer.parseInt(command.substring(1).trim());
        
            ArrayList<String> values = new ArrayList<String>();
            for(int i = 0; i < count; i++) {
                if(!reader.hasNextLine()) throw new InvalidInputException(
                    String.format(
                        "Expected a value @ line %d!" +
                                    " Malformed input file.",
                            lineCount));
                values.add(reader.nextLine());
            }
            buffer.add(values);
            return count + 1;            
        }
        catch(NumberFormatException e) {
            throw new InvalidInputException(
                String.format(
                        "Expected a number value @ line %d!" +
                                " Malformed input file.",
                        lineCount),
                e);
        }
    }

    /**
     * This exception describes a bad input from the user.
     */
    public static class InvalidInputException extends Exception {
        public InvalidInputException() {
            super();
        }
        public InvalidInputException(String msg) {
            super(msg);
        }        
        public InvalidInputException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}

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
package com.eadsjr.demo.maven.stringplay;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Program that reverses English words in string passed in by Terminal argument.
 * 
 * Only the words are reversed, not the whole String, punctuation or numbers.
 *
 * @author Jason Randolph Eads <jeads442@gmail.com>
 */
public class WordReversal {

    /**
     * Feeds input into function and prints output
     *
     * @param args the command line arguments, first one after name is input
     */
    public static void main(String[] args) {

        //String input = "Hello World!!! How are things?";
        String input = args[0];
        
        System.out.println(WordReversal.reverseWordsInString(input));
    }

    /**
     * Reverses each word in a String of English characters.
     *
     * @param input The String whose words will be reversed.
     * @return The String with each word reversed.
     */
    public static String reverseWordsInString(String input) {

        // This is used to determine if the character is alpha numeric
        Predicate<String> alphaPatternPredicate
                = Pattern.compile("[a-zA-Z]").asPredicate();

        // This stores a word, which will be reversed.
        StringBuilder wordStringBuilder = new StringBuilder();

        // This stores the ultimate output
        StringBuilder newStringBuilder = new StringBuilder();

        // This flag tells if you are currently collecting a word
        boolean isInWord = false;

        // For each character...
        for (int i = 0; i < input.length(); i++) {

            // Coerece character to String
            String Char = input.charAt(i) + "";

            // If in word...
            if (isInWord) {
                // add letters to word.
                if (alphaPatternPredicate.test(Char)) {
                    wordStringBuilder.append(Char);
                } // OR terminate word, reverse it, append it,
                // then add the new character directly.
                else {
                    isInWord = false;
                    newStringBuilder.append(wordStringBuilder.reverse().toString());
                    wordStringBuilder = new StringBuilder();
                    newStringBuilder.append(Char);
                }
            } // Else if not in word
            else {
                // Initiate a new word and add the letter...
                if (alphaPatternPredicate.test(Char)) {
                    isInWord = true;
                    wordStringBuilder.append(Char);
                } // OR add a non-letter character directly
                else {
                    newStringBuilder.append(Char);
                }
            }
        }

        // Finish up if ending on a word.
        if (isInWord) {
            newStringBuilder.append(wordStringBuilder.reverse().toString());
        }

        return newStringBuilder.toString();
    }
}

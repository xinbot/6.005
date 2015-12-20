package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class DigitsToStringConverterTest {
    @Test
    public void basicNumberSerializerTest() {
        // Input is a 4 digit number, 0.123 represented in base 4
        int[] input = {0, 1, 2, 3};
        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet = {'d', 'c', 'b', 'a'};

        String expectedOutput = "dcba";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));
    }

    @Test
    public void NumberSerializerTest() {
    	// digits[i] >= base
        int[] input = {0, 1, 5, 3};
        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet = {'d', 'c', 'b', 'a'};

        String expectedOutput = null;
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));
    
        // digits[i] < 0
        input = new int[]{0, 1, 5, -3};
        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        alphabet = new char[]{'d', 'c', 'b', 'a'};

        expectedOutput = null;
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));
        
        //alphabet.length != base
        input = new int[]{0, 1, 5, -3};
        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        alphabet = new char[]{'f', 'e', 'd', 'c', 'b', 'a'};

        expectedOutput = null;
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));
        
        // Input is a 8 digit number, 0.1101101 represented in base 2
        input = new int[]{0, 1, 1, 0, 1, 1, 0, 1};
        // Want to map 0 -> "b", 1 -> "a"
        alphabet = new char[]{'b', 'a'};
        
        expectedOutput = "baabaaba";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 2, alphabet));
        
        
        // Input is a 8 digit number, 5.7431323 represented in base 8
        input = new int[]{5, 7, 4, 3, 1, 3, 2, 3};
        // Want to map 0 -> "h", 1 -> "g", 2->"f", 3->"e", 4->"d", 5->"c", 6->"b", 7->"a"
        alphabet = new char[]{'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};
        
        expectedOutput = "cadegefe";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 8, alphabet));
        
        
        // Input is a 11 digit number, F.7E69A23689 represented in base 8
        input = new int[]{15, 7, 14, 6, 9, 10, 2, 3, 6, 8, 9};
        // Want to map 0 -> "p", 1 -> "o", 2->"n", 3->"m", 4->"l", 5->"k", 6->"j", 7->"i"
        //			   8 -> "h", 9 -> "g", 10 -> "f", 11 -> "e", 12 -> "d", 13 -> "c", 14 -> "b", 15 -> "a"			   
        alphabet = new char[]{'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};
        
        expectedOutput = "aibjgfnmjhg";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 16, alphabet));
         
    }
}

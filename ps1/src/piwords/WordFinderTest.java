package piwords;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class WordFinderTest {
    @Test
    public void basicGetSubstringsTest() {
        String haystack = "abcde";
        String[] needles = {"ab", "abc", "de", "fg"};

        Map<String, Integer> expectedOutput = new HashMap<String, Integer>();
        expectedOutput.put("ab", 0);
        expectedOutput.put("abc", 0);
        expectedOutput.put("de", 3);

        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack,
                                                              needles));
    }

    @Test
    public void GetSubstringsTest() {
    	//case 1
        String haystack = "ashdjkasdh";
        String[] needles = {"ash", "hdjk", "dj", "kas", "asdh", "jka"};

        Map<String, Integer> expectedOutput = new HashMap<String, Integer>();
        expectedOutput.put("ash", 0);
        expectedOutput.put("hdjk", 2);
        expectedOutput.put("dj", 3);
        expectedOutput.put("kas", 5);
        expectedOutput.put("asdh", 6);
        expectedOutput.put("jka", 4);

        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack,
                                                              needles));
        
        //case 2
        haystack = "lkhgjothbsdjhajbnb";
        needles = new String[]{"hgj", "th", "djha", "nb", "lkh", "bsd", "jo", "oth"};

        expectedOutput = new HashMap<String, Integer>();
        expectedOutput.put("hgj", 2);
        expectedOutput.put("th", 6);
        expectedOutput.put("djha", 10);
        expectedOutput.put("nb", 16);
        expectedOutput.put("lkh", 0);
        expectedOutput.put("bsd", 8);
        expectedOutput.put("jo", 4);
        expectedOutput.put("oth", 5);

        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack,
                                                              needles));
        
    }
}

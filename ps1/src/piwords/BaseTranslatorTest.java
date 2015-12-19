package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseTranslatorTest {
    @Test
    public void basicBaseTranslatorTest() {
        // Expect that .01 in base-2 is .25 in base-10
        // (0 * 1/2^1 + 1 * 1/2^2 = .25)
        int[] input = {0, 1};
        int[] expectedOutput = {2, 5};
        assertArrayEquals(expectedOutput,
                          BaseTranslator.convertBase(input, 2, 10, 2));
    }

    @Test
    public void BaseTranslatorTests() {
    	//baseA < 2
    	int[] input = new int[]{0, 1, 0, 1, 1};
    	int[] expectedOutput = null;
    	assertArrayEquals(expectedOutput,
                BaseTranslator.convertBase(input, 1, 10, 5));
    	
    	//one of the digits < 0
    	input = new int[]{0, -1, 0, 1, 1};
    	expectedOutput = null;
    	assertArrayEquals(expectedOutput,
                BaseTranslator.convertBase(input, 2, 10, 5));
    	
    	//one of the digits >= baseA
    	input = new int[]{0, 1, 2, 1, 1};
    	expectedOutput = null;
    	assertArrayEquals(expectedOutput,
                BaseTranslator.convertBase(input, 2, 10, 5));
    	
    	// Expect that .01011 in base-2 is .34375 in base-10
    	input = new int[]{0, 1, 0, 1, 1};
        expectedOutput = new int[]{3, 4, 3, 7, 5};
        assertArrayEquals(expectedOutput,
               BaseTranslator.convertBase(input, 2, 10, 5));
        
        // Expect that .A31 in base-16 is .5061 in base-8
        input = new int[]{10, 3, 1};
        expectedOutput = new int[]{5, 0, 6, 1};
        assertArrayEquals(expectedOutput,
                BaseTranslator.convertBase(input, 16, 8, 4));
        
        
     // Expect that .3673 in base-8 is .7BB in base-16
        input = new int[]{3, 6, 7, 3};
        expectedOutput = new int[]{7, 11, 11};
        assertArrayEquals(expectedOutput,
                BaseTranslator.convertBase(input, 8, 16, 3));
        
     // Expect that .764 in base-8 is .1111101 in base-2
        input = new int[]{7, 6, 4};
        expectedOutput = new int[]{1, 1, 1, 1, 1, 0, 1};
        assertArrayEquals(expectedOutput,
                BaseTranslator.convertBase(input, 8, 2, 7));
    }
}

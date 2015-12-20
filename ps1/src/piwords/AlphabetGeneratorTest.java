package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlphabetGeneratorTest {
    @Test
    public void generateFrequencyAlphabetTest() {
        // Expect in the training data that Pr(a) = 2/5, Pr(b) = 2/5,
        // Pr(c) = 1/5.
        String[] trainingData = {"aa", "bbc"};
        // In the output for base 10, they should be in the same proportion.
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        10, trainingData));
        
        // Expect in the training data that Pr(a) = 11/22, Pr(b) = 11/22.
        trainingData = new String[]{"aaaa", "bbbb", "aabaabbb", "abbaab"};
        // In the output for base 10, they should be in the same proportion.
        expectedOutput = null;
        //base < 0
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        -2, trainingData));
        
        // Expect in the training data that Pr(a) = 11/22, Pr(b) = 11/22.
        trainingData = new String[]{"aaaa", "bbbb", "aabaabbb", "abbaab"};
        // In the output for base 10, they should be in the same proportion.
        expectedOutput = new char[]{};
        //base = 0
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        0, trainingData));
        
        
        // Expect in the training data that Pr(a) = 11/22, Pr(b) = 11/22.
        trainingData = new String[]{"aaaa", "bbbb", "aabaabbb", "abbaab"};
        // In the output for base 10, they should be in the same proportion.
        expectedOutput = new char[]{'a', 'b'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        2, trainingData));
        
        
        // Expect in the training data that Pr(a) = 4/12, Pr(b) = 2/12, Pr(c) = 6/12.
        trainingData = new String[]{"aabc", "cccc", "abac"};
        // In the output for base 10, they should be in the same proportion.
        expectedOutput = new char[]{'a', 'a', 
        							'b', 
        							'c', 'c', 'c'};
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        6, trainingData));
        
    }

}

package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiGeneratorTest {
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
    }
    
    @Test
    public void PowerModTest() {
        // 2^4 mod 16 = 0
        assertEquals(0, PiGenerator.powerMod(2, 4, 16));
        
        assertEquals(-1, PiGenerator.powerMod(-2, 4, 16));
        
        assertEquals(-1, PiGenerator.powerMod(2, -4, 16));
        
        assertEquals(-1, PiGenerator.powerMod(2, 4, -16));
        
        // 16^4 mod 27 = 7
        assertEquals(7, PiGenerator.powerMod(16, 4, 27));
        
        // 3^7 mod 6 = 3
        assertEquals(3, PiGenerator.powerMod(3, 7, 6));
        
        assertEquals(1, PiGenerator.powerMod(3, 0, 6));
        
    }

    @Test
    public void computePiInHexTest(){
    	//3.243F6A8885A308D313198A2E037073
    	//assign negative value
    	assertEquals(null, PiGenerator.computePiInHex(-1));
    	
    	int[] expectedOutput = new int[]{2};
    	assertArrayEquals(expectedOutput, PiGenerator.computePiInHex(1));
    	
    	expectedOutput = new int[]{2, 4, 3, 15};
    	assertArrayEquals(expectedOutput, PiGenerator.computePiInHex(4));
    	
    	expectedOutput = new int[]{2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3, 0};
    	assertArrayEquals(expectedOutput, PiGenerator.computePiInHex(13));
    	
    	expectedOutput = new int[]{2, 4, 3, 15, 6, 10, 8};
    	assertArrayEquals(expectedOutput, PiGenerator.computePiInHex(7));
    	
    }
    
}

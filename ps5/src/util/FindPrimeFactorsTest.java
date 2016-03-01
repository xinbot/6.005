package util;

import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindPrimeFactorsTest {
	private final static int PRIME_CERTAINTY = 10;
	
	@Test
    public void findPrimeFactorsTest(){
		//case one
		BigInteger N = BigInteger.valueOf(85);
		BigInteger low = BigInteger.valueOf(2);
		BigInteger hi = BigInteger.valueOf(17);
		
		BigInteger[] expected = new BigInteger[]{
				BigInteger.valueOf(5), BigInteger.valueOf(17)
		};
		
		BigInteger[] actual = BigMath.findPrimeFactors(N, low, hi, PRIME_CERTAINTY);
		
		assertArrayEquals(expected, actual);
		
		//case two
		hi = BigInteger.valueOf(16);
		expected = new BigInteger[]{
				BigInteger.valueOf(5)
		};
		
		actual = BigMath.findPrimeFactors(N, low, hi, PRIME_CERTAINTY);
		
		assertArrayEquals(expected, actual);
		
		//case three
		hi = BigInteger.valueOf(4);
		expected = new BigInteger[]{};
		
		actual = BigMath.findPrimeFactors(N, low, hi, PRIME_CERTAINTY);
		
		assertArrayEquals(expected, actual);
		
		//case four
		N = BigInteger.valueOf(264);
		hi = BigInteger.valueOf(17);
		expected = new BigInteger[]{
				BigInteger.valueOf(2),
				BigInteger.valueOf(2),
				BigInteger.valueOf(2),
				BigInteger.valueOf(3),
				BigInteger.valueOf(11),
		};
		
		actual = BigMath.findPrimeFactors(N, low, hi, PRIME_CERTAINTY);
		
		assertArrayEquals(expected, actual);
		
    }
	
}

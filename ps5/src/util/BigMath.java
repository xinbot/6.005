package util;

import java.math.BigInteger;
import java.util.ArrayList;

public class BigMath {

    /**
     * Given a BigInteger input n, where n >= 0, returns the largest BigInteger r such that r*r <= n.
     * 
     * For n < 0, returns 0.
     * 
     * 
     * details: http://faruk.akgul.org/blog/javas-missing-algorithm-biginteger-sqrt
     * 
     * @param n BigInteger input.
     * @return for n >= 0: largest BigInteger r such that r*r <= n.
     *             n <  0: BigInteger 0
     */
    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
          BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
          if (mid.multiply(mid).compareTo(n) > 0) 
              b = mid.subtract(BigInteger.ONE);
          else 
              a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }
    
    /**
     * Find all the prime factors of an arbitrarily large integer N (N >= 2),
     * @param N BigInteger N the number to be factored in to primes, Requires 2 <= N 
     * @param low BigInteger low the lower bound of prime factors. Requires 1 <= low <= hi
     * @param hi BigInteger hi the upper bound of prime factors.  Requires 1 <= low <= hi
     * 
     * @return an array of prime factors for N
     * */
    public static BigInteger[] findPrimeFactors(BigInteger N, BigInteger low, BigInteger hi, int certainty){
    	ArrayList<BigInteger> result = new ArrayList<BigInteger>();
    	
    	BigInteger x = low;
    	while(x.compareTo(hi) <= 0){
    		if(x.isProbablePrime(certainty)){
    			while(N.remainder(x).compareTo(BigInteger.ZERO) == 0){
    				result.add(x);
    				N = N.divide(x);
    			}
    		}
    		x = x.add(BigInteger.ONE);
		}

    	return result.toArray(new BigInteger[0]);
    }
}

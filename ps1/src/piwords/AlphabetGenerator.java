package piwords;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class AlphabetGenerator {
    /**
     * Given a numeric base, return a char[] that maps every digit that is
     * representable in that base to a lower-case char.
     * 
     * This method will try to weight each character of the alphabet
     * proportional to their occurrence in words in a training set.
     * 
     * This method should do the following to generate an alphabet:
     *   1. Count the occurrence of each character a-z in trainingData.
     *   2. Compute the probability of each character a-z by taking
     *      (occurrence / total_num_characters).
     *   3. The output generated in step (2) is a PDF of the characters in the
     *      training set. Convert this PDF into a CDF for each character.
     *   4. Multiply the CDF value of each character by the base we are
     *      converting into.
     *   5. For each index 0 <= i < base,
     *      output[i] = (the first character whose CDF * base is > i)
     * 
     * A concrete example:
     * 	 0. Input = {"aaaaa..." (302 "a"s), "bbbbb..." (500 "b"s),
     *               "ccccc..." (198 "c"s)}, base = 93
     *   1. Count(a) = 302, Count(b) = 500, Count(c) = 193
     *   2. Pr(a) = 302 / 1000 = .302, Pr(b) = 500 / 1000 = .5,
     *      Pr(c) = 198 / 1000 = .198
     *   3. CDF(a) = .302, CDF(b) = .802, CDF(c) = 1
     *   4. CDF(a) * base = 28.086, CDF(b) * base = 74.586, CDF(c) * base = 93
     *   5. Output = {"a", "a", ... (28 As, indexes 0-27),
     *                "b", "b", ... (47 Bs, indexes 28-74),
     *                "c", "c", ... (18 Cs, indexes 75-92)}
     * 
     * The letters should occur in lexicographically ascending order in the
     * returned array.
     *   - {"a", "b", "c", "c", "d"} is a valid output.
     *   - {"b", "c", "c", "d", "a"} is not.
     *   
     * If base >= 0, the returned array should have length equal to the size of
     * the base.
     * 
     * If base < 0, return null.
     * 
     * If a String of trainingData has any characters outside the range a-z,
     * ignore those characters and continue.
     * 
     * @param base A numeric base to get an alphabet for.
     * @param trainingData The training data from which to generate frequency
     *                     counts. This array is not mutated.
     * @return A char[] that maps every digit of the base to a char that the
     *         digit should be translated into.
     */
    public static char[] generateFrequencyAlphabet(int base,
                                                   String[] trainingData) {
        
    	if(base < 0)
    		return null;
    	
    	char[] result = new char[base];
    	
    	TreeMap<Character, Integer> occurrence = new TreeMap<Character, Integer>(new Comparator<Character>() {
    		//specify the compare method
			@Override
			public int compare(Character c1, Character c2) {
				return c1.compareTo(c2);
			}
    	});
    	
    	int total = 0; 
    	
    	for (int i = 0; i < trainingData.length; i++) {
			
    		for (int j = 0; j < trainingData[i].length(); j++) {
				
    			char character = trainingData[i].charAt(j);
    			
    			boolean isLetter = Character.isLetter(trainingData[i].charAt(j));
    			
    			if(!isLetter)
    				continue; // characters outside the range a-z, ignore it
    			else{
    				total++;
    				
    				if(occurrence.containsKey(character)){
    					int newValue = occurrence.get(character) + 1; // increase the occurrence by 1
    					occurrence.put(character, newValue); 
    				}else{
    					occurrence.put(character, 1); // initial the occurrence to 1
    				}
    			}
			}
		}
    	
    	//calculate the character occurrence probability
    	TreeMap<Character, Double> probability = new TreeMap<Character, Double>();
    	
    	double prior = 0;
    	
    	for(Map.Entry<Character, Integer> entry : occurrence.entrySet()){
    		
    		Character c = entry.getKey();
    		
    		double pro = (double) entry.getValue().intValue() / total;

    		probability.put(c, pro + prior);
    		
    		prior = pro + prior;
    	}
    	
    	int k = 0;
    	for(Map.Entry<Character, Double> entry : probability.entrySet()){
    		
    		int limit = (int) (Math.round(entry.getValue() * base)-1); //index width
    		
    		while ((k <= limit) && (k < base)) { //truncate those words outside base 
    			result[k] = entry.getKey();
    			k++;
    		}
    	}
    	
        return result;
    }
}

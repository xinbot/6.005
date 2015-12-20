package piwords;

import java.util.HashMap;
import java.util.Map;

public class WordFinder {
    /**
     * Given a String (the haystack) and an array of Strings (the needles),
     * return a Map<String, Integer>, where keys in the map correspond to
     * elements of needles that were found as substrings of haystack, and the
     * value for each key is the lowest index of haystack at which that needle
     * was found. A needle that was not found in the haystack should not be
     * returned in the output map.
     * 
     * @param haystack The string to search into.
     * @param needles The array of strings to search for. This array is not
     *                mutated.
     * @return The list of needles that were found in the haystack.
     */
    public static Map<String, Integer> getSubstrings(String haystack,
                                                     String[] needles) { 
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        
        StringBuffer substring;
        
        String temp;
        
        for (int i = 0; i < haystack.length(); i++) {
			
        	substring = new StringBuffer();
        	
        	for (int j = i; j < haystack.length(); j++) {
        		
				substring.append(haystack.charAt(j));
				
				temp = substring.toString();
				
				if(contains(temp,needles))
					result.put(temp, i);
			}
		}
        
        return result;
    }
    
    public static boolean contains(String v, String[] s){
    	
    	for (int i = 0; i < s.length; i++) {
			if(s[i].equals(v))
				return true;
		}
    	
    	return false;
    }
}


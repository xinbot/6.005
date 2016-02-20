package sudoku;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import sudoku.Sudoku.ParseException;


public class SudokuTest {
    

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

	@Test
    public void SudokuValidEmptyTableTest(){
    	//test empty table
    	Sudoku testTable = new Sudoku(3);
    	System.out.println(testTable);
    }
    
    @Test
    public void SudokuInvalidEmptyTableTest(){
    	Sudoku testTable = new Sudoku(0);
    	System.out.println(testTable);
    	
    	testTable = new Sudoku(-2);
    	System.out.println(testTable);
    }
        
    @Test
    public void SudokuInvalidColumnTest(){
    	//invalid column
    	Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 3, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 2, 0, 3, 0 },
                new int[] { 0, 0, 0, 0 },
        });
    }
    
    @Test
    public void SudokuInvalidRowTest(){
    	//invalid row
    	Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 3, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 2, 0, 2, 0 },
                new int[] { 0, 0, 0, 0 },
        });
    }
    
    @Test
    public void SudokuInvalidValueTest(){
    	//invalid value
//    	Sudoku testTable = new Sudoku(2, new int[][] {
//                new int[] { 0, 1, 3, 4 },
//                new int[] { 0, 0, 0, 0 },
//                new int[] { 2, 0, 0, 0 },
//                new int[] { 0, 0, -2, 0 },
//        });
    	
    	Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 3, 6 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 2, 0, 0, 0 },
                new int[] { 0, 0, 0, 0 },
        });
    }
    
    @Test
    public void SudokuInvalidSubRegionTest(){
    	//invalid subregion
    	Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 3, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 2, 0, 0, 0 },
                new int[] { 0, 2, 0, 0 },
        });
    }
    
    @Test
    public void SudokuValidTest(){
    	Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 3, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 2, 0, 0, 0 },
                new int[] { 0, 0, 0, 0 },
        });
    	System.out.println(testTable);
    }
    
    @Test
    public void SudokuFromFileTest() throws IOException, ParseException{
    	
    	Sudoku sudoku = Sudoku.fromFile(2, "samples"+ File.separator +"sudoku_4x4.txt");
    	String expected = ".234\n"
		    			+ "341.\n"
		    			+ "214.\n"
		    			+ ".321\n";
    	assertEquals(expected, sudoku.toString());
    	
    	sudoku = Sudoku.fromFile(3, "samples"+ File.separator +"sudoku_easy.txt");
    	expected = "2..1.5..3\n"
				  +".54...71.\n"
				  +".1.2.3.8.\n"
				  +"6.28.73.4\n"
				  +".........\n"
				  +"1.53.98.6\n"
				  +".2.7.1.6.\n"
				  +".81...24.\n"
				  +"7..4.2..1\n";
    	assertEquals(expected, sudoku.toString());
    	
    	sudoku = Sudoku.fromFile(3, "samples"+ File.separator +"sudoku_easy2.txt");
    	expected ="713254968\n"
    			+ "9623875.1\n"
    			+ "84.961723\n"
    			+ "358612497\n"
    			+ "621479835\n"
    			+ "479538612\n"
    			+ "59472.186\n"
    			+ "28.196354\n"
    			+ "1368452.9\n";
    	assertEquals(expected, sudoku.toString());
    	
    	sudoku = Sudoku.fromFile(3, "samples"+ File.separator +"sudoku_evil.txt");
    	expected ="8.6.2....\n"
    			+ "74...3..8\n"
    			+ "....5..3.\n"
    			+ "5..4..8..\n"
    			+ "6.......7\n"
    			+ "..7..2..1\n"
    			+ ".7..6....\n"
    			+ "4..8...16\n"
    			+ "....4.9.2\n";
    	assertEquals(expected, sudoku.toString());
    	
    	sudoku = Sudoku.fromFile(3, "samples"+ File.separator +"sudoku_hard.txt");
    	expected ="9....3..7\n"
    			+ "8.......1\n"
    			+ "..32..864\n"
    			+ ".6..27...\n"
    			+ ".81..4...\n"
    			+ "....3....\n"
    			+ ".....9352\n"
    			+ ".....5...\n"
    			+ "1.....47.\n";
    	assertEquals(expected, sudoku.toString());
    	
    	sudoku = Sudoku.fromFile(3, "samples"+ File.separator +"sudoku_hard2.txt");
    	expected ="713254968\n"
    			+ "....87541\n"
    			+ "845.617..\n"
    			+ ".5.612497\n"
    			+ "...4798..\n"
    			+ "4.9538.1.\n"
    			+ "...72.186\n"
    			+ "...196.54\n"
    			+ "1.6845279\n";
    	assertEquals(expected, sudoku.toString());
    }
}
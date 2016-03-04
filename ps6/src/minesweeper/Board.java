package minesweeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


/*
 * Thread Safety Argument:
 *   This class is threadsafe because it's immutable:
 *    - col is final
 *    - row is final
 *    - state points to a immutable char array
 *    - board points to a immutable boolean array
 *    - all accesses to state and board happen within Board methods,
 *      which are all guarded by Board's lock
 * */
public class Board {
	
	private final int col;
	private final int row;
	private final char[][] state; 
	private final boolean[][] board;
	
	/**
	 * Construct a untouched Board form a file
	 * @param file contains board information
	 * @throws IOException 
	 * */
	public Board(File file) throws IOException{
		try(
			BufferedReader in = new BufferedReader(
					new FileReader(file));
		){
			String line = in.readLine();
			col = Integer.parseInt(line.split(" ")[0]); 
			row = Integer.parseInt(line.split(" ")[1]);
			
			state = new char[col][row];
			board = new boolean[col][row];
			
			for (int i = 0; i < col; i++) {
				line = in.readLine();
				String grids[] = line.split(" ");
				for (int j = 0; j < row; j++) {
					board[i][j] = grids[j].equals("1");
					state[i][j] = '-';
				}
			}
			
		}
	}
	
	/**
	 * Construct a random Board
	 * @param c the number of column
	 * @param r the number of row
	 * */
	public Board(int column, int row){
		this.col = column;
		this.row = row;
		
		Random random = new Random(47);
		
		state = new char[col][row];
		board = new boolean[col][row];
		
		for (int i = 0; i < col; i++) {
			for (int j = 0; j < row; j++) {
				board[i][j] = random.nextBoolean();
				state[i][j] = '-';
			}
		}
	}
	
	/**
	 * Flag at position (x, y), requires (x, y) to be untouched
	 * @param x the row position, requires to be in range
	 * @param y the column position, requires to be in range
	 * */
	public synchronized void setFlag(int x, int y) {
		if (state[y][x] == '-') {
			state[y][x] = 'F';
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * DeFlag at position (x, y), requires (x, y) to be Flaged
	 * @param x the row position, requires to be in range
	 * @param y the column position, requires to be in range
	 * */
	public synchronized void deFlag(int x, int y) {
		if (state[y][x] == 'F') {
			state[y][x] = '-';
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	
	/**
	 * Dig at position (x, y).If (x, y)'s state is 'untouched' change it to 'dug'
	 * If  (x, y)'s state is 'bombed' change it to 'no bomb'
	 * @param x the row position, requires to be in range
	 * @param y the column position, requires to be in range
	 * @return true if board[y][x] has bomb else false
	 * */
	public synchronized boolean dig(int x, int y){
		if (state[y][x] != '-') {
			throw new UnsupportedOperationException();
		}
		boolean hasBomb = board[x][y];
		
		if(hasBomb){
			board[x][y] = false;
			for (Integer[] pos : getNeighbors(x, y)) {
				int tx = pos[0];
		        int ty = pos[1];
		        if( (state[tx][ty] != '-') && (state[tx][ty] != 'F') ){
		        	state[tx][ty] -= 1; //reduce neighbor's bomb count by one
		        }
			}
		}else{
		
			ArrayDeque<Integer[]> queue = new ArrayDeque<Integer[]>();
			HashSet<String> used = new HashSet<String>();
			
			queue.add(new Integer[]{y, x});
			used.add(String.format("%s,%s", y, x));
			
			while(!queue.isEmpty()){
				Integer[] pos = queue.poll();
		        int tx = pos[0];
		        int ty = pos[1];
		        int bombCount = getBombCount(ty, tx);
		        
		        if (bombCount == 0) {
		        	state[ty][tx] = ' ';
		        	
		        	ArrayList<Integer[]> neighbors = getNeighbors(tx, ty);
		        	
		        	for(Integer[] tpos : neighbors){
		        		if (!used.contains(String.format("%s,%s", tpos[0], tpos[1]))) {
		                    queue.add(new Integer[] {tpos[0], tpos[1]});
		                    used.add(String.format("%s,%s", tpos[0], tpos[1]));
		                }
		        	}
		        	
		        }else{
		        	state[ty][tx] = (char) ('0' + bombCount);
		        }
			}
		}
		return hasBomb;
	}
	
	/**
	 * Get (x, y) neighbor's position
	 * @param x the row position, requires to be in range
	 * @param y the column position, requires to be in range
	 * @return ArrayList contains neighbor's position
	 * */
	private ArrayList<Integer[]> getNeighbors(int x, int y){
		ArrayList<Integer[]> neighbors = new ArrayList<Integer[]>();
		for(int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++) {
				if (validPos(x + i, y +j) && (i != 0 || j != 0)) {
	                neighbors.add(new Integer[] {x + i, y + j});
	            }
			}
		}
		return neighbors;
	}
	
	/**
	 * Check whether (x, y) is a valid position
	 * @return true if (x, y) is a valid position otherwise false
	 * */
	private boolean validPos(int x, int y) {
		return x >= 0 && x < row && y >= 0 && y < col;
	}
	
	/**
	 * Count the number of bombs surrounding at position (x, y)
	 * @param x the row position, requires to be in range
	 * @param y the column position, requires to be in range
	 * @return integer the number of bombs in 3 * 3 neighborhood
	 * */
	private synchronized int getBombCount(int x, int y){
		ArrayList<Integer[]> neighbors = getNeighbors(x, y);
		int count = 0;
		
		for(Integer[] pos : neighbors){
			if(board[pos[0]][pos[1]] == true){
				count += 1;
			}
		}
		
		return count;
	}
	
	/**
	 * Return single state information
	 * @param x the row position, requires to be in range
	 * @param y the column position, requires to be in range
	 * @return char represents state information
	 * */
	public synchronized char getState(int x, int y) {
		return state[y][x];
	}
	
	public int getColumn() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	/**
	 * Format char array into specified string form
	 * @return a string represents board's state
	 * */
	@Override
	public synchronized String toString(){
		
		StringBuilder result = new StringBuilder();
		
		for(int i=0; i<col; i++){
			for (int j = 0; j < row; j++) {
				result.append(state[i][j]);
				result.append(" ");
			}
			result.deleteCharAt(result.length() - 1); //remove the last space
			result.append(System.lineSeparator());
		}
		
		return result.toString();
	}
	
}

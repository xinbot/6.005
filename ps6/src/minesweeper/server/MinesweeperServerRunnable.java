package minesweeper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import minesweeper.Board;

public class MinesweeperServerRunnable implements Runnable {
	
	private Socket clientSocket;
	
	private final Board board;
	
	private boolean debug;

	private boolean readyToClose = false;
	
	/**
	 * Generate a client Socket running on it's own thread
	 * @param socket a client Socket
	 * @param debug flag value
	 * @param board represents a board instance
	 * */
	public MinesweeperServerRunnable(Socket socket, Board board, boolean debug) {
		clientSocket = socket;
		this.board = board;
		this.debug = debug;
		
	}

	@Override
	public void run() {
		// handle the client
        try {
            handleConnection(clientSocket);
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate serve()
        } finally {
        	try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	
	/**
     * Handle a single client connection.  Returns when client disconnects.
     * @param socket  socket where client is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
        	out.format("Welcome to Minesweeper. Board: %s columns by %s rows."
        		     + " Players: %s including you. Type 'help' for help.%n",
        		       board.getColumn(), board.getRow(), Thread.activeCount() - 1);
        	
        	for (String line = in.readLine(); line != null && !readyToClose; line = in.readLine()) {
        		String output = handleRequest(line);
        		if(output != null) {
        			out.println(output);
        		}
        	}
        } finally {        
        	out.close();
        	in.close();
        }
    }
    
    /**
	 * handler for client input
	 * 
	 * make requested mutations on game state if applicable, then return appropriate message to the user
	 * 
	 * @param input
	 * @return
	 */
	private String handleRequest(String input) {

		String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
		if(!input.matches(regex)) {
			//invalid input
			return String.format("HELP!%n");
		}
		String[] tokens = input.split(" ");
		if(tokens[0].equals("look")) {
			// 'look' request
			return board.toString();
		} else if(tokens[0].equals("help")) {
			// 'help' request
			return String.format("HELP!%n");
		} else if(tokens[0].equals("bye")) {
			// 'bye' request
			readyToClose = true;
	    	return null;
		} else {
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			if(tokens[0].equals("dig")) {
				// 'dig x y' request
				synchronized(board) {
		        	if (x >= 0 && x < board.getColumn() && y >= 0 && y < board.getRow()
		        		&& board.getState(x, y) == '-') {
		        		if (board.dig(x, y)) {
		        			if (debug == false) {
		        				readyToClose = true;
		        			}
		        			return String.format("BOOM!%n");
		        		}
		        	}
		        	return board.toString();
		        }
			} else if(tokens[0].equals("flag")) {
				// 'flag x y' request
				synchronized(board) {
		        	if (x >= 0 && x < board.getColumn() && y >= 0 && y < board.getRow()
		            		&& board.getState(x, y) == '-') {
		            		board.setFlag(x, y);
		            	}
		        	return board.toString();
		        }
			} else if(tokens[0].equals("deflag")) {
				// 'deflag x y' request
				synchronized (board) {
		        	if (x >= 0 && x < board.getColumn() && y >= 0 && y < board.getRow()
		            		&& board.getState(x, y) == 'F') {
		            		board.deFlag(x, y);
		            	}
		        	return board.toString();
		        }
			}
		}
		//should never get here
		return "";
	}

}

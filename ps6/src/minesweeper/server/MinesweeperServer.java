package minesweeper.server;

import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

import minesweeper.Board;

public class MinesweeperServer {

	private final static int PORT = 43594;
	private ServerSocket serverSocket;

	private final boolean debug;
	private static final int DEFAULT_SIZE = 10;
	
	private final Board board;

	/**
	 * Make a MinesweeperServer that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535.
	 */
	public MinesweeperServer(int port, boolean debug,Board board) throws IOException {
		serverSocket = new ServerSocket(port);
		this.board = board;
		this.debug = debug;
	}

	/**
	 * Run the server, listening for client connections and handling them. Never
	 * returns unless an exception is thrown.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken (IOExceptions from
	 *             individual clients do *not* terminate serve()).
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client connects
			Socket socket = serverSocket.accept();

			// start a client Socket new thread
			new Thread(new MinesweeperServerRunnable(socket, board, debug)).start();

		}
	}

	/**
	 * Start a MinesweeperServer using the given arguments.
	 * 
	 * Usage: MinesweeperServer ["true" | "false"] [-s N] [-f PATH]
	 * 
	 */
	public static void main(String[] args) {
    	int N = 0;
    	File file = null;
    	boolean d = false; //Debug
    	Board b = new Board(DEFAULT_SIZE, DEFAULT_SIZE); //Board
    	Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        
    	try{
        	while(!arguments.isEmpty()){
        		String flag = arguments.remove();
        	try{
        		if (flag.equals("true")) {
                    d = true;
                } else if (flag.equals("false")) {
                    d = false;
                }else if (flag.equals("-s")) {
                    String size = arguments.remove();
                    N = Integer.parseInt(size);
                    b = new Board(N, N);
                }else if (flag.equals("-f")) {
                    file = new File(arguments.remove());
                    if (!file.isFile()) {
                        throw new IllegalArgumentException(
                                "file not found: \"" + file + "\"");
                    }
                    b = new Board(file);
                }else {
                    throw new IllegalArgumentException("unknown option: \""
                            + flag + "\"");
                }
        		
        		}catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException(
                            "unable to parse Size for " + flag);
                } catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("Usage: MinesweeperServer ['true' | 'false'] [-s N] [-f PATH]");
            return;
        }
        
        try {
            MinesweeperServer server = new MinesweeperServer(PORT, d, b);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
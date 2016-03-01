package factors.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.BigMath;

/**
 * PrimeFactorsServer performs the "server-side" algorithm for counting prime
 * factors.
 *
 * Your PrimeFactorsServer should take in a single Program Argument indicating
 * which port your Server will be listening on. ex. arg of "4444" will make your
 * Server listen on 4444.
 * 
 * Your server will only need to handle one client at a time. If the connected
 * client disconnects, your server should go back to listening for future
 * clients to connect to.
 * 
 * The client messages that come in will indicate the value that is being
 * factored and the range of values this server will be processing over. Your
 * server will take this in and message back all factors for our value.
 */
public class PrimeFactorsServer {

	/** Certainty variable for BigInteger isProbablePrime() function. */
	private final static int PRIME_CERTAINTY = 10;

	/** default port number where the PrimeFactors server listen for connection */
	private static final int PRIME_FACTOR_PORT = 4444;

	private ServerSocket serverSocket;

	/**
	 * Make a PrimeFactorsServer that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 */
	public PrimeFactorsServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Run the server, listening for connections and handling them.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken
	 */
	private void serve() throws IOException {
		while (true) {
			// blocks until a client connects
			Socket clientSocket = serverSocket.accept();
			try {
				handle(clientSocket);
			} catch (IOException ioe) {
				ioe.printStackTrace(); // but don't terminate serve()
			} finally {
				clientSocket.close();
			}
		}
	}

	/**
	 * Handle one client connection. Return when client disconnect.
	 * 
	 * @param clientSocket
	 *            socket for a connected client to handle
	 * @throws IOException
	 *             if connection encounters an error
	 */
	private void handle(Socket clientSocket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				clientSocket.getOutputStream()));

		try {
			String request = in.readLine(); // Single line of message
			while (request != null) {
				processRequest(out, request);
				request = in.readLine();
			}
		} finally {
			out.close();
			in.close();
		}
	}

	/**
	 * Process the client's one request. Return when transaction finishes.
	 * 
	 * @param request
	 *            legal client to server request message
	 * @param out
	 *            the output stream for a client socket
	 * @throws IOException
	 *             on server or network failure
	 */
	public void processRequest(PrintWriter out, String request)
			throws IOException {
		if (validRequest(request)) {
			String s[] = request.split("\\s");

			BigInteger N = new BigInteger(s[1]);
			BigInteger low = new BigInteger(s[2]);
			BigInteger hi = new BigInteger(s[3]);

			BigInteger[] primeFactors = BigMath.findPrimeFactors(N, low, hi,
					PRIME_CERTAINTY);

			for (int i = 0; i < primeFactors.length; i++) {
				sendReply(out, "found " + N + " " + primeFactors[i]);
			}

			sendReply(out, "done " + N + " " + low + " " + hi);
		} else {
			sendReply(out, "invalid");
		}
	}

	/**
	 * Check to see if the client's request is legal,according to
	 * client-to-server message protocol
	 * 
	 * @param request
	 *            to be processed
	 * @return true if the client's request is in a legitimate form, otherwise
	 *         return false.
	 * */
	public boolean validRequest(String request) {
		Pattern pattern = Pattern.compile("factor\\s\\d+\\s\\d+\\s\\d+");
		Matcher matcher = pattern.matcher(request);

		if (matcher.find()) {
			String s[] = request.split("\\s");
			BigInteger N = new BigInteger(s[1]);
			if (!(N.compareTo(BigInteger.valueOf(2)) == -1)) { // N >= 2
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Send a reply message to the client. Requires this is "open"
	 * 
	 * @param out
	 *            the output stream for a client socket
	 * @param message
	 *            message to reply the client
	 * @throws IOException
	 *             on server or network failure
	 */
	private void sendReply(PrintWriter out, String message) throws IOException {
		out.print(message + "\n");
		out.flush(); // important! flush out the buffer so the reply gets sent
	}

	/**
	 * @param args
	 *            String array containing Program arguments. It should only
	 *            contain one String indicating the port it should connect to.
	 *            Defaults to port 4444 if no Program argument is present.
	 */
	public static void main(String[] args) {
		if (args.length > 1) {
			System.err
					.println("Usage: java PrimeFactorsServer [port], where port is an optional\n"
							+ " numerical argument within range 0-65535.");
			System.exit(1);
		}

		int portNumber = 0;

		if (args.length == 0) {
			portNumber = PRIME_FACTOR_PORT;
		} else if (args.length == 1) {
			try {
				portNumber = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				System.err.println("EchoServer: illegal port, " + args[0]);
				nfe.printStackTrace();
			}
		}

		try {
			PrimeFactorsServer server = new PrimeFactorsServer(portNumber);
			server.serve();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
}

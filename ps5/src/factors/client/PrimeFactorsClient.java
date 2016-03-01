package factors.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.BigMath;

/**
 * PrimeFactorsClient class for PrimeFactorsServer.
 * 
 * Your PrimeFactorsClient class should take in Program arguments
 * space-delimited indicating which PrimeFactorsServers it will connect to. ex.
 * args of "localhost:4444 localhost:4445 localhost:4446" will connect the
 * client to PrimeFactorsServers running on localhost:4444, localhost:4445,
 * localhost:4446
 *
 * Your client should take user input from standard input. The appropriate input
 * that can be processed is a number. If your input is not of the correct
 * format, you should ignore it and continue to the next one.
 * 
 * Your client should distribute to each server the appropriate range of values
 * to look for prime factors through.
 */
public class PrimeFactorsClient {
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;

	/**
	 * Make a prime factors client and connect it to a server running on
	 * hostname at the specified port.
	 * 
	 * @param hostname
	 *            the host on which a server runs
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 * @throws IOException
	 */
	public PrimeFactorsClient(String hostname, int port) throws IOException {
		clientSocket = new Socket(hostname, port);
		in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(
				clientSocket.getOutputStream()));
	}

	/**
	 * Establish connection between this client and multiple servers
	 * 
	 * @param servers
	 *            a list of server address
	 * @return a list of clients connected to those specified servers
	 * @throws IOException
	 *             on server or network failure
	 * */
	private static PrimeFactorsClient[] establishConnection(String[] servers)
			throws IOException {
		int serverAmount = servers.length;
		PrimeFactorsClient[] clients = new PrimeFactorsClient[serverAmount];
		for (int i = 0; i < serverAmount; i++) {
			clients[i] = new PrimeFactorsClient(getHostname(servers[i]),
					getPort(servers[i]));
		}

		return clients;
	}

	/**
	 * Extract hostname value from server parameter
	 * 
	 * @param server
	 *            a string in the form of hostname:port
	 * @return the hostname of a server address
	 * */
	private static String getHostname(String server) {
		return server.substring(0, server.indexOf(":"));
	}

	/**
	 * Extract port number from server parameter
	 * 
	 * @param server
	 *            a string in the form of hostname:port
	 * @return the port number of a server address
	 * */
	private static int getPort(String server) {
		String portNumber = server.substring(server.indexOf(":") + 1);
		return Integer.parseInt(portNumber);
	}

	/**
	 * Handle the user's request, in Read-Evaluate-Print Loop (REPL) way.
	 * 
	 * @param clients
	 *            sub-clients in connection with different servers
	 * @throws IOException
	 *             on server or network failure
	 */
	public static void handleUserRequests(PrimeFactorsClient[] clients)
			throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));

		String userInput = stdIn.readLine();

		while (userInput != null) {
			Pattern pattern = Pattern.compile("\\s*quit\\s*");
			Matcher matcher = pattern.matcher(userInput);

			if (matcher.find()) {
				// user quits this conversation
				for (int i = 0; i < clients.length; i++) {
					clients[i].close();
				}
				System.exit(0);
			}

			// STEP1: Validate the user's request
			if (!validateUserInput(userInput)) {
				displayResult("invalid");
			}

			// STEP2: Dispatch the computation over different servers
			BigInteger N = new BigInteger(userInput.trim());
			dispatch(N, clients);

			// STEP3: Accumulate results from different servers
			BigInteger[] primeFactors = accumulate(N, clients);

			// STEP4: Show the user the final result
			StringBuilder evaluationResult = new StringBuilder(N + "=");

			int i = 0;

			while (i < primeFactors.length - 1) {
				evaluationResult.append(new String(primeFactors[i]
						.toByteArray()) + "*");
				i++;
			}

			evaluationResult.append(new String(
					primeFactors[primeFactors.length - 1].toByteArray()));

			displayResult(evaluationResult.toString());

			userInput = stdIn.readLine();
		}

	}

	/**
	 * Validate whether user enter into correct form
	 * 
	 * @param user
	 *            input requires to be valid number greater than 2
	 * */
	private static boolean validateUserInput(String userInput) {
		Pattern pattern = Pattern.compile("\\s*\\d+\\s*");
		Matcher matcher = pattern.matcher(userInput);
		if (matcher.find()) {
			String inputValue = userInput.trim();
			BigInteger N = new BigInteger(inputValue);
			if (N.compareTo(BigInteger.valueOf(2)) == -1) {
				// N less than 2
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Partition and dispatch the prime factor searching over servers.
	 * 
	 * @param N
	 *            the number to be factored in to primes, assumed to be valid
	 * @param clients
	 *            sub-clients in connection with different servers
	 * @throws IOException
	 *             on server or network failure
	 */
	private static void dispatch(BigInteger N, PrimeFactorsClient[] clients)
			throws IOException {
		int len = clients.length; // amount of servers
		BigInteger lo;
		BigInteger hi;
		BigInteger averageWorkload = BigMath.sqrt(N).divide(
				BigInteger.valueOf(len)); // average workload for a server;

		for (int i = 0; i < len; i++) {
			if (i == 0) {// interval [2, sqrt(N)/m]
				lo = BigInteger.valueOf(2);
				hi = averageWorkload;
			} else if (i == len - 1) {// interval [((m-1)*sqrt(N)/m)+1, sqrt(N)]
				lo = averageWorkload.multiply(BigInteger.valueOf(len - 1)).add(
						BigInteger.ONE);
				hi = BigMath.sqrt(N);
			} else {// (i*sqrt(N)/m)+1, (i+1)*sqrt(N)/m], where 0 < i < m-1
				lo = averageWorkload.multiply(BigInteger.valueOf(i)).add(
						BigInteger.ONE);
				hi = averageWorkload.multiply(BigInteger.valueOf(i + 1));
			}

			clients[i].sendRequest(N, lo, hi);
		}

	}

	/**
	 * Accumulate the prime factors return from distributed servers.
	 * 
	 * @param N
	 *            the number to be factored in to primes, assumed to be valid
	 * @param clients
	 *            sub-clients in connection with different servers
	 * @return an array that contains prime factors accumulated from different
	 *         servers.
	 * @throws IOException
	 *             on server or network failure
	 */
	private static BigInteger[] accumulate(BigInteger N,
			PrimeFactorsClient[] clients) throws IOException {
		int len = clients.length; // amount of servers
		ArrayList<BigInteger> primeFactors = new ArrayList<BigInteger>();

		for (int i = 0; i < len; i++) {
			ArrayList<BigInteger> factors = clients[i].getPrimeFactors();
			Iterator<BigInteger> factorIter = factors.iterator();

			while (factorIter.hasNext()) {
				primeFactors.add(factorIter.next());
			}
		}

		Iterator<BigInteger> primeFactorIter = primeFactors.iterator();

		while (primeFactorIter.hasNext()) {
			N = N.divide(primeFactorIter.next());
		}

		if (N.compareTo(BigInteger.ONE) == 1) {
			primeFactors.add(N);
		}

		return primeFactors.toArray(new BigInteger[0]);

	}

	/**
	 * Return all the prime factors a server found in its searching interval.
	 * 
	 * @throws IOException
	 *             on server or network failure
	 */
	private ArrayList<BigInteger> getPrimeFactors() throws IOException {
		ArrayList<BigInteger> primeFactors = new ArrayList<BigInteger>();

		String result = in.readLine();

		if (result == null) {
			return getPrimeFactors();
		}

		while (result != null) {
			if (result.startsWith("done")) {
				// the server has finished in searching for prime factors
				break;
			} else {
				String[] foundSignal = result.split("\\s");
				String primeFactorStr = foundSignal[2];
				BigInteger primeFactor = new BigInteger(
						primeFactorStr.getBytes());
				primeFactors.add(primeFactor);
			}
			result = in.readLine();
		}

		return primeFactors;
	}

	/**
	 * Send the prime factor searching request to the server. Require this is
	 * "open"
	 * 
	 * @param N
	 *            the number to be factored in to primes
	 * @param low
	 *            the lower bound of prime factors
	 * @param high
	 *            the upper bound of prime factors
	 * @throws IOException
	 *             on server or network failure
	 * */
	private void sendRequest(BigInteger N, BigInteger lo, BigInteger hi) {
		String clientRequest = "factor " + N + " " + lo + " " + hi;
		out.print(clientRequest + "\n");
		out.flush(); // important! make sure message actually gets sent
	}

	/**
	 * Show user the result of evaluation, which is either a decomposition of
	 * the request number or a warning of invalid input.
	 * 
	 * @param result
	 *            string to be printed on the console
	 * */
	private static void displayResult(String result) {
		System.out.print(">>> " + result + "\n");
	}

	/**
	 * Closes the client's connection to the server. This client is now closed.
	 * Requires this is "open"
	 * 
	 * @throws IOException
	 *             if close fails
	 */
	public void close() throws IOException {
		out.close();
		in.close();
		clientSocket.close();
	}

	/**
	 * @param args
	 *            String array containing Program arguments. Each String
	 *            indicates a PrimeFactorsServer location in the form
	 *            "host:port" If no program arguments are inputted, this Client
	 *            will terminate.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.err
					.println("You should provide at least one Program Argument in the form of [hostname:port]");
			System.exit(1);
		}

		PrimeFactorsClient[] clients = establishConnection(args);
		handleUserRequests(clients);
	}

}

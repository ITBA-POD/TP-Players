package ar.edu.itba.pod.tp.referee;

import ar.edu.itba.pod.tp.interfaces.Referee;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Hello world!
 *
 */
public class RefereeApp 
{
	
    public static void main( String[] args ) throws ParseException
    {
		final CommandLine cmdLine = parseArguments(args);
		final int port = Integer.valueOf( cmdLine.getOptionValue(PORT_L, PORT_D));
		final int total = Integer.valueOf( cmdLine.getOptionValue(REQS_L, REQS_D));
		
		if (cmdLine.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("server", options);
			System.exit(0);
		}
		try {
			System.out.println("Server starting on port: " + port);
			Registry registry = LocateRegistry.createRegistry(port);

			System.out.println("Registry started - requests total: " + total);
			RefereeServer server = new RefereeServer(total);
			Referee stub = (Referee) UnicastRemoteObject.exportObject(server, port);
			registry.bind("referee", stub);

			System.out.println("Press any key to start");
			Scanner scan = new Scanner(System.in);
			
			synchronized (server) {
				scan.nextLine();
				server.playing = true;
			}
			System.out.println("EMPEZAMOS!");
			String line;
			do {
				line = scan.next();
				server.showResults();
				
			} while(!"x".equals(line));
			System.exit(0);
		}
		catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			System.exit(-1);
		}    
	}

	private static CommandLine parseArguments(String[] args) throws ParseException
	{
		CommandLineParser parser = new BasicParser();
		try {
			// parse the command line arguments
			return parser.parse(options, args, false);
		}
		catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			throw exp;
		}	
	}
	
	private static Options createOptions()
	{
		final Options result = new Options();
		result.addOption(PORT_S, PORT_L, true, "Referee server port");
		result.addOption(REQS_S, REQS_L, true, "Requests total");
		result.addOption("help", false, "Help");
		return result;
	}

	private static final String PORT_L = "port";
	private static final String PORT_S = "p";
	private static final String PORT_D = "7242";
	private static final String REQS_L = "requests";
	private static final String REQS_S = "r";
	private static final String REQS_D = "200";
	private static Options options = createOptions();
}

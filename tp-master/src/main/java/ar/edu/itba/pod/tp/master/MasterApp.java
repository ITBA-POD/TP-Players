package ar.edu.itba.pod.tp.master;

import ar.edu.itba.pod.tp.interfaces.Master;
import ar.edu.itba.pod.tp.interfaces.Referee;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class MasterApp 
{
    public static void main( String[] args ) throws ParseException, RemoteException, AlreadyBoundException, InterruptedException
    {
		CommandLineParser parser = new BasicParser();
		final CommandLine cmdLine = parser.parse(options, args, false);

		if (cmdLine.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("server", options);
			System.exit(0);
		}

		final int port = Integer.valueOf( cmdLine.getOptionValue(PORT_L, PORT_D));
		final int requestsTotal = Integer.valueOf( cmdLine.getOptionValue(REQS_L, REQS_D));
		final int gameTotal = Integer.valueOf( cmdLine.getOptionValue(GAME_L, GAME_D));
		final int timeout = Integer.valueOf( cmdLine.getOptionValue(TIME_L, TIME_D));
		
		System.out.println("Master starting on port: " + port);
		Registry registry = LocateRegistry.createRegistry(port);

		System.out.println(String.format("Master started - requests total: %s game score total: %s", requestsTotal, gameTotal));
		final MasterServer server = new MasterServer(registry, requestsTotal, gameTotal);
		Master stub = (Master) UnicastRemoteObject.exportObject(server, port);
		registry.bind("master", stub);
		ExecutorService executor = Executors.newScheduledThreadPool(2);

		do {
			System.out.println(String.format("Press any key to start: %s players", server.getReferees().size()));
			Scanner scan = new Scanner(System.in);
			scan.nextLine();
			
		} while(server.getReferees().size() < 2);
		System.out.println(String.format("Game will finish in %s seconds. %d players", timeout, server.getReferees().size()));
		
		executor.submit(new Runnable() {

			public void run()
			{
				try {
					final Referee[] referees = server.getReferees().values().toArray(new Referee[0]);
					final int opt = (int) (java.lang.Math.random() * referees.length);
					Referee referee = referees[opt];
					
					final String salt = UUID.randomUUID().toString();
					referee.hostGame(requestsTotal, salt);
					for (Referee player : referees) {
						if (player == referee) {
							continue;
						}
						player.joinGame(0, "mm");
					}
				}
				catch (RemoteException ex) {
					Logger.getLogger(MasterApp.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		executor.awaitTermination(timeout, TimeUnit.SECONDS);
				
		System.out.println("Finished");
		System.exit(0);
    }

	private static Options createOptions()
	{
		final Options result = new Options();
		result.addOption(PORT_S, PORT_L, true, "Port");
		result.addOption(REQS_S, REQS_L, true, "Requests Total");
		result.addOption(GAME_S, GAME_L, true, "Game Score Total");
		result.addOption(TIME_S, TIME_L, true, "Game Timeout");
		result.addOption("help", false, "Help");
		return result;
	}

	private static final String PORT_L = "port";
	private static final String PORT_S = "p";
	private static final String PORT_D = "7242";
	private static final String REQS_L = "requests";
	private static final String REQS_S = "r";
	private static final String REQS_D = "200";
	private static final String GAME_L = "game";
	private static final String GAME_S = "g";
	private static final String GAME_D = "100";
	private static final String TIME_L = "timeout";
	private static final String TIME_S = "t";
	private static final String TIME_D = "60";
	private static Options options = createOptions();
}

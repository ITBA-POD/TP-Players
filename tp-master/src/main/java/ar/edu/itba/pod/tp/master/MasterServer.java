/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.itba.pod.tp.master;

import ar.edu.itba.pod.tp.interfaces.GameResult;
import ar.edu.itba.pod.tp.interfaces.Master;
import ar.edu.itba.pod.tp.interfaces.PlayerReferee;
import ar.edu.itba.pod.tp.interfaces.PlayerRefereeRegistration;
import ar.edu.itba.pod.tp.interfaces.Referee;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mariano
 */
public class MasterServer implements Master
{
	private final Registry registry;
	private final Map<String, Referee> referees = Collections.synchronizedMap(new HashMap());
	private final int requestsTotal;
	private final int gameTotal;
	private final int gameTimeout = 2;

	MasterServer(Registry registry, int requestsTotal, int gameTotal)
	{
		this.registry = registry;
		this.requestsTotal = requestsTotal;
		this.gameTotal = gameTotal;
	}
	
	@Override
	public void registerReferee(Referee referee) throws RemoteException
	{
		try {
			final Remote refereeRemote = registry.lookup("referees/" + referee.getName());
			if (!(refereeRemote instanceof Referee)) {
				throw new IllegalArgumentException("Remote bind is not a Referee");
			}
			if (!referee.getName().equals(((Referee) refereeRemote).getName())) {
				throw new IllegalArgumentException("[BUG] Invalid name");
			}
			referees.put(referee.getName(), referee);
			System.out.println("Referee registered: " + referee.getName());
		}
		catch (Exception ex) {
			throw new RemoteException("Referee Registration failed", ex);
		}
	}

	public void registerNewGame(PlayerReferee referee, List<PlayerReferee> players) throws RemoteException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void registerGameResults(PlayerReferee referee, String results) throws RemoteException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<PlayerReferee> getActivePlayers() throws RemoteException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Map<String, Integer> getScores() throws RemoteException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private List<Referee> copyReferees()
	{
		synchronized (referees) {
			return new ArrayList<Referee>(referees.values());
		}
	}

	Map<String, Referee> getReferees()
	{
		return referees;
	}

	@Override
	public int getRequestsTotal()
	{
		return requestsTotal;
	}

	int getGameTotal()
	{
		return gameTotal;
	}

	Runnable newRunner()
	{
		return new GamesRunner();
	}
	
	class GamesRunner implements Runnable
	{
		final ExecutorService executor = Executors.newSingleThreadExecutor();

		public void run()
		{
			do {
				try {
					hostGame();
				}
				catch (Exception ex) {
					Logger.getLogger(MasterServer.class.getName()).log(Level.SEVERE, null, ex);
				}
			} while (true);
		}
		
		private void hostGame() throws RemoteException, TimeoutException, InterruptedException, ExecutionException
		{
			final String salt = UUID.randomUUID().toString();
			final List<Referee> myReferees = copyReferees();
			final int opt = (int) (java.lang.Math.random() * myReferees.size());
			final Referee referee = myReferees.remove(opt);
			final List<String> players = new ArrayList();
			for (Referee player : myReferees) {
				players.add(player.getName());
			}
			Future<GameResult> submit = executor.submit(new Callable<GameResult>() {
				public GameResult call() throws Exception
				{
					return referee.hostGame(requestsTotal, salt, players);
				}
			});

			submit.get(gameTimeout, TimeUnit.SECONDS);
		}
	}
}

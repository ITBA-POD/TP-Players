/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.itba.pod.tp.master;

import ar.edu.itba.pod.tp.interfaces.Master;
import ar.edu.itba.pod.tp.interfaces.PlayerReferee;
import ar.edu.itba.pod.tp.interfaces.PlayerRefereeRegistration;
import ar.edu.itba.pod.tp.interfaces.Referee;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mariano
 */
public class MasterServer implements Master
{
	private final Registry registry;
	private final Map<String, Referee> referees = new HashMap();
	private final int requestsTotal;
	private final int gameTotal;

	MasterServer(Registry registry, int requestsTotal, int gameTotal)
	{
		this.registry = registry;
		this.requestsTotal = requestsTotal;
		this.gameTotal = gameTotal;
	}
	
	@Override
	public void registerReferee(String refereeName, Referee refereeClient) throws RemoteException
	{
		try {
			final Remote refereeRemote = registry.lookup("referees/" + refereeName);
			if (!(refereeRemote instanceof Referee)) {
				throw new IllegalArgumentException("Remote bind is not a Referee");
			}
			final Referee referee = (Referee) refereeRemote;
			
			referees.put(refereeName, referee);
			System.out.println("Referee registered: " + refereeName);
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
	
}

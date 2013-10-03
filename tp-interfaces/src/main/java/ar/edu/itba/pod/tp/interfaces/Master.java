/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.itba.pod.tp.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mariano
 */
public interface Master extends Remote
{
	/**
	 * A new referee registers with the master
	 * @return
	 * @throws RemoteException 
	 */
	void registerReferee(Referee referee) throws RemoteException;

	int getRequestsTotal() throws RemoteException;
	
	void registerGameResults(PlayerReferee referee, String results) throws RemoteException;
	
	List<PlayerReferee> getActivePlayers() throws RemoteException;
	
	Map<String, Integer> getScores() throws RemoteException;
}

package ar.edu.itba.pod.tp.interfaces;

import ar.edu.itba.pod.tp.interfaces.GameResult.PlayerResult;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface Referee extends Remote 
{
	String getName() throws RemoteException;

	/**
	 * Start a new game, notify players, let them play and process results and return the results
	 * @param gameIn
	 * @param gameHash
	 * @param guests
	 * @return 
	 * @throws RemoteException
	 */
	GameResult hostGame(final int gameIn, final String gameHash, List<String> guests) throws RemoteException;

	/**
	 * A player from this Referee must join an new game.
	 * @param gameIn
	 * @param gameHash
	 * @param host
	 * @throws RemoteException
	 */
	void joinGame(final int gameIn, final String gameHash, final String host) throws RemoteException;

	Registration newPlayer(String playerName, Player playerClient) throws RemoteException;

	void registerRequest(Player player, Request request) throws RemoteException;

	void registerResponse(Player player, Response response) throws RemoteException;

	Map<Player, PlayerResult> showResults() throws RemoteException;
}

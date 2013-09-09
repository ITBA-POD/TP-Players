package ar.edu.itba.pod.tp.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Referee extends Remote
{
	Registration newPlayer(String playerName, Player playerClient) throws RemoteException;
	
	void registerRequest(Player player, Request request) throws RemoteException;

	void registerResponse(Player player, Response response) throws RemoteException;
}

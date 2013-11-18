package ar.edu.itba.pod.tp.interfaces;

import java.io.Serializable;
import java.util.Collection;
import java.util.PriorityQueue;

/**
 *
 * @author mariano
 */
public class GameResult implements Serializable
{
	private final PriorityQueue<PlayerResult> results = new PriorityQueue<PlayerResult>();

	public Collection<PlayerResult> getResults()
	{
		return results;
	}
	
	public void addPlayerResult(String player, Status status, int serverCount, int playerCount)
	{
		PlayerResult playerResult = new PlayerResult(player, status, serverCount, playerCount);
		results.add(playerResult);
		
	}
	public static class PlayerResult implements Serializable, Comparable<PlayerResult>
	{
		public String player;
		public int position;
		public Status status;
		public int serverCount;
		public int playerCount;

		public PlayerResult(String player, Status status, int serverCount, int playerCount)
		{
			this.player = player;
			this.status = status;
			this.serverCount = serverCount;
			this.playerCount = playerCount;
		}

		@Override
		public String toString()
		{
			return "PlayerResult{" + "player=" + player + ", position=" + position + ", status=" + status + '}';
		}

		@Override
		public int compareTo(PlayerResult pr)
		{
			return -serverCount - playerCount + pr.serverCount + pr.playerCount;
		}
	}
	
	public enum Status 
	{
		SUCCESS,
		SLOW,
		ERROR
	}

	@Override
	public String toString()
	{
		String s = "GameResult { \n";
		int i = 1;
		for (PlayerResult pr : results) {
			s += "Position " + i++ + "\n" + pr + "\n";
		}
		s += "\n}";
		return s;
	}
	
	
}

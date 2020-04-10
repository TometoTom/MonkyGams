package core.data;

import java.util.HashMap;

import command.meta.PermissionLevel;
import games.meta.GameType;
import games.meta.statistics.GameStatistics;

public class PlayerStatistics {

	private String uuid;
	private PermissionLevel rank;
	private int currency;
	private HashMap<GameType, GameStatistics> gameStatistics = new HashMap<>();
	private int totalXP;
	private int totalChatMessages;
	private int totalBlocksWalked;
	private int timesLoggedIn;
	private String lastUsedIP;
	
	public PlayerStatistics(String uuid, PermissionLevel rank, int currency, HashMap<GameType, GameStatistics> gameStatistics, int totalXP,
			int totalChatMessages, int totalBlocksWalked, int timesLoggedIn, String lastUsedIP) {
		this.uuid = uuid;
		this.rank = rank;
		this.currency = currency;
		this.gameStatistics = gameStatistics;
		this.totalXP = totalXP;
		this.totalChatMessages = totalChatMessages;
		this.totalBlocksWalked = totalBlocksWalked;
		this.timesLoggedIn = timesLoggedIn;
		this.lastUsedIP = lastUsedIP;
	}
	
	public int getTotalXP() {
		return totalXP;
	}
	public void setTotalXP(int totalXP) {
		this.totalXP = totalXP;
	}
	public int getLevel() {
		int xp = getTotalXP();
		int count = 0;
		while (true) {
			xp -= count * 1000;
			count++;
			
			if (xp <= 0) {
				return count;
			}
		}
	}
	public int getTotalChatMessages() {
		return totalChatMessages;
	}
	public void setTotalChatMessages(int totalChatMessages) {
		this.totalChatMessages = totalChatMessages;
	}
	public int getTotalBlocksWalked() {
		return totalBlocksWalked;
	}
	public void setTotalBlocksWalked(int totalBlocksWalked) {
		this.totalBlocksWalked = totalBlocksWalked;
	}
	public void incrementTotalBlocksWalked() {
		this.totalBlocksWalked++;
	}
	public int getTimesLoggedIn() {
		return timesLoggedIn;
	}
	public void setTimesLoggedIn(int timesLoggedIn) {
		this.timesLoggedIn = timesLoggedIn;
	}
	public String getLastUsedIP() {
		return lastUsedIP;
	}
	public void setLastUsedIP(String lastUsedIP) {
		this.lastUsedIP = lastUsedIP;
	}
	public <T> T getStatistics(Class<T> statsClass) {
		
		GameType g = null;
		for (GameType gt : GameType.values()) {
			if (gt.getStatsClass() == statsClass) {
				g = gt;
			}
		}
		
		if (g == null) {
			return null;
		}
		
		if (!gameStatistics.containsKey(g)) {
			try {
				gameStatistics.put(g, g.getStatsClass().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
				/* This shouldn't fail. All can be instantiated with no arguments */
			}
		}
		return statsClass.cast(gameStatistics.get(g));
	}
	public GameStatistics getStatistics(GameType g) {
		if (!gameStatistics.containsKey(g)) {
			try {
				gameStatistics.put(g, g.getStatsClass().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
				/* This shouldn't fail. All can be instantiated with no arguments */
			}
		}
		return gameStatistics.get(g);
	}

	public PermissionLevel getRank() {
		return rank;
	}

	public void setRank(PermissionLevel rank) {
		this.rank = rank;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}
	
}

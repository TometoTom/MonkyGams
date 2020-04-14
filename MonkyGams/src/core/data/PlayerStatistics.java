package core.data;

import java.util.ArrayList;
import java.util.HashMap;

import command.meta.PermissionLevel;
import games.meta.GameType;
import games.meta.statistics.GameStatistics;
import hub.gadgets.meta.GadgetType;

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
	
	/* Additions */
	private HashMap<GadgetType, Integer> gadgets;
	private ArrayList<String> unlockedKits; // Stored as key: "Game Name:Kit Name". The game and kit names are specified in the relevant enums.
	private String customName;
	private String location;
	
	public PlayerStatistics(String uuid, PermissionLevel rank, int currency, HashMap<GameType, GameStatistics> gameStatistics, int totalXP,
			int totalChatMessages, int totalBlocksWalked, int timesLoggedIn, String lastUsedIP,
			HashMap<GadgetType, Integer> gadgets, ArrayList<String> unlockedKits, String customName, String location) {
		this.uuid = uuid;
		this.rank = rank;
		this.currency = currency;
		this.gameStatistics = gameStatistics;
		this.totalXP = totalXP;
		this.totalChatMessages = totalChatMessages;
		this.totalBlocksWalked = totalBlocksWalked;
		this.timesLoggedIn = timesLoggedIn;
		this.lastUsedIP = lastUsedIP;
		
		/* Additions */
		this.gadgets = gadgets == null ? new HashMap<>() : gadgets;
		this.unlockedKits = unlockedKits == null ? new ArrayList<>() : unlockedKits;
		this.customName = customName == null ? "" : customName;
		this.location = location == null ? "Earth" : location;
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

	public HashMap<GadgetType, Integer> getGadgets() {
		if (gadgets == null) gadgets = new HashMap<GadgetType, Integer>();
		return gadgets;
	}

	public void setGadgets(HashMap<GadgetType, Integer> gadgets) {
		this.gadgets = gadgets;
	}

	public ArrayList<String> getUnlockedKits() {
		if (unlockedKits == null) unlockedKits = new ArrayList<>();
		return unlockedKits;
	}

	public void setUnlockedKits(ArrayList<String> unlockedKits) {
		this.unlockedKits = unlockedKits;
	}

	public String getCustomName() {
		if (customName == null) customName = "";
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getLocation() {
		if (location == null) location = "";
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}

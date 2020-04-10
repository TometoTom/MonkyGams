package games.meta.statistics;

import org.bukkit.Material;

import utils.MonkyItemStack;

public abstract class GameStatistics {

	private long timePlayed = 0;
	private int gamesPlayed = 0;
	private int wins = 0;
	public String type = getClass().getName();

	public long getTimePlayed() {
		return timePlayed;
	}

	public void setTimePlayed(long timePlayed) {
		this.timePlayed = timePlayed;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public void addTimePlayed(long timePlayed) {
		setTimePlayed(getTimePlayed() + timePlayed);
	}
	
	public void incrementGamesPlayed() {
		setGamesPlayed(getGamesPlayed() + 1);
	}
	
	public void incrementWins() {
		setWins(getWins() + 1);
	}

	public GameStatistics(long timePlayed, int gamesPlayed, int wins) {
		this.timePlayed = timePlayed;
		this.gamesPlayed = gamesPlayed;
		this.wins = wins;
	}
	
	public GameStatistics() {
		
	}
	
	public String getTimePlayedStr() {
		if (getTimePlayed() == 0) {
			return "0 hours 0 minutes";
		}

		int mins = (int) (timePlayed / 1000 / 60);

		return (mins / 60) + " hours " + (mins % 60) + " minutes";
	}
	
	public abstract MonkyItemStack[] getInventory();
	
	public MonkyItemStack getPaper(String name, String... values) {
		MonkyItemStack stack = new MonkyItemStack(Material.PAPER);
		stack.setName(name);
		stack.setLore(values);
		return stack;
	}

	public String getType() {
		return type;
	}
	
}

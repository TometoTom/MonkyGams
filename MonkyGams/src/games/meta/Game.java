package games.meta;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import core.Main;
import games.meta.statistics.DataRecorder;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyScoreboard;

public abstract class Game {

	protected abstract void registerListeners();
	protected abstract void start();
	protected abstract void end();
	
	private ArrayList<Listener> listeners = new ArrayList<>();
	private DataRecorder stats = new DataRecorder();
	private GameCountdown countdown;
	private GameType type;
	private Map map;
	
	public Game(GameType type, Map map) {
		this.type = type;
		this.map = map;
		this.scoreboard = new MonkyScoreboard(GOLD + "" + BOLD + type.getName());
	}
	
	public GameType getType() {
		return type;
	}
	public Map getMap() {
		return map;
	}
	public DataRecorder getStats() {
		return stats;
	}
	public ArrayList<Listener> getListeners() {
		return listeners;
	}
	public MonkyScoreboard getScoreboard() {
		return scoreboard;
	}
	
	public void doCountdown(Runnable countdownEnd) {
		doCountdown(countdownEnd, 6, true);
	}
	public void doCountdown(Runnable countdownEnd, int seconds) {
		doCountdown(countdownEnd, seconds, true);
	}
	public void doCountdown(Runnable countdownEnd, int seconds, boolean stopMovement) {

		if (countdown != null) {
			countdown.stop();
		}
		
		countdown = new GameCountdown(countdownEnd, seconds, stopMovement);
		countdown.start();
		
	}
	public void cancelCountdown() {
		if (countdown != null)
			countdown.stop();
	}
	
	/*
	 * Events
	 */
	public void registerEvent(Listener l) {
		Bukkit.getPluginManager().registerEvents(l, Main.getPlugin());
		listeners.add(l);
	}

	public void deregisterAllEvents() {

		ArrayList<Listener> toRemove = new ArrayList<>();

		listeners.forEach((l) -> {
			HandlerList.unregisterAll(l);
			toRemove.add(l);
		});

		toRemove.forEach(listeners::remove);

	}
	
	public void deregisterEvent(Listener l) {
		HandlerList.unregisterAll(l);
	}

	public void deregisterEvents(Class<? extends Listener> l) {

		ArrayList<Listener> toRemove = new ArrayList<Listener>();
		listeners.forEach((listener) -> {
			/* If the listener in the loop is an instance of l */
			if (listener.getClass().isAssignableFrom(l.getClass())) {
				HandlerList.unregisterAll(listener);
				toRemove.add(listener);
			}
		});
		toRemove.forEach(listeners::remove);

	}

	/*
	 * Scoreboard
	 */
	private MonkyScoreboard scoreboard;
	private int scoreboardTaskId = -1;
	private long startTime;

	public void stopScoreboardUpdates() {
		Bukkit.getScheduler().cancelTask(scoreboardTaskId);
	}

	public void doScoreboardUpdating(Runnable r) {
		scoreboardTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), r, 0, 1);
	}

	public long getStartTime() {
		return startTime;
	}

	public void resetStartTime() {
		startTime = System.currentTimeMillis();
	}

	public String getScoreboardFriendlyTime() {

		if (getStartTime() == 0) {
			return "0.00s";
		}

		long l = System.currentTimeMillis() - getStartTime();

		if (l < 60000) {
			float f = l / 1000f;
			DecimalFormat df = new DecimalFormat("#.#");
			df.setRoundingMode(RoundingMode.CEILING);
			return df.format(f) + "s";
		}

		else {
			long timeSecs = l / 1000;

			int mins = (int) (timeSecs / 60);
			int secs = (int) (timeSecs % 60);

			return mins + "m " + secs + "s"; 
		}

	}

	public void broadcastIntroduction(String... lines) {
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "------------ " + getType().getName() + " ------------");
		Bukkit.broadcastMessage("");
		for (String line : lines) {
			Bukkit.broadcastMessage(ChatColor.GRAY + line);
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-------------" + getType().getName().replaceAll(".", "-") + "-------------");
	}
	
	public String getChatHeader() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "------------ " + getType().getName() + " ------------";
	}
	public String getChatFooter() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "-------------" + getType().getName().replaceAll(".", "-") + "-------------";
	}

	/*
	 * Statistics
	 */

	public int getTimeXP() {
		int xp = (int) (System.currentTimeMillis() - getStartTime());
		xp /= 1000;
		/* XP cap of 1 hour on games */
		if (xp > 3600) {
			xp = 3600;
		}
		return xp;
	}

	public int getTimePounds() {
		int pounds = (int) (System.currentTimeMillis() - getStartTime());
		pounds /= 1000;
		pounds = pounds / 7;
		return pounds;
	}

	public String AQUA = ChatColor.AQUA.toString();
	public String BLACK = ChatColor.BLACK.toString();
	public String BLUE = ChatColor.BLUE.toString();
	public String BOLD = ChatColor.BOLD.toString();
	public String DARK_AQUA = ChatColor.DARK_AQUA.toString();
	public String DARK_BLUE = ChatColor.DARK_BLUE.toString();
	public String DARK_GREY = ChatColor.DARK_GRAY.toString();
	public String DARK_GREEN = ChatColor.DARK_GREEN.toString();
	public String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
	public String DARK_RED = ChatColor.DARK_RED.toString();
	public String GOLD = ChatColor.GOLD.toString();
	public String GREY = ChatColor.GRAY.toString();
	public String GREEN = ChatColor.GREEN.toString();
	public String ITALIC = ChatColor.ITALIC.toString();
	public String LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();
	public String MAGIC = ChatColor.MAGIC.toString();
	public String RED = ChatColor.RED.toString();
	public String RESET = ChatColor.RESET.toString();
	public String STRIKETHROUGH = ChatColor.STRIKETHROUGH.toString();
	public String UNDERLINE = ChatColor.UNDERLINE.toString();
	public String WHITE = ChatColor.WHITE.toString();
	public String YELLOW = ChatColor.YELLOW.toString();

}

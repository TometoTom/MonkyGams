package games.game.dropper;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import core.Main;
import games.game.elytraparkour.ShowHidePlayerEvents;
import games.game.parkourrace.FeatherEvents;
import games.meta.Game;
import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;
import net.md_5.bungee.api.ChatColor;
import utils.game.GameUtils;

public class DropperGame extends Game {

	private long startTime; // For speed runs
	private HashMap<String, Integer> deaths = new HashMap<>(); // For scores at the end
	private HashMap<String, Integer> stages = new HashMap<>(); // For leaderboard stages
	private HashMap<String, Location> spawnPoints = new HashMap<>(); // For checkpoint

	private int scoreboardTaskId = -1;

	public long getStartTime() {
		return startTime;
	}

	public void resetStartTime() {
		startTime = System.currentTimeMillis();
	}

	public HashMap<String, Integer> getDeaths() {
		return deaths;
	}

	public HashMap<String, Location> getSpawnPoints() {
		return spawnPoints;
	}
	
	public HashMap<String, Integer> getStages(){
		return stages;
	}

	public DropperGame(Map map) {
		super(GameType.DROPPER, map);
	}

	@Override
	protected void registerListeners() {

		registerEvent(new ShowHidePlayerEvents());
		registerEvent(new FeatherEvents());
		registerEvent(new DeathEvents());
		registerEvent(new JoinLeaveEvents());
	}

	@Override
	protected void start() {

		registerListeners();

		getScoreboard().setLine(0, "");
		getScoreboard().setLine(1, RED + BOLD + "Map");
		getScoreboard().setLine(2, GREY + getMap().mapName);
		getScoreboard().setLine(3, "");
		getScoreboard().setLine(4, YELLOW + BOLD + "Time");
		getScoreboard().setLine(5, GREY + getScoreboardFriendlyTime());
		getScoreboard().setLine(6, "");
		getScoreboard().setLine(7, GREEN + BOLD + "Leaderboard:");
		getDeaths().entrySet().forEach((entry) -> {
			getScoreboard().addLine(GREY + entry.getKey() + " (Level " + stages.get(entry.getKey()) + ")" + " - " + entry.getValue() + "");
		});
		getScoreboard().showScoreboardToAll();

		scoreboardTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {

			getScoreboard().setLine(5, GREY + getScoreboardFriendlyTime());
			int count = 8;
			for (Entry<String, Integer> entry : getDeaths().entrySet()) {
				getScoreboard().setLine(count, GREY + entry.getKey() + " (Level " + stages.get(entry.getKey()) + ")" + " - " + entry.getValue() + "");
				count++;
			}
		}, 0, 1);

		Bukkit.getOnlinePlayers().forEach((p) -> p.teleport(getMap().getSpawn()));
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "------------ " + getType().getName() + " ------------");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.GRAY + "Navigate to the end of the map!");
		Bukkit.broadcastMessage(ChatColor.GRAY + "Green carpets are checkpoints.");
		Bukkit.broadcastMessage(ChatColor.GRAY + "Try to beat all the other players!");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-------------" + getType().getName().replaceAll(".", "-") + "-------------");

		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.getInventory().clear();
			p.getInventory().setItem(8, ShowHidePlayerEvents.GREEN_DYE);
			p.getInventory().setItem(0, FeatherEvents.FEATHER);
		});

		doCountdown(() -> {
			deregisterAllEvents();
			registerListeners();
			resetStartTime();
			Bukkit.getOnlinePlayers().forEach((p) -> getSpawnPoints().put(p.getName(), getMap().getSpawn()));
			Bukkit.getOnlinePlayers().forEach((p) -> getDeaths().put(p.getName(), 0));
			Bukkit.getOnlinePlayers().forEach((p) -> getStages().put(p.getName(), 1));
		});

	}

	public void win(Player winner) {

		Bukkit.getScheduler().cancelTask(scoreboardTaskId);

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Thread(() -> {
			winner.setGlowing(false);
			GameController.endCurrentGame();
		}), 20 * 8);

		Bukkit.getOnlinePlayers().forEach(player -> {
			player.teleport(getMap().getSpawn());
			ShowHidePlayerEvents.showPlayers(player);
		});

		GameUtils.fireworks(getMap(), 3, 40);
		winner.setGlowing(true);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + winner.getName() + " won!", getScoreboardFriendlyTime(), 5, 7 * 20, 5);

		}

		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "------------ " + getType().getName() + " ------------");
		Bukkit.broadcastMessage(ChatColor.GRAY + "Winner: " + winner.getName());
		Bukkit.broadcastMessage(ChatColor.GRAY + "Map: " + getMap().getMapName());
		Bukkit.broadcastMessage(ChatColor.GRAY + "Time: " + getScoreboardFriendlyTime());
		Bukkit.broadcastMessage("");
		for (Entry<String, Integer> entry : getDeaths().entrySet()) {
			Bukkit.broadcastMessage(ChatColor.GRAY + entry.getKey() + " (Level " + stages.get(entry.getKey()) + ") - " + entry.getValue() + " death" + (entry.getValue() == 1 ? "" : "s"));
		}
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-------------" + getType().getName().replaceAll(".", "-") + "-------------");

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

	@Override
	protected void end() {
		Bukkit.getScheduler().cancelTask(scoreboardTaskId);
		getScoreboard().unshowScoreboardForAll();
		deregisterAllEvents();
		Bukkit.getOnlinePlayers().forEach(ShowHidePlayerEvents::showPlayers);
	}

}

package games.game.parkourrace;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import core.Main;
import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import games.game.elytraparkour.ShowHidePlayerEvents;
import games.meta.Game;
import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;
import games.meta.statistics.ParkourRaceStatistics;
import net.md_5.bungee.api.ChatColor;
import utils.game.GameListeners;
import utils.game.GameUtils;

public class ParkourRaceGame extends Game {

	private Player winner;
	
	public ParkourRaceGame(Map map) {
		super(GameType.PARKOURRACE, map);
	}

	@Override
	protected void registerListeners() {
		registerEvent(new DeathEvents());
		registerEvent(new JoinLeaveEvents());
		registerEvent(new ShowHidePlayerEvents());
		registerEvent(new FeatherEvents());
		registerEvent(GameListeners.getPreventDestroyingBlocks());
		registerEvent(GameListeners.getPreventPlacingBlocks());
		registerEvent(GameListeners.getPreventInventoryMove());
		registerEvent(GameListeners.getPreventItemDrop());
		registerEvent(GameListeners.getPreventFire());
		registerEvent(GameListeners.getPreventRain());
		registerEvent(GameListeners.getPreventFoodChange());
		registerEvent(GameListeners.getPreventDamage());
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
		getScoreboard().setLine(7, GREEN + BOLD + "Deaths:");
		getStats().getDeaths().entrySet().forEach((entry) -> {
			getScoreboard().addLine(GREY + entry.getKey() + " - " + entry.getValue() + "");
		});
		getScoreboard().showScoreboardToAll();

		doScoreboardUpdating(() -> {
			getScoreboard().setLine(5, GREY + getScoreboardFriendlyTime());
			int count = 8;
			for (Entry<String, Integer> entry : getStats().getDeaths().entrySet()) {
				getScoreboard().setLine(count, GREY + entry.getKey() + " - " + entry.getValue() + "");
				count++;
			}
		});

		broadcastIntroduction("Navigate to the end of the map!", "Green carpets are checkpoints.", "Try to beat all the other players!", "", "Map: " + getMap().getMapName());

		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.teleport(getMap().getSpawn());
			p.getInventory().clear();
			p.getInventory().setItem(8, ShowHidePlayerEvents.GREEN_DYE);
			p.getInventory().setItem(0, FeatherEvents.FEATHER);
		});

		doCountdown(() -> {
			deregisterAllEvents();
			registerListeners();
			resetStartTime();
			Bukkit.getOnlinePlayers().forEach((p) -> getStats().getSpawns().put(p.getName(), getMap().getSpawn()));
			Bukkit.getOnlinePlayers().forEach((p) -> getStats().getDeaths().put(p.getName(), 0));
		});
		
	}

	@Override
	protected void end() {
		stopScoreboardUpdates();
		getScoreboard().unshowScoreboardForAll();
		deregisterAllEvents();
		Bukkit.getOnlinePlayers().forEach(ShowHidePlayerEvents::showPlayers);
	}

	public void win(Player winner) {

		this.winner = winner;
		
		stopScoreboardUpdates();

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Thread(() -> {
			winner.setGlowing(false);
			GameController.endCurrentGame();
		}), 20 * 8);

		Bukkit.getOnlinePlayers().forEach(player -> {
			player.teleport(getMap().getSpawn());
			ShowHidePlayerEvents.showPlayers(player);
		});

		boolean isWR = isWorldRecord();
		if (isWR) {
			GameUtils.fireworks(getMap(), 6, 10);
			Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "WORLD RECORD!", getScoreboardFriendlyTime(), 5, 5 * 20, 5));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
				Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + winner.getName() + " won!", getScoreboardFriendlyTime(), 5, 2 * 20, 5));
			}, 3 * 20);
			GameUtils.repeatTask(() -> Bukkit.getOnlinePlayers().forEach(player -> player.setGlowing(true)), 10, 16);
			GameUtils.repeatTask(() -> Bukkit.getOnlinePlayers().forEach(player -> player.setGlowing(false)), 5, 10, 16);
		}
		else {
			GameUtils.fireworks(getMap(), 1, 40);
			winner.setGlowing(true);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + winner.getName() + " won!", getScoreboardFriendlyTime(), 5, 7 * 20, 5);
			}
		}

		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "------------ " + getType().getName() + " ------------");
		Bukkit.broadcastMessage(ChatColor.GRAY + "Winner: " + winner.getName());
		Bukkit.broadcastMessage(ChatColor.GRAY + "Map: " + getMap().getMapName());
		Bukkit.broadcastMessage(ChatColor.GRAY + "Time: " + getScoreboardFriendlyTime() + (isWR ? GREEN + "" + BOLD + " (WORLD RECORD!)" : ""));
		Bukkit.broadcastMessage("");
		for (Entry<String, Integer> entry : getStats().getDeaths().entrySet()) {
			Bukkit.broadcastMessage(ChatColor.GRAY + entry.getKey() + " - " + entry.getValue() + " death" + (entry.getValue() == 1 ? "" : "s"));
		}
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-------------" + getType().getName().replaceAll(".", "-") + "-------------");

	}
	
	public boolean isWorldRecord() {

		long time = System.currentTimeMillis() - getStartTime();

		String currentWrString = null;
		try {
			currentWrString = getMap().getExtraInfo().get(0);
		} catch (Exception e) {
			getMap().getExtraInfo().add(time + "");
			return true;
		}

		if (Long.valueOf(currentWrString) > time) {
			currentWrString = getMap().getExtraInfo().set(0, time + "");
			return true;
		}

		return false;

	}

	public String doStatistics(Player p) {
		String report = "";

		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(p);

		boolean win = false;
		if (getWinner() == null || getWinner().getName().equalsIgnoreCase(p.getName())) {
			win = true;
		}

		int pounds = getTimePounds();

		report += GREEN + "+ " + pounds + " pounds for time played\n";
		pounds += 10;
		report += GREEN + "+ 10 pounds for participation\n";

		if (isWorldRecord()){
			pounds += 150;
			report += GREEN + "+ 150 pounds for new world record\n";
		}

		if (win) {
			pounds += 40;
			report += GREEN + "+ 40 pounds for winning";
		}

		Integer deaths = getStats().getDeaths().get(p.getName());
		if (deaths != null) {
			if (deaths >= 25) {
				pounds += 5;
				report += GREEN + "+ 5 pounds for pity (25+ deaths)\n";
			}
		}

		report += GOLD + BOLD + "= " + pounds + " pounds\n\n";

		int xp = getTimeXP();

		report += GREEN + "+ " + xp + " XP for time played\n";

		if (isWorldRecord()){
			xp += 3000;
			report += GREEN + "+ 3000 XP for new world record\n";
		}

		if (win) {
			xp += 400;
			report += GREEN + "+ 400 XP for winning\n";
		}

		if (deaths != null) {
			xp += 100 - deaths;
			report += GREEN + "+ " + (100 - deaths) + " XP for " + deaths + " deaths\n";
		}

		report += GOLD + BOLD + "= " + xp + " XP\n";

		ps.setCurrency(ps.getCurrency() + pounds);
		ps.setTotalXP(ps.getTotalXP() + xp);

		ParkourRaceStatistics pgs = (ParkourRaceStatistics) ps.getStatistics(getType());
		deaths = pgs.deaths + (deaths == null ? 0 : deaths);
		pgs.setGamesPlayed(pgs.getGamesPlayed() + 1);
		pgs.setWins(win ? pgs.getWins() + 1 : pgs.getWins());
		pgs.setTimePlayed(System.currentTimeMillis() - getStartTime());

		if (win) {
			Long oldTime = pgs.personalBests.get(getMap().getMapName() + " Personal Best");
			long time = System.currentTimeMillis() - getStartTime();
			if (oldTime == null || oldTime > time) {
				pgs.personalBests.put(getMap().getMapName() + " Personal Best", time);
			}
		}
		return report;
	}
	
	public Player getWinner() {
		return this.winner;
	}
	
}

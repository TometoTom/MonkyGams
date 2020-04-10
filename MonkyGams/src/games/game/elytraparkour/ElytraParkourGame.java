package games.game.elytraparkour;

import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import core.Main;
import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import games.meta.Game;
import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;
import games.meta.statistics.ElytraParkourStatistics;
import net.md_5.bungee.api.ChatColor;
import utils.game.GameListeners;
import utils.game.GameUtils;

public class ElytraParkourGame extends Game {

	private Player winner = null;

	public ElytraParkourGame(Map m) {
		super(GameType.ELYTRAPARKOUR, m);
	}
	
	@Override
	public void registerListeners() {
		registerEvent(new DeathEvents());
		registerEvent(new HitEvent());
		registerEvent(new JoinLeaveEvents());
		registerEvent(new ShowHidePlayerEvents());
		registerEvent(GameListeners.getPreventDestroyingBlocks());
		registerEvent(GameListeners.getPreventPlacingBlocks());
		registerEvent(GameListeners.getPreventInventoryMove());
		registerEvent(GameListeners.getPreventItemDrop());
		registerEvent(GameListeners.getPreventFire());
		registerEvent(GameListeners.getPreventRain());
		registerEvent(GameListeners.getPreventFoodChange());
	}

	@Override
	public void start() {

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

		Bukkit.getOnlinePlayers().forEach((p) -> p.teleport(getMap().getSpawn()));

		broadcastIntroduction("Use the Elytra to fly to the end of the map!",
				"You can sneak in the air to boost your flight.",
				"Your XP bar is your boost charge. It refills as you fly.",
				"",
				"Map: " + getMap().getMapName());

		ItemStack elytra = new ItemStack(Material.ELYTRA);
		ItemMeta meta = elytra.getItemMeta();
		meta.setUnbreakable(true);
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Elytra");
		elytra.setItemMeta(meta);
		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.getInventory().clear();
			p.getInventory().setChestplate(elytra);
			p.getInventory().setItem(8, ShowHidePlayerEvents.GREEN_DYE);
			p.setExp(1F);
		});

		doCountdown(() -> {
			deregisterAllEvents();
			registerListeners();
			resetStartTime();
			Bukkit.getOnlinePlayers().forEach((p) -> getStats().getSpawns().put(p.getName(), getMap().getSpawn()));
			Bukkit.getOnlinePlayers().forEach((p) -> getStats().getDeaths().put(p.getName(), 0));
		}, 6, true);

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
		
		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
			String report = doStatistics(p);
			GameUtils.delayTask(() -> p.sendMessage(report), 12 * 20);
		});

	}

	@Override
	public void end() {

		stopScoreboardUpdates();
		getScoreboard().unshowScoreboardForAll();
		deregisterAllEvents();
		Bukkit.getOnlinePlayers().forEach(ShowHidePlayerEvents::showPlayers);

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

	public String greenify(String input) {
		return GREEN + BOLD + input + RESET + GREY;
	}

	public String doStatistics(Player p) {

		String report = "";

		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(p);

		boolean win = false;
		if (getWinner() == null || getWinner().getName().equalsIgnoreCase(p.getName())) {
			win = true;
		}

		int pounds = getTimePounds();

		report += GREY + "+ " + greenify(pounds + " pounds") + " for time played\n";
		pounds += 10;
		report += GREY + "+ " + greenify("10 pounds") + " for participation\n";

		if (isWorldRecord()){
			pounds += 150;
			report += GREY + "+ " + greenify("150 pounds") + " for new world record\n";
		}

		if (win) {
			pounds += 40;
			report += GREY + "+ " + greenify("40 pounds") + " pounds for winning\n";
		}

		Integer deaths = getStats().getDeaths().get(p.getName());
		if (deaths != null) {
			if (deaths >= 25) {
				pounds += 5;
				report += GREY + "+ " + greenify("5 pounds") + " for pity (25+ deaths)\n";
			}
		}

		report += GOLD + BOLD + "= " + pounds + " pounds\n \n";

		int xp = getTimeXP();

		report += GREY + "+ " + greenify(xp + " XP") + " for time played\n";

		if (isWorldRecord()){
			xp += 3000;
			report += GREY + "+ " + greenify("3000 XP") + " for new world record\n";
		}

		if (win) {
			xp += 250;
			report += GREY + "+ " + greenify("250 XP") + " for winning\n";
		}

		if (deaths != null) {
			xp += 100 - deaths;
			report += GREY + "+ " + greenify(100 - deaths + " XP") + " for " + deaths + " death" + (deaths == 1 ? "" : "s") + "\n";
		}

		report += GOLD + BOLD + "= " + xp + " XP\n";

		ps.setCurrency(ps.getCurrency() + pounds);
		int levelBefore = ps.getLevel();
		ps.setTotalXP(ps.getTotalXP() + xp);
		int levelAfter = ps.getLevel();

		if (levelAfter > levelBefore) {
			GameUtils.delayTask(() -> {
				Firework f = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
				FireworkMeta fm = f.getFireworkMeta();
				Random ran = new Random();
				fm.addEffect(FireworkEffect.builder()
						.flicker(false)
						.trail(true)
						.with(Type.STAR)
						.withColor(Color.fromRGB(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)))
						.withFade(Color.fromRGB(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)))
						.build());
				fm.setPower(1);
				f.setFireworkMeta(fm);
				Bukkit.broadcastMessage(GREEN + BOLD + "LEVEL: " + RESET + GREY + p.getName() + " has leveled up! They are now level " + levelAfter + ".");
			}, 60);
		}

		ElytraParkourStatistics pgs = (ElytraParkourStatistics) ps.getStatistics(getType());
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
		return winner;
	}

}

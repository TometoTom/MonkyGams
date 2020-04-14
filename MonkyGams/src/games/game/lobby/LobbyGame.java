package games.game.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import command.meta.PermissionLevel;
import core.Main;
import core.data.RankManager;
import games.meta.Game;
import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyScoreboard;
import utils.game.GameListeners;
import utils.npc.NPC;

public class LobbyGame extends Game {
	
	public static Map lobbyMap;
	
	private int countdown = 30;
	private int countdownRemaining = countdown;
	private int countdownId = -1;
	
	private MonkyScoreboard scoreboard;
	private StackerManager stackerManager = new StackerManager();
	private DrawingManager drawingManager = new DrawingManager();
	private KitEntities kitEntities = new KitEntities();
	
	private NPC hubber;
	
	private GameType queuedGame;
	private Map queuedMap;
	
	public HashMap<String, String> playerNameMapNameVotes = new HashMap<String, String>();
	
	public LobbyGame(GameType type) {
		super(GameType.LOBBY, lobbyMap);
		queuedGame = type;
		queuedMap = null;
	}
	
	public LobbyGame(GameType type, Map m) {
		super(GameType.LOBBY, lobbyMap);
		queuedGame = type;
		queuedMap = m;
	}

	public GameType getQueuedGame() {
		return queuedGame;
	}
	
	public Map getQueuedMap() {
		return queuedMap;
	}
	
	public void setQueuedGame(GameType gt) {
		GameController.endCurrentGame(true, gt);
	}
	
	public void setQueuedMap(Map m) {
		queuedMap = m;
	}
	
	public int getCountdownRemaining() {
		return countdownRemaining;
	}
	
	public int getCountdown() {
		return countdown;
	}
	
	public void setCountdownRemaining(int i) {
		countdownRemaining = i;
	}
	
	@Override
	protected void registerListeners() {
		
		registerEvent(new LobbyListeners());
		registerEvent(new Listener() {
			
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {
				
				LobbyGame game = (LobbyGame) GameController.getCurrentGame();
				if (game.countdownId == -1) {
					game.doLobbyCountdown();
				}
				scoreboard.showScoreboard(e.getPlayer());
				
			}
			
		});
		registerEvent(new Listener() {
			@EventHandler
			public void onQuit(PlayerQuitEvent e) {
				
				playerNameMapNameVotes.remove(e.getPlayer().getName());
				
				LobbyGame game = (LobbyGame) GameController.getCurrentGame();
				if (game.queuedGame.getRequiredPlayers() > Bukkit.getOnlinePlayers().size()) {
					Bukkit.getScheduler().cancelTask(countdownId);
					countdownId = -1;
					Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "LOBBY: " + ChatColor.RESET + "" + ChatColor.GRAY + "Not enough players.");
					
				}
				
			}
		});
		
		registerEvent(new MapChooserEvents());
		registerEvent(new StackerEvents());
		registerEvent(new DrawingChangeColourEvent());
		registerEvent(GameListeners.getPreventItemDrop());
		registerEvent(new GameMenuEvents());
		registerEvent(new KitSelectEvent());
		
	}

	@Override
	protected void start() {
		
		hubber = new NPC(new Location(lobbyMap.getWorld(), 36, 87, -46, 39.45f, 0.61f), "fa31d17fa33a403da432e0b971f5ec6b", GOLD + BOLD + "Go to Hub");
		hubber.setClickEvent((p) -> {
			p.sendMessage(GREEN + BOLD + "SERVER: " + RESET + GREY + "Sending you to Lobby...");
			Main.sendPlayerToServer(p, "Lobby");
		});
		hubber.showForAll();
		hubber.register();
		
		scoreboard = new MonkyScoreboard(ChatColor.GOLD + "" + ChatColor.BOLD + "Lobby");
		scoreboard.addLine("");
		scoreboard.addLine(ChatColor.RED + "" + ChatColor.BOLD + "Game");
		scoreboard.addLine(ChatColor.GRAY + queuedGame.getName());
		scoreboard.addLine("");
		scoreboard.addLine(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map");
		scoreboard.addLine(ChatColor.GRAY + (queuedMap == null ? "No map selected" : queuedMap.mapName));
		scoreboard.addLine("");
		scoreboard.addLine(ChatColor.GREEN + "" + ChatColor.BOLD + "Time Remaining");
		scoreboard.addLine(ChatColor.GRAY + "" + countdownRemaining + " second" + (countdownRemaining == 1 ? "" : "s"));
		scoreboard.showScoreboardToAll();
		
		Bukkit.getOnlinePlayers().forEach(LobbyGame::teleport);
		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setExp(0f);
			p.getInventory().clear();
		});
		Bukkit.getOnlinePlayers().forEach((p) -> {
			ItemStack map = new ItemStack(Material.PAPER);
			ItemMeta meta = map.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Map Voting");
			map.setItemMeta(meta);
			p.getInventory().setItem(8, map);
		});
		Bukkit.getOnlinePlayers().forEach((p) -> {
			if (RankManager.getRankManager().hasRank(p, PermissionLevel.ADMIN))
				p.getInventory().setItem(7, GameMenuEvents.ENDER_EYE);
		});
		drawingManager.setup();
		kitEntities.spawn(lobbyMap.getWorld(), getQueuedGame());
		registerListeners();
		doLobbyCountdown();
		
	}

	@Override
	public void end() {
		
		drawingManager.stop();
		try {
			drawingManager.painter.despawn();
			drawingManager.painter.deregister();
		} catch (Exception e) {
			// Was giving errors before. Idk why
		}
		
		hubber.despawn();
		hubber.deregister();
		
		kitEntities.clearPhysicalSelections(lobbyMap.getWorld());
		
		try { 
			Bukkit.getScheduler().cancelTask(countdownId);
		} catch (Exception e) { };
		
		scoreboard.unshowScoreboardForAll();
		deregisterAllEvents();
		
		lobbyMap.getWorld().getEntitiesByClass(Horse.class).forEach(horse -> horse.remove());
		
	}

	public static void teleport(Player p) {
		
		PlayerInventory i = p.getInventory();
		i.clear(0);
		i.clear(1);
		i.clear(2);
		i.clear(3);
		i.clear(36);
		i.clear(37);
		i.clear(38);
		i.clear(39);
		
		ArrayList<Location> possibleLocations = new ArrayList<Location>();
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 34.12041120335206, 87.0, -23.540316017799242, 116.52222f, 4.725029f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 25.64574437835281, 89.0, -14.558890844581644, 176.59216f, 7.7423277f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 12.10293844958218, 87.0, -11.44113122038279, 211.79297f, 1.524848f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 9.749621712029391, 87.0, -24.31095632702975, 280.1831f, 0.701946f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 32.21894515054898, 87.0, -45.980151757481266, 30.631348f, 1.9820156f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 31.694148362301945, 87.0, -36.11578918195136, 73.51233f, -2.223928f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 15.829302036199579, 87.0, -32.837052489490794, 210.10974f, 3.7192526f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 18.808331925915436, 87.0, -42.97247444816607, 36.435303f, 1.5248575f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 24.039849572124126, 87.0, -37.509075629436225, 18.577637f, -0.21237832f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 22.509331992113108, 87.0, -24.29829843216877, 201.30615f, 2.0734568f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 19.775505510722844, 87.0, -17.160504917405248, 184.62988f, -1.3095852f));
		possibleLocations.add(new Location(Bukkit.getWorld("Lobby"), 31.801952980351203, 87.0, -17.05524167274394, 332.77417f, 4.176428f));
		
		Location l = possibleLocations.get(new Random().nextInt(possibleLocations.size()));
		p.teleport(l);
		
	}
	
	public String getVotedMap() {
		
		if (getQueuedMap() != null) return getQueuedMap().getMapName();
		
		/* Select a map based on votes */
		HashMap<String, Integer> mapVotes = new HashMap<>();
		for (String mapVote : playerNameMapNameVotes.values()) {
			if (mapVotes.containsKey(mapVote)) {
				mapVotes.put(mapVote, mapVotes.get(mapVote) + 1);
			}
			else mapVotes.put(mapVote, 1);
		}
		
		Entry<String, Integer> topMap = null;
		for (Entry<String, Integer> entry : mapVotes.entrySet()) {
			if (topMap == null || topMap.getValue() < entry.getValue()) {
				topMap = entry;
			}
		}
		return topMap == null ? null : topMap.getKey();
	}
	
	public void doLobbyCountdown() {
		
		/* Reset countdown. The countdown can happen more than once in one Lobby game. */
		countdownRemaining = countdown;
		
		countdownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
						
			if (queuedGame.getRequiredPlayers() > Bukkit.getOnlinePlayers().size()) {
				Bukkit.broadcastMessage("Not enough players.");
				Bukkit.getScheduler().cancelTask(countdownId);
				countdownId = -1;
				return;
			}
			
			if (countdownRemaining <= 0) {
				
				Bukkit.getScheduler().cancelTask(countdownId);
				countdownId = -1;
				
				/* Select a map based on votes */
				String topMap = getVotedMap();
				
				/* If no votes, select a random one. */
				if (topMap == null) {
					ArrayList<Map> maps = Map.getMaps(queuedGame);
					queuedMap = maps.get(new Random().nextInt(maps.size()));
				}
				else {
					queuedMap = Map.getMap(topMap, queuedGame);
				}
				
				if (queuedMap == null) {
					Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME: " + ChatColor.RESET + "" + ChatColor.GRAY + "Could not start a game as there are no maps available.");
					doLobbyCountdown();
				}
				else {
					GameController.startNewGame(queuedGame, queuedMap);
				}
			}
			
			if (countdownRemaining <= 5) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.playSound(p.getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 10, 2);
				}
			}
		
			scoreboard.setLine(2, ChatColor.GRAY + queuedGame.getName());
			scoreboard.setLine(5, ChatColor.GRAY + (queuedMap == null ? "No map selected" : queuedMap.mapName));
			scoreboard.setLine(8, ChatColor.GRAY + "" + countdownRemaining + " second" + (countdownRemaining == 1 ? "" : "s"));
			
			Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(countdownRemaining));
			countdownRemaining--;
			
		}, 0, 20);
		
	}

	public StackerManager getStackerManager() {
		return stackerManager;
	}
	public DrawingManager getDrawingManager() {
		return drawingManager;
	}
}

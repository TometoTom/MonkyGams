package core;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import command.meta.CommandManager;
import core.data.DatabaseManager;
import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import core.events.ChatEvents;
import core.events.ConsistentEvents;
import core.events.PingEvent;
import core.events.SignTPEvent;
import core.events.StatisticEvents;
import core.events.StatsGUIEvents;
import games.game.elytrabattle.ElytraBattleKits;
import games.game.elytraparkour.Boost;
import games.game.lobby.LobbyGame;
import games.meta.GameController;
import games.meta.GameJoinEvent;
import games.meta.GameType;
import games.meta.Map;
import hub.HubManager;
import hub.gadgets.meta.GadgetUseEvent;
import net.md_5.bungee.api.ChatColor;
import utils.SerialisableLocation;
import utils.game.GameUtils;
import utils.gui.ClickEventHandler;
import utils.npc.NPCInteractEvent;
import utils.npc.NPCManager;

public class Main extends JavaPlugin {

	public static String getPluginName() {
		return "MonkyGams";
	}
	private static Plugin plugin;
	public Main() {
		plugin = this;
	}
	public static Plugin getPlugin() {
		return plugin;
	}

	public static String LOBBY_WORLD_NAME = "lobby";
	private static boolean isHubMode = Bukkit.getServer().getMotd().equalsIgnoreCase("Lobby Server");

	@Override
	public void onEnable() {
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		Bukkit.getPluginManager().registerEvents(new ChatEvents(), this);
		Bukkit.getPluginManager().registerEvents(new PingEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SignTPEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ConsistentEvents(), this);
		Bukkit.getPluginManager().registerEvents(new StatisticEvents(), this);
		Bukkit.getPluginManager().registerEvents(new StatsGUIEvents(), this);
		Bukkit.getPluginManager().registerEvents(new NPCInteractEvent(), this);
		Bukkit.getPluginManager().registerEvents(new GameJoinEvent(), this);
		Bukkit.getPluginManager().registerEvents(new GadgetUseEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ClickEventHandler(), this);
		
		CommandManager.registerCommands();

		getConfig().options().copyDefaults(true);
		saveConfig();

		Bukkit.getOnlinePlayers().forEach((p) -> {
			try {
				PlayerStatisticsManager.addStatistics(DatabaseManager.getPlayerData(p.getUniqueId().toString()));
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getLogger().log(Level.SEVERE, "Failed to obtain database information for " + p.getName());
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "SERVER: " + ChatColor.RESET + "" + ChatColor.GRAY + "Failed to load your statistics. I suggest relogging to fix this.");
			}
		});
		
		NPCManager.readCache();
		
		/* Only applicable for game servers */
		if (!isHubMode()) {
			
			Boost.setup();
			
			Type array = new TypeToken<ArrayList<Map>>() {}.getType();
			String configMaps = getConfig().getString("maps");
			Map.maps = new Gson().fromJson(configMaps == null ? "[]" : configMaps, array);
			
			/* This is in a scheduler because only 'world' loads and then it loads plugins. We need 'Lobby'. So we wait! */
			Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
				LobbyGame.lobbyMap = new Map("Lobby", "Tom", new SerialisableLocation(Bukkit.getWorld("Lobby").getSpawnLocation()), GameType.LOBBY, null);
				GameController.startNewGame(GameType.LOBBY, null);
			}, 1);
		}
		
		/* Only applicable for hub servers */
		else {
			GameUtils.delayTask(() -> HubManager.setup(), 1);
		}

		ElytraBattleKits.setup();
		
	}

	@Override
	public void onDisable() {
		
		GameController.endCurrentGame(false, null);
		
		NPCManager.getNpcs().forEach(npc -> npc.despawn());
		
		try {
			DatabaseManager.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (PlayerStatistics ps : PlayerStatisticsManager.getPlayerStatistics()) {
			DatabaseManager.updatePlayerData(ps);
		}
		
		NPCManager.saveCache();
		getConfig().set("maps", new Gson().toJson(Map.maps));
		saveConfig();

	}
	public static boolean isHubMode() {
		return isHubMode;
	}
	public static void setLobbyMode(boolean isHubMode) {
		Main.isHubMode = isHubMode;
	}
	
	public static void sendPlayerToServer(Player p, String serverName) {

		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		out.writeUTF("ConnectOther");
		out.writeUTF(p.getName());
		out.writeUTF(serverName);

		p.sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
	}
	
}

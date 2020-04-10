package hub;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import core.Main;
import net.md_5.bungee.api.ChatColor;
import utils.game.GameUtils;
import utils.npc.NPC;

public class HubManager {

	private static String HUB_WORLD_NAME;
	
	public static void setup() {
		
		HUB_WORLD_NAME = Main.getPlugin().getConfig().getString("hubWorldName");
		
		Bukkit.getWorld(HUB_WORLD_NAME).getEntitiesByClass(Horse.class).forEach(horse -> horse.remove());
		
		Bukkit.getPluginManager().registerEvents(new DoubleJump(), Main.getPlugin());
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {
				teleportSpawn(e.getPlayer());
			}
		}, Main.getPlugin());
		Bukkit.getPluginManager().registerEvents(new OptimisationEvents(), Main.getPlugin());
		
		NPC gameSelector = new NPC(new Location(Bukkit.getWorld(HUB_WORLD_NAME), 50.5, 161.5, 18.5, 0.091522835f, -8.503474f),
				"0ca2aa1dcf164364a39d1b4a2725be4e",
				ChatColor.GOLD + "Elytra Parkour");
		gameSelector.showForAll();
		gameSelector.register();
		gameSelector.setClickEvent((p) -> {
			p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SERVER: " + ChatColor.GRAY + "Sending you to Server EP...");
			p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3, 1);
			p.spawnParticle(Particle.EXPLOSION_HUGE, gameSelector.getLocation(), 1, 0, 0, 0);
			GameUtils.delayTask(() -> Main.sendPlayerToServer(p, "EP"), 20);
		});
		
	}
	
	public static void teleportSpawn(Player p) {
		
		p.teleport(Bukkit.getWorld(HUB_WORLD_NAME).getSpawnLocation());
		
	}

	public static String getHubWorldName() {
		return HUB_WORLD_NAME;
	}

}

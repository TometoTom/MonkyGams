package games.game.parkourrace;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import games.meta.GameController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class DeathEvents implements Listener {

	public Material winBlock = Material.RED_CARPET;
	public Material checkpointBlock = Material.LIME_CARPET;
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		
		if (e.getTo().getY() <= 100) {
			respawn(e.getPlayer());
			return;
		}
		
		Material m = e.getPlayer().getLocation().getBlock().getType();
		Player p = e.getPlayer();
		
		if (m==winBlock){
			win(p);
			return;
		}
		if (m == checkpointBlock) {
			HashMap<String, Location> spawnPoints = GameController.getCurrentGame().getStats().getSpawns();
			
			if (spawnPoints.containsKey(p.getName())) {
				Location prevSpawn = spawnPoints.get(p.getName());
				int x = (int) prevSpawn.getX();
				int y = (int) prevSpawn.getY();
				int z = (int) prevSpawn.getZ();
				
				Location newSpawn = p.getLocation();
				int newX = (int) newSpawn.getX();
				int newY = (int) newSpawn.getY();
				int newZ = (int) newSpawn.getZ();
				
				if (x == newX && y == newY && z == newZ) {
					return;
				}
				else {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 9);
					Bukkit.getOnlinePlayers().forEach(each -> each.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "CHECKPOINTS: " + ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + " reached the checkpoint!"));
					spawnPoints.put(p.getName(), p.getLocation());
				}
			}

			return;
		}
		
		if (m == Material.LAVA || m == Material.WATER) {
			respawn(p);
			return;
		}
		
	}
	
	public void win(Player p) {
		
		((ParkourRaceGame) GameController.getCurrentGame()).win(p);
		return;
		
	}
	
	public static void respawn(Entity e) {
		Player p = (Player) e;
		p.setHealth(20);
		p.setFoodLevel(20);
		p.playSound(p.getLocation(), Sound.ITEM_AXE_STRIP, 10, 2);
		p.setGliding(false);
		p.setFireTicks(2);
		
		ParkourRaceGame g = GameController.getCurrentGame(ParkourRaceGame.class);
		
		Location spawn = g.getStats().getSpawns().get(p.getName());
		
		if (spawn == null) {
			spawn = g.getMap().getSpawn();
		}
		
		p.teleport(spawn);
		
		TextComponent tc = new TextComponent();
		tc.setText(ChatColor.GOLD + p.getName() + " died!");
		Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, tc));
		
		g.getStats().getDeaths().increment(p.getName());
	}
	
}

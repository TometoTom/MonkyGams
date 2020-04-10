package games.game.dropper;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import games.meta.GameController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class DeathEvents implements Listener {

	public Material winBlock = Material.RED_CARPET;
	public Material checkpointBlock = Material.LIME_CARPET;

	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if (e.toWeatherState()) e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {

		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e){

		e.setCancelled(true);

		if (e.getCause() == DamageCause.FALL) {
			respawn(e.getEntity());
		}

	}

	@EventHandler
	public void onMove(PlayerMoveEvent e){

		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

		Material m = e.getPlayer().getLocation().getBlock().getType();
		Player p = e.getPlayer();

		if (m==winBlock){
			win(p);
			return;
		}
		if (m == checkpointBlock) {

			Block blockBeneath = e.getPlayer().getLocation().subtract(0, 1, 0).getBlock();

			if (blockBeneath.getType() != Material.OAK_SIGN) {
				e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "CHECKPOINTS: " + ChatColor.RESET + "" + ChatColor.GRAY + "This checkpoint isn't configured correctly.");
				return;
			}

			Sign sign = (Sign) blockBeneath.getState();

			Location newSpawn = null;

			try {
				String line = sign.getLine(0).replaceAll("[\\[\\] ]", "");
				String[] coords = line.split(",");
				
				String line2 = sign.getLine(1).replaceAll("[\\[\\] ]", "");
				String[] lookDirection = line2.split(",");
				
				newSpawn = new Location(e.getPlayer().getWorld(), Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2]), Float.valueOf(lookDirection[0]), Float.valueOf(lookDirection[1]));
				e.getPlayer().teleport(newSpawn);

			} catch (Exception ex) {
				e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "CHECKPOINTS: " + ChatColor.RESET + "" + ChatColor.GRAY + "This checkpoint isn't configured correctly."
						+ " It must be in the following format:\n[x, y, z]\n[yaw, pitch]");
				return;
			}

			HashMap<String, Location> spawnPoints = ((DropperGame) GameController.getCurrentGame()).getSpawnPoints();
			HashMap<String, Integer> stages = ((DropperGame) GameController.getCurrentGame()).getStages();
			
			if (!stages.containsKey(e.getPlayer().getName())) stages.put(e.getPlayer().getName(), 1);
			else stages.put(p.getName(), stages.get(p.getName()) + 1);
			
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 9);
			Bukkit.getOnlinePlayers().forEach(each -> each.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "CHECKPOINTS: " + ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + " reached level " + stages.get(p.getName())));
			
			spawnPoints.put(p.getName(), p.getLocation());

			return;
		}

	}

	public void win(Player p) {

		((DropperGame) GameController.getCurrentGame()).win(p);
		return;

	}

	public static void respawn(Entity e) {
		Player p = (Player) e;
		p.setHealth(20);
		p.setFoodLevel(20);
		p.playSound(p.getLocation(), Sound.ITEM_AXE_STRIP, 10, 2);
		p.setFireTicks(1);

		DropperGame g = ((DropperGame) GameController.getCurrentGame());

		Location spawn = g.getSpawnPoints().get(p.getName());

		if (spawn == null) {
			spawn = g.getMap().getSpawn();
		}

		p.teleport(spawn);

		TextComponent tc = new TextComponent();
		tc.setText(ChatColor.GOLD + p.getName() + " died!");
		Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, tc));

		g.getDeaths().put(p.getName(), g.getDeaths().containsKey(p.getName()) ? g.getDeaths().get(p.getName()) + 1 : 0);
	}

}

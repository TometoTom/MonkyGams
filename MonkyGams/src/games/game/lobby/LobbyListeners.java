package games.game.lobby;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import command.meta.PermissionLevel;
import core.data.RankManager;
import net.md_5.bungee.api.ChatColor;
import utils.game.GameUtils;

public class LobbyListeners implements Listener {

	public static ArrayList<String> pvpArea = new ArrayList<String>();
	public static ArrayList<String> dmtArea = new ArrayList<String>();

	@EventHandler
	public void onExplodeEvent(EntityExplodeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {

		if (RankManager.getRankManager().hasRank(e.getPlayer(), PermissionLevel.OWNER) && e.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		e.setCancelled(true);

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		if (RankManager.getRankManager().hasRank(e.getPlayer(), PermissionLevel.OWNER) && e.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		e.setCancelled(true);

	}

	@EventHandler
	public void onPlayerDeathEvent(EntityDamageEvent e) {
		if (e.getEntity().getType() != EntityType.PLAYER) {
			return;
		}

		e.setCancelled(true);
		
		Player p = (Player) e.getEntity();
		if (p.getHealth() - e.getDamage() < 1
				&& e.getCause() != DamageCause.FALL) {
			p.setHealth(20);
			LobbyGame.teleport(p);
			GameUtils.doDeathEffect(p);
		}
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
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity().getLocation().getY() > 80) e.setCancelled(true);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if (GameUtils.areLocationsEqual(e.getTo(), e.getFrom())) {
			return;
		}
		
		Player p = e.getPlayer();
		Location to = e.getTo();
		
		if (to.getY() < 0) {
			p.setHealth(20);
			LobbyGame.teleport(p);
			GameUtils.doDeathEffect(p);
			return;
		}

		if (GameUtils.isWithinLocations(to, new Location(Bukkit.getWorld("Lobby"), 32, 74, -52), new Location(Bukkit.getWorld("Lobby"), 5, 81, -25))) {

			if (!pvpArea.contains(e.getPlayer().getName())) {
				PlayerInventory i = e.getPlayer().getInventory();
				i.setItem(0, new ItemStack(Material.STONE_SWORD));
				i.setItem(1, new ItemStack(Material.BOW));
				i.setItem(2, new ItemStack(Material.FISHING_ROD));
				ItemStack arrow = new ItemStack(Material.ARROW);
				arrow.setAmount(64);
				i.setItem(3, arrow);

				i.setHelmet(new ItemStack(Material.IRON_HELMET));
				i.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				i.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				i.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
				pvpArea.add(e.getPlayer().getName());
			}

		}
		
		else {
			if (pvpArea.remove(e.getPlayer().getName())) {
				PlayerInventory i = e.getPlayer().getInventory();
				i.remove(Material.STONE_SWORD);
				i.remove(Material.BOW);
				i.remove(Material.FISHING_ROD);
				i.remove(Material.ARROW);
				i.setBoots(null);
				i.setLeggings(null);
				i.setChestplate(null);
				i.setHelmet(null);
				
			}
		}

		if (GameUtils.isWithinLocations(to, new Location(Bukkit.getWorld("Lobby"), 9, 95, 22) ,new Location(Bukkit.getWorld("Lobby"), 29, 81, 10))) {
			
			if (!dmtArea.contains(e.getPlayer().getName())) {
				ItemStack sword = new ItemStack(Material.SPECTRAL_ARROW);
				ItemMeta meta = sword.getItemMeta();
				String b = ChatColor.BOLD.toString();
				meta.setDisplayName(ChatColor.RED + b + "P" + ChatColor.GOLD + b + "a" + ChatColor.YELLOW + b + "i" + ChatColor.GREEN + b + "n"
						+ ChatColor.DARK_GREEN + b + "t" + ChatColor.AQUA + b + "b" + ChatColor.DARK_AQUA + b + "r" + ChatColor.BLUE + b + "u"
						+ ChatColor.LIGHT_PURPLE + b + "s" + ChatColor.DARK_PURPLE + b + "h");
				meta.setLore(Arrays.asList(ChatColor.GRAY + "Sneak with this in your hand to draw on a canvas!"));
				sword.setItemMeta(meta);
				e.getPlayer().getInventory().addItem(sword);
				dmtArea.add(e.getPlayer().getName());
			}
			
		}
		else {
			if (dmtArea.remove(e.getPlayer().getName())) {
				e.getPlayer().getInventory().remove(Material.SPECTRAL_ARROW);
			}
		}

	}

}

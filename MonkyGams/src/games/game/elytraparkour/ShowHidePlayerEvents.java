package games.game.elytraparkour;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import core.Main;
import net.md_5.bungee.api.ChatColor;

public class ShowHidePlayerEvents implements Listener {

	static {
		
		ItemStack green = new ItemStack(Material.LIME_DYE);
		ItemMeta meta = green.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Show/Hide Players");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Use this to show or hide players on your screen."));
		green.setItemMeta(meta);
		
		ItemStack grey = new ItemStack(Material.GRAY_DYE);
		grey.setItemMeta(meta);
		
		GREEN_DYE = green;
		GREY_DYE = grey;
	}
	
	public static ItemStack GREEN_DYE;
	public static ItemStack GREY_DYE;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		if (!e.hasItem()) return;
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		
		if (e.getHand() != EquipmentSlot.HAND) return;
		
		if (e.getItem().getType() == Material.LIME_DYE) {
			hidePlayers(e.getPlayer());
			e.getPlayer().getInventory().setItem(8, GREY_DYE);
		}
		else if (e.getItem().getType() == Material.GRAY_DYE) {
			showPlayers(e.getPlayer());
			e.getPlayer().getInventory().setItem(8, GREEN_DYE);
		}
		
	}
	
	public static void hidePlayers(Player p) {
		
		Bukkit.getOnlinePlayers().forEach(otherPlayer -> p.hidePlayer(Main.getPlugin(), otherPlayer));
		
	}
	
	public static void showPlayers(Player p) {
		
		Bukkit.getOnlinePlayers().forEach(otherPlayer -> p.showPlayer(Main.getPlugin(), otherPlayer));
		
	}
	
}

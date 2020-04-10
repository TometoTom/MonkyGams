package games.game.parkourrace;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class FeatherEvents implements Listener {

	static {

		ItemStack feather = new ItemStack(Material.FEATHER);
		ItemMeta meta = feather.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Respawn");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Use this to quickly jump back to your last respawn point."));
		feather.setItemMeta(meta);
		
		FEATHER = feather;

	}

	public static ItemStack FEATHER;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		if (!e.hasItem()) return;
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if (e.getHand() != EquipmentSlot.HAND) return;
		if (e.getItem().getType() == Material.FEATHER) {
			DeathEvents.respawn(e.getPlayer());
		}
		
	}

}

package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class WaterLavaPlaceEvents implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.LAPIS_BLOCK) {
			e.getBlock().setType(Material.WATER);
			e.getPlayer().playSound(e.getBlock().getLocation(), Sound.ENTITY_BOAT_PADDLE_WATER, 10, 1);
		}
		if (e.getBlock().getType() == Material.REDSTONE_BLOCK) {
			e.getBlock().setType(Material.LAVA);
			e.getPlayer().playSound(e.getBlock().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 10, 1);
		}
		if (e.getBlock().getType() == Material.TNT) {
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.COBWEB) {
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.FIRE_CHARGE) {
			e.setCancelled(true);
		}
	}
	
}

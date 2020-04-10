package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;

@SuppressWarnings("deprecation")
public class CobwebPhysics implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.COBWEB) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (!e.hasItem()) return;
		if (e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		
		if (i.getType() == Material.COBWEB) {
			e.setCancelled(true);
			FallingBlock fb = (FallingBlock) e.getPlayer().getWorld().spawnFallingBlock(p.getLocation().add(0, 1, 0), new MaterialData(Material.COBWEB));
			fb.setVelocity(p.getLocation().add(0, 1, 0).getDirection().multiply(1.1));
			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).cobwebsThrown++;
			
			if (i.getAmount() > 1) {
				i.setAmount(i.getAmount() - 1);
				p.updateInventory();
			}
			else 
				p.getInventory().removeItem(i);
		}
		
	}
	
}

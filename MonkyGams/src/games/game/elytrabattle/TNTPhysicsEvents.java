package games.game.elytrabattle;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;

@SuppressWarnings("deprecation")
public class TNTPhysicsEvents implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.TNT) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (!e.hasItem()) return;
		if (e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		
		if (i.getType() == Material.TNT) {
			e.setCancelled(true);
			TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.PRIMED_TNT);
			tnt.setGlowing(true);
			tnt.setVelocity(p.getLocation().add(0, 1, 0).getDirection().multiply(1.1));
			tnt.setFuseTicks(40);
			
			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).tntThrown++;
			
			if (i.getAmount() > 1) {
				i.setAmount(i.getAmount() - 1);
				p.updateInventory();
			}
			else 
				p.getInventory().removeItem(i);
		}
		
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		
		e.setYield(0);
		
		List<Block> blockList = e.blockList();
		blockList.forEach(b -> {
			if (b.isLiquid()) return;
			
			FallingBlock fb = (FallingBlock) b.getWorld().spawnFallingBlock(b.getLocation(), new MaterialData(b.getType()));
			
			Location centre = e.getEntity().getLocation();
			Location block = b.getLocation();
			fb.setVelocity(new Vector(block.getX() - centre.getX(), block.getY() - centre.getY() + 1, block.getZ() - centre.getZ()).multiply(0.3));
			
			b.setType(Material.AIR);
		});
		
	}
	
}

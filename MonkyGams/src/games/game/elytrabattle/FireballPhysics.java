package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;

public class FireballPhysics implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (!e.hasItem()) return;
		if (e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		
		if (i.getType() == Material.FIRE_CHARGE) {
			e.setCancelled(true);
			Fireball fb = (Fireball) e.getPlayer().getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.FIREBALL);
			fb.setDirection(p.getLocation().getDirection().multiply(0.1));

			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).fireballsThrown++;
			
			if (i.getAmount() > 1) {
				i.setAmount(i.getAmount() - 1);
				p.updateInventory();
			}
			else 
				p.getInventory().removeItem(i);
		}
		
	}
	
}

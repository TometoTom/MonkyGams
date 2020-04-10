package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;

public class CoalInteractEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (!e.hasItem()) return;
		if (e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		
		if (i.getType() == Material.COAL) {
			p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 10, 1);
			p.setExp(p.getExp() + 0.45F > 1 ? 1 : p.getExp() + 0.45F);
			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).coalUsed++;
			if (i.getAmount() > 1) {
				i.setAmount(i.getAmount() - 1);
				p.updateInventory();
			}
			else 
				p.getInventory().removeItem(i);
		}

	}

}

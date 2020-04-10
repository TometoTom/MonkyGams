package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;

public class SoupEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (!e.hasItem()) return;
		if (e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		
		if (i.getType() == Material.MUSHROOM_STEW) {
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4 * 20, 1, false, false, false));
			p.playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 10, 1);
			p.getInventory().removeItem(i);
			p.setFoodLevel(p.getFoodLevel() > 15 ? 20 : p.getFoodLevel() + 5);
			p.setSaturation(3);
			
			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).soupUsed++;
		}
		
	}
	
}

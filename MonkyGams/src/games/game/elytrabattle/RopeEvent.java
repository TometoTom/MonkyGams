package games.game.elytrabattle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;
import utils.game.GameUtils;

public class RopeEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (!e.hasItem()) return;
		if (e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();
		ItemStack i = e.getItem();

		if (i.getType() != Material.LEAD) return;

		e.setCancelled(true);

		Chicken c = (Chicken) p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
		c.setInvulnerable(true);
		c.setAI(false);
		c.setBaby();
		GameUtils.setInvisible(c);
		c.setSilent(true);
		c.addPassenger(p);
		
		Location location = p.getLocation();
		for (int y = 0; y != 20; y++) {
			int relativeY = y * 3 / 20;
			GameUtils.delayTask(() -> {
				for (int degree = 0; degree < 6; degree++) {
					double radians = Math.toRadians(degree * 60);
					double x = Math.cos(radians);
					double z = Math.sin(radians);
					location.add(x, relativeY, z);
					location.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, location, 1);
					p.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 2, 1);
					location.subtract(x, relativeY, z);
				}
			}, y * 4);
		}
		GameUtils.delayTask(() -> {
			p.teleport(location.getWorld().getHighestBlockAt(location).getLocation().add(0, 1, 0));
			p.sendMessage(GameUtils.getSuccessMessage("GAME", "You climbed to the surface!"));
			p.playSound(p.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 10, 2);
			p.spawnParticle(Particle.FLASH, location, 1);
			c.removePassenger(p);
			c.remove();
		}, 50);

		PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).ropesUsed++;
		
		if (i.getAmount() > 1) {
			i.setAmount(i.getAmount() - 1);
			p.updateInventory();
		}
		else 
			p.getInventory().removeItem(i);

	}

}

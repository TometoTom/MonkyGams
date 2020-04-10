package games.game.elytrabattle;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;

public class DamageEvent implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		
		if (!(e.getDamager() instanceof Player && e.getEntity() instanceof Player)) return;
		
		Player damager = (Player) e.getDamager();
		
		Material item = damager.getInventory().getItemInMainHand().getType();
		if (item == Material.WOODEN_AXE || item == Material.STONE_AXE || item == Material.GOLDEN_AXE || item == Material.IRON_AXE || item == Material.DIAMOND_AXE) {
			e.setDamage(1);
		}
		
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		
		EntityType type = e.getEntityType();
		
		if (type == EntityType.PLAYER) return;
		
		e.getDrops().clear();
		e.setDroppedExp(0);
		
		if (type == EntityType.SHEEP) {
			e.getDrops().add(new MonkyItemStack(Material.COOKED_MUTTON).setNumber(3));
		}
		if (type == EntityType.COW) {
			e.getDrops().add(new MonkyItemStack(Material.COOKED_BEEF).setNumber(3));
		}
		if (type == EntityType.CHICKEN) {
			e.getDrops().add(new MonkyItemStack(Material.COOKED_CHICKEN).setNumber(3));
		}
		if (type == EntityType.PIG) {
			e.getDrops().add(new MonkyItemStack(Material.COOKED_PORKCHOP).setNumber(3));
		}
		if (type == EntityType.SALMON) {
			e.getDrops().add(new MonkyItemStack(Material.COOKED_SALMON).setNumber(3));
		}
		if (type == EntityType.COD) {
			e.getDrops().add(new MonkyItemStack(Material.COOKED_COD).setNumber(3));
		}
		if (type == EntityType.TROPICAL_FISH) {
			e.getDrops().add(new MonkyItemStack(Material.COOKED_COD).setNumber(3));
		}
		if (type == EntityType.BAT) {
			e.getDrops().add(new MonkyItemStack(Material.COAL).setNumber(new Random().nextInt(3) + 1));
		}
		if (type == EntityType.RABBIT) {
			if (e.getEntity().getKiller() == null) return;
			e.setDroppedExp(0);
			e.getEntity().getKiller().sendTitle(ChatColor.DARK_RED + "Do not kill innocent creatures.", "", 5, 5 * 20, 5);
			e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
		}

	}
	
}

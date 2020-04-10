package core.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import command.meta.PermissionLevel;
import core.data.RankManager;
import net.md_5.bungee.api.ChatColor;
import utils.npc.NPC;
import utils.npc.NPCManager;

/**
 * 
 * @author Tom
 * Events which are consistent across all games and lobbies.
 */
public class ConsistentEvents implements Listener {

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		
		if (!e.getFrom().getWorld().getUID().toString().equalsIgnoreCase(e.getTo().getWorld().getUID().toString())) {
			
			for (NPC npc : NPCManager.getNpcs()) {
				if (npc.getLocation().getWorld().getUID().toString().equalsIgnoreCase(e.getTo().getWorld().getUID().toString())) {
					npc.show(e.getPlayer());
				}
			}

		}
		
	}
	
	@EventHandler
	public void onRodLand(ProjectileHitEvent e) {
		if(!(e.getEntityType() == EntityType.FISHING_HOOK)) {
			return;
		}
		for(Entity entity : Bukkit.getWorld(e.getEntity().getLocation().getWorld().getName()).getNearbyEntities(e.getEntity().getLocation(), 0.2, 0.2, 0.2)) {
			if(!(entity instanceof Player)) {
				continue;
			}
			FishHook hook = (FishHook) e.getEntity();
			Player rodder = (Player) hook.getShooter();
			Player player = (Player) entity;
			if(player.getName().equalsIgnoreCase(rodder.getName())) {
				continue;
			}
			player.damage(0.2);
			Location loc = player.getLocation().add(0, 0.5, 0);
			player.teleport(loc);
			player.setVelocity(rodder.getLocation().getDirection().multiply(+0.4));
			hook.remove();
			rodder.updateInventory();
			return;
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player player = e.getPlayer();
		String[] lines = e.getLines();

		if (RankManager.getRankManager().hasRank(player, PermissionLevel.BUILDER)) {
			int count = 0;
			for (String line : lines) {
				String colour = ChatColor.translateAlternateColorCodes('&', line);
				e.setLine(count, colour);
				count++;
			}
		}

	}

}

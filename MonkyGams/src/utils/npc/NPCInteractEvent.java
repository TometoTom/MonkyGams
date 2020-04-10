package utils.npc;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCInteractEvent implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onClick(PlayerInteractEntityEvent e) {
		
		if (e.getRightClicked().getType() == EntityType.HORSE) {
			
			for (NPC npc : NPCManager.getNpcs()) {
				if (((Horse) e.getRightClicked()).getUniqueId().toString().equalsIgnoreCase(npc.getHorse().getUniqueId().toString())){
					e.setCancelled(true);
					npc.getClickEvent().onClick(e.getPlayer());
				}
			}
		}

	}

}

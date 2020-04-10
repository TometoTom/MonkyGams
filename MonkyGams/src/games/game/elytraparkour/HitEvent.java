package games.game.elytraparkour;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitEvent implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}

}

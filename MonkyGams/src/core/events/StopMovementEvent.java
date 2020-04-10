package core.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class StopMovementEvent implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if (e.getFrom().distance(e.getTo()) != 0) {
			e.setCancelled(true);
		}

	}
	
}

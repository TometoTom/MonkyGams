package hub;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJump implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().setAllowFlight(false);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.getPlayer().setAllowFlight(false);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if (e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
			e.getPlayer().setAllowFlight(true);
		}
		
	}
	
	@EventHandler
	public void onFlight(PlayerToggleFlightEvent e) {
		
		Player p = e.getPlayer();
		
		if (p.getGameMode() != GameMode.SURVIVAL) return;
		
		e.setCancelled(true);
		p.setAllowFlight(false);
		
		if (!p.getWorld().getName().equalsIgnoreCase(HubManager.getHubWorldName())) return;
		if (p.getLocation().subtract(0, 2, 0).getBlock().getType() != Material.AIR) {
			p.setVelocity(p.getLocation().getDirection().multiply(1.5).setY(1));
		}
		
	}
	
}

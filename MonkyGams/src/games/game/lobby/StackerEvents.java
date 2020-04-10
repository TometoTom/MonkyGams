package games.game.lobby;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import games.meta.GameController;

public class StackerEvents implements Listener {

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		
		((LobbyGame) GameController.getCurrentGame()).getStackerManager().removePlayerFromStack(e.getPlayer());
		
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent e) {
		
		if (e.getRightClicked().getType() == EntityType.PLAYER) {
			((LobbyGame) GameController.getCurrentGame()).getStackerManager().stackPlayer(e.getPlayer(), ((Player) e.getRightClicked()));
		}
		
	}
	
	@EventHandler
	public void onLeftClick(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.LEFT_CLICK_AIR) {
			((LobbyGame) GameController.getCurrentGame()).getStackerManager().throwPlayer(e.getPlayer().getName());
		}
		
	}
	
}

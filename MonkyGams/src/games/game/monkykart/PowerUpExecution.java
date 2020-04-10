package games.game.monkykart;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PowerUpExecution {

	public void doPowerUp(MonkyKartGame game, Player p);
	
}

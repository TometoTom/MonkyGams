package games.game.elytrabattle;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import games.meta.GameController;

public class JoinEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setHealth(20);
		p.setFoodLevel(20);
		p.getInventory().clear();
		p.setExp(0);
		p.setLevel(0);
		p.setGameMode(GameMode.SPECTATOR);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		ElytraBattleGame game = GameController.getCurrentGame(ElytraBattleGame.class);
		
		ArrayList<Player> alivePlayers = game.getAlivePlayers();
		if (alivePlayers.size() == 1) {
			game.win(alivePlayers.get(0));
		}
		else if (alivePlayers.size() == 0) {
			game.win(null);
		}
	}
	
}

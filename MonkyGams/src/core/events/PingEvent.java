package core.events;

import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import core.Main;
import games.game.lobby.LobbyGame;
import games.meta.Game;
import games.meta.GameController;
import net.md_5.bungee.api.ChatColor;

public class PingEvent implements Listener {

	@EventHandler
	public void ServerListPing(ServerListPingEvent e) {
		e.setMaxPlayers(new Random().nextInt(100000) + 10);
		
		if (Main.isHubMode()) {
			e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "===== Tom's Server =====\n" + ChatColor.GRAY + "" + ChatColor.BOLD + "Elytra Parkour & Parkour Race");
			return;
		}
		
		Game g = GameController.getCurrentGame();
		
		if (g instanceof LobbyGame) {
			
			LobbyGame lg = (LobbyGame) g;
			
			if (lg.getCountdown() == lg.getCountdownRemaining()) e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "In lobby - Waiting for players");
			else e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "In lobby - " + lg.getCountdownRemaining() + " seconds remaining");
		}
		
		else {
			
			e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "In game - " + g.getType().getName() + " - " + g.getMap().mapName);
			
		}
	}
	
}

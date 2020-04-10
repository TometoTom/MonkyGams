package games.game.dropper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import games.game.elytraparkour.ShowHidePlayerEvents;
import games.game.parkourrace.FeatherEvents;
import games.meta.GameController;
import net.md_5.bungee.api.ChatColor;

public class JoinLeaveEvents implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		DropperGame game = (DropperGame) GameController.getCurrentGame();
		e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "GAME: " + ChatColor.RESET + "" + ChatColor.GRAY + "You have joined a game which is already in progress.");
		game.getScoreboard().showScoreboard(e.getPlayer());

		if (!game.getDeaths().containsKey(e.getPlayer().getName())) game.getDeaths().put(e.getPlayer().getName(), 0);
		if (!game.getStages().containsKey(e.getPlayer().getName())) game.getStages().put(e.getPlayer().getName(), 1);
		
		e.getPlayer().teleport(game.getMap().getSpawn());
		e.getPlayer().getInventory().clear();
		e.getPlayer().getInventory().setItem(8, ShowHidePlayerEvents.GREEN_DYE);
		e.getPlayer().getInventory().setItem(0, FeatherEvents.FEATHER);

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {

		DropperGame game = (DropperGame) GameController.getCurrentGame();

		game.getDeaths().remove(e.getPlayer().getName());
		game.getStages().remove(e.getPlayer().getName());

	}


}

package games.meta;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import games.game.lobby.LobbyGame;
import games.meta.statistics.DataRecorder;
import games.meta.statistics.GameStatistics;
import games.meta.statistics.XPCalculation;
import net.md_5.bungee.api.ChatColor;

public class GameJoinEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Game g = GameController.getCurrentGame();
		
		if (g == null || g instanceof LobbyGame)
			return;
		
		e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "GAME: " + ChatColor.RESET + "" + ChatColor.GRAY + "You have joined a game which is already in progress.");
		
		
		g.getStats().getJoinTimes().put(e.getPlayer().getName(), System.currentTimeMillis());
		g.getScoreboard().showScoreboard(e.getPlayer());
		
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {

		Game g = GameController.getCurrentGame();
		
		if (g == null || g instanceof LobbyGame)
			return;
		
		g.getStats().getLeaveTimes().put(e.getPlayer().getName(), System.currentTimeMillis());
		
		Player p = e.getPlayer();
		Class<? extends GameStatistics> statsClass = g.getType().getStatsClass();
		if (statsClass != null) {
			try {
				DataRecorder recorder = g.getStats();
				PlayerStatistics ps = PlayerStatisticsManager.getStatistics(p);
				GameStatistics s = g.getType().getStatsClass().cast(ps.getStatistics(g.getType()));
				long joinTime = recorder.getJoinTimes().getOrDefault(p.getName(), 0L);
				long leaveTime = recorder.getLeaveTimes().getOrDefault(p.getName(), System.currentTimeMillis());
				if (joinTime != 0) {
					s.addTimePlayed(leaveTime - joinTime);
				}
				s.incrementGamesPlayed();

				XPCalculation xp = new XPCalculation(recorder);
				ps.setCurrency(ps.getCurrency() + xp.getTimePounds(p));
				ps.setTotalXP(ps.getTotalXP() + xp.getTimeXP(p));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}

package core.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;

public class StatisticEvents implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if (e.getTo().getBlockX() != e.getFrom().getBlockX() ||
				e.getTo().getBlockY() != e.getFrom().getBlockY() ||
				e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
			PlayerStatisticsManager.getStatistics(e.getPlayer()).incrementTotalBlocksWalked();
		}
		
	}
	
	public void onJoin(PlayerJoinEvent e) {
		PlayerStatisticsManager.getStatistics(e.getPlayer()).setLastUsedIP(e.getPlayer().getAddress().getAddress().getHostAddress());
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(e.getPlayer());
		ps.setTotalChatMessages(ps.getTotalChatMessages() + 1);
	
	}
	
}

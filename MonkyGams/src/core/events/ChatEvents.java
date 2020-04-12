package core.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import core.data.DatabaseManager;
import core.data.PlayerStatisticsManager;
import core.data.RankManager;
import games.game.elytraparkour.ShowHidePlayerEvents;
import games.meta.Game;
import games.meta.GameController;
import hub.gadgets.meta.Gadget;
import net.md_5.bungee.api.ChatColor;
import utils.npc.NPCManager;

public class ChatEvents implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		
		e.setJoinMessage(null);
		Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "+ " + ChatColor.RESET + "" + ChatColor.GRAY + e.getPlayer().getName());
		ShowHidePlayerEvents.showPlayers(e.getPlayer());
		
		PlayerStatisticsManager.addStatistics(DatabaseManager.getPlayerData(e.getPlayer().getUniqueId().toString()));
		NPCManager.showAllForPlayer(e.getPlayer());
		
		Game g = GameController.getCurrentGame();
		if (g != null) {
			g.getStats().getJoinTimes().put(e.getPlayer().getName(), System.currentTimeMillis());
		}
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		
		e.setQuitMessage(null);
		Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "- " + ChatColor.RESET + "" + ChatColor.GRAY + e.getPlayer().getName());
		
		Gadget.disableAllGadgets(e.getPlayer());
		Game g = GameController.getCurrentGame();
		if (g != null) {
			g.getStats().getLeaveTimes().put(e.getPlayer().getName(), System.currentTimeMillis());
		}
		
		DatabaseManager.updatePlayerData(PlayerStatisticsManager.getStatistics(e.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		e.setCancelled(true);
		
		String rank = RankManager.getRankManager().getRank(e.getPlayer()).getChatName();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			String msg = e.getMessage();
			String newMsg = "";
			for (String word : msg.split(" ")) {
				if (word.equalsIgnoreCase(p.getName())) {
					word = ChatColor.BOLD + "" + p.getName() + ChatColor.RESET + "" + ChatColor.GRAY;
				}
				newMsg += word + " ";
			}
			p.sendMessage(rank + (rank.isEmpty() ? "" : " ")
					+ ChatColor.RESET + "" + ChatColor.YELLOW 
					+ e.getPlayer().getName() + " " + ChatColor.GRAY + newMsg);
			
		}
	
	}
	
}

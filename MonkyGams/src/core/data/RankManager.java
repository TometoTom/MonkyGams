package core.data;

import org.bukkit.entity.Player;

import command.meta.PermissionLevel;

public class RankManager {

	private static RankManager instance = new RankManager();
	
	private RankManager() { }
	
	public static RankManager getRankManager() {
		return instance;
	}
	
	public PermissionLevel getRank(Player p) {
		return PlayerStatisticsManager.getStatistics(p).getRank();
	}
	
	public void removeRank(Player p) {
		PlayerStatisticsManager.getStatistics(p).setRank(PermissionLevel.NONE);
	}
	
	public void setRank(Player p, PermissionLevel lvl) {
		
		PlayerStatisticsManager.getStatistics(p).setRank(lvl);
		
	}
	
	public boolean hasRank(Player p, PermissionLevel l) {
		
		return getRank(p).getLevel() >= l.getLevel();
		
	}
	
}

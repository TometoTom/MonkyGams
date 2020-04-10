package core.data;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerStatisticsManager {

	private static HashMap<String, PlayerStatistics> statisticsStore = new HashMap<>();
	
	public static Collection<PlayerStatistics> getPlayerStatistics(){
		return statisticsStore.values();	
	}
	
	public static void addStatistics(PlayerStatistics ps) {
		statisticsStore.put(ps.getUuid(), ps);
	}
	
	public static PlayerStatistics getStatistics(String uuid) {
		return statisticsStore.get(uuid);
	}
	public static PlayerStatistics getStatistics(Player p) {
		return statisticsStore.get(p.getUniqueId().toString());
	}
	public static void removeStatistics(String uuid) {
		statisticsStore.remove(uuid);
	}
	public static void removeStatistics(Player p) {
		statisticsStore.remove(p.getUniqueId().toString());
	}
}

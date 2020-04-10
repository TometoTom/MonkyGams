package games.meta.statistics;

import org.bukkit.entity.Player;

public class XPCalculation {

	private DataRecorder stats;
	
	public XPCalculation(DataRecorder recorder) {
		this.stats = recorder;
	}
	
	public int getTimeXP(Player p) {
		long joinTime = stats.getJoinTimes().getOrDefault(p.getName(), 0L);
		long leaveTime = stats.getLeaveTimes().getOrDefault(p.getName(), 0L);
		
		if (leaveTime == 0) {
			leaveTime = System.currentTimeMillis();
		}
		if (joinTime == 0) {
			return 0;
		}
		
		int xp = (int) (leaveTime - joinTime);
		xp /= 1000;
		/* XP cap of 1 hour on games */
		if (xp > 3600) {
			xp = 3600;
		}
		return xp;
	}

	public int getTimePounds(Player p) {
		
		long joinTime = stats.getJoinTimes().getOrDefault(p.getName(), 0L);
		long leaveTime = stats.getLeaveTimes().getOrDefault(p.getName(), 0L);
		
		if (leaveTime == 0) {
			leaveTime = System.currentTimeMillis();
		}
		if (joinTime == 0) {
			return 0;
		}
		
		int pounds = (int) (leaveTime - joinTime);
		pounds /= 1000;
		pounds = pounds / 4;
		return pounds;
	}
	
}

package games.game.elytrabattle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import core.Main;

public class ElytraBattleBoost {

	public static int scheduleId = -1;

	public static void start() {
		scheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable(){
			public void run(){
				for (Player p : Bukkit.getOnlinePlayers()){
					new Thread(() -> {
						if (p.isGliding()){
							if (p.isSneaking()){
								if (p.getExp() < 0.1) return;
								p.setVelocity(p.getLocation().getDirection().multiply(1.03));
								p.setExp(p.getExp() - 0.05F);
							}
						}
					}).start();
				}
			}
		}, 0, 2);
	}
	
	public static void stop() {
		Bukkit.getScheduler().cancelTask(scheduleId);
	}

}

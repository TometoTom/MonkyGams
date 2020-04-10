package games.game.elytraparkour;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import games.game.elytrabattle.ElytraBattleGame;
import games.meta.GameController;

public class Boost {

	public static void setup() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("MonkyGams"), new Runnable(){
			public void run(){
				if (GameController.getCurrentGame() instanceof ElytraBattleGame) return;
				for (Player p : Bukkit.getOnlinePlayers()){
					new Thread(() -> {
						if (p.isGliding()){
							if (p.isSneaking()){

								if (p.getExp() < 0.1) return;
								p.setVelocity(p.getLocation().getDirection().multiply(1.03));
								p.setExp(p.getExp() - 0.05F);

							}
							else {
								if (p.getExp() < 1 - 0.0125F) p.setExp(p.getExp() + 0.0125F);
							}
						}
					}).start();
				}
			}
		}, 0, 2);
	}
	
}

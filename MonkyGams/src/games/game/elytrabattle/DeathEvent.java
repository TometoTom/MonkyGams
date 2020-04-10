package games.game.elytrabattle;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import core.data.PlayerStatisticsManager;
import games.meta.GameController;
import games.meta.statistics.ElytraBattleStatistics;
import net.md_5.bungee.api.ChatColor;
import utils.Utils;
import utils.game.GameUtils;

public class DeathEvent implements Listener {

	@EventHandler
	public void onDeath(EntityDamageByEntityEvent e) {
		
		if (e.getEntityType() != EntityType.PLAYER) return;
		if (e.getCause() == DamageCause.FALL) return;
		
		Player p = (Player) e.getEntity();
		
		if (p.getHealth() - e.getDamage() < 0) {
			e.setCancelled(true);
			Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "DEATH: "
					+ ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + " was killed by " + Utils.bold(e.getDamager().getName()) + ".");
			
			ElytraBattleGame game = GameController.getCurrentGame(ElytraBattleGame.class);
			game.die(p);
			
			game.getStats().getKills().increment(e.getDamager().getName());
			
			if (e.getDamager().getType() == EntityType.PLAYER) {
				PlayerStatisticsManager.getStatistics(Bukkit.getPlayer(e.getDamager().getName())).getStatistics(ElytraBattleStatistics.class).kills++;
			}
			
		}
	}
	
	@EventHandler
	public void onDeath(EntityDamageEvent e) {
		
		if (e.getEntityType() != EntityType.PLAYER) return;
		if (e.getCause() == DamageCause.FALL) return;
		
		if (e.getCause() == DamageCause.ENTITY_ATTACK) return;
		if (e.getCause() == DamageCause.ENTITY_EXPLOSION) return;
		if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) return;
		
		Player p = (Player) e.getEntity();
		
		if (p.getHealth() - e.getDamage() < 0) {
			e.setDamage(0);
			e.setCancelled(true);
			
			String cause = GameUtils.getStringFromDamageCause(e.getCause());
			
			Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "DEATH: "
					+ ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + " was killed by " + ChatColor.BOLD + cause + ChatColor.RESET + "" + ChatColor.GRAY + ".");
			GameController.getCurrentGame(ElytraBattleGame.class).die(p);
			
		}
	}
	
}

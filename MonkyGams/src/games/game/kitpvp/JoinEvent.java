package games.game.kitpvp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener{

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		
		Player p = e.getPlayer();
//		p.setMaximumNoDamageTicks(2);
//		p.setNoDamageTicks(2);
		DeathEvent.killstreak.put(p, 0);
		RespawnEvent.giveKit(p);
	}
	
}

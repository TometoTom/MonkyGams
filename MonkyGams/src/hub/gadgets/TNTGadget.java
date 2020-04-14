package hub.gadgets;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import hub.gadgets.meta.Gadget;
import utils.game.GameUtils;

public class TNTGadget extends Gadget {

	@Override
	public void onEnable(Player p) {

		TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.PRIMED_TNT);
		tnt.setVelocity(p.getLocation().add(0, 1, 0).getDirection().multiply(1.1));
		tnt.setFuseTicks(40);

		registerEvent(new Listener() {
			@EventHandler
			public void onExplode(EntityExplodeEvent e) {
				e.getLocation().getWorld().createExplosion(e.getLocation(), 10, false, false);
				Bukkit.getOnlinePlayers().forEach((p) -> {
					if (e.getLocation().getWorld().getName().equalsIgnoreCase(p.getWorld().getName())
							&& e.getLocation().distance(p.getLocation()) <= 5) {
						p.setVelocity(new Vector(
								p.getLocation().getX() - e.getLocation().getX(),
								2,
								p.getLocation().getZ() - e.getLocation().getZ()));
					}
				});
			}
		});
		
		GameUtils.delayTask(() -> disable(p), 50);
	}

	@Override
	public void onDisable(Player p) {
		deregisterAllEvents();
	}

}

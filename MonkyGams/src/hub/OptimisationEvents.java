package hub;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import command.meta.PermissionLevel;
import core.data.RankManager;
import utils.game.GameUtils;

public class OptimisationEvents implements Listener {

	@EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
	
	@EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
		if (e.toWeatherState()) e.setCancelled(true);
    }
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {

		if (RankManager.getRankManager().hasRank(e.getPlayer(), PermissionLevel.OWNER) && e.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		e.setCancelled(true);

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		if (RankManager.getRankManager().hasRank(e.getPlayer(), PermissionLevel.OWNER) && e.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		e.setCancelled(true);

	}

	@EventHandler
	public void onPlayerDeathEvent(EntityDamageEvent e) {

		e.setDamage(0);
		e.setCancelled(true);

	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if (GameUtils.areLocationsEqual(e.getTo(), e.getFrom())) {
			return;
		}
		
		Player p = e.getPlayer();
		Location to = e.getTo();
		
		if (to.getY() < 0) {
			p.setHealth(20);
			HubManager.teleportSpawn(p);
			GameUtils.doDeathEffect(p);
			return;
		}
	}
}

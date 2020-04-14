package utils.game;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import games.game.lobby.LobbyGame;

public class GameListeners {

	private static Listener preventDestroyingBlocks = new Listener() {
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
			}
		}
	};
	
	private static Listener preventPlacingBlocks = new Listener() {
		@EventHandler
		public void onBlockPlace(BlockPlaceEvent e) {
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
			}
		}
	};
	
	private static Listener preventFire = new Listener() {
		@EventHandler
		public void onFire(EntityDamageEvent e) {
			if (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK) {
				e.getEntity().setFireTicks(1);
			}
		}
	};
	
	private static Listener preventDamage = new Listener() {
		@EventHandler
		public void onDamage(EntityDamageEvent e){
			e.setCancelled(true);
		}
	};
	
	private static Listener preventRain = new Listener() {
		@EventHandler
	    public void onWeatherChange(WeatherChangeEvent e) {
			if (e.toWeatherState()) e.setCancelled(true);
	    }
	};
	
	private static Listener preventFoodChange = new Listener() {
		@EventHandler
	    public void onFoodLevelChange(FoodLevelChangeEvent e) {
	        e.setCancelled(true);
	    }
	};
	
	private static Listener preventItemDrop = new Listener() {
		@EventHandler
		public void onDropItem(PlayerDropItemEvent e) {
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
			}
		}
	};
	
	private static Listener preventInventoryMove = new Listener() {
		@EventHandler
		public void onInventoryClick(InventoryClickEvent e) {
			if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
			}
		}
	};
	
	private static Listener teleportOnDeath = new Listener() {
		@EventHandler
		public void onPlayerDeathEvent(EntityDamageEvent e) {
			if (e.getEntity().getType() != EntityType.PLAYER) {
				return;
			}

			e.setCancelled(true);
			
			Player p = (Player) e.getEntity();
			if (p.getHealth() - e.getDamage() < 1
					&& e.getCause() != DamageCause.FALL) {
				p.setHealth(20);
				LobbyGame.teleport(p);
				GameUtils.doDeathEffect(p);
			}
		}
	};

	public static Listener getPreventDestroyingBlocks() {
		return preventDestroyingBlocks;
	}

	public static Listener getPreventPlacingBlocks() {
		return preventPlacingBlocks;
	}

	public static Listener getPreventFire() {
		return preventFire;
	}

	public static Listener getPreventDamage() {
		return preventDamage;
	}

	public static Listener getPreventRain() {
		return preventRain;
	}

	public static Listener getPreventFoodChange() {
		return preventFoodChange;
	}

	public static Listener getPreventItemDrop() {
		return preventItemDrop;
	}

	public static Listener getPreventInventoryMove() {
		return preventInventoryMove;
	}

	public static Listener getTeleportOnDeath() {
		return teleportOnDeath;
	}
	
}

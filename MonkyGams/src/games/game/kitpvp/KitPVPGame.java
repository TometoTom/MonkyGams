package games.game.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import command.meta.CommandManager;
import games.meta.Game;
import games.meta.GameType;
import games.meta.Map;
import utils.game.GameListeners;

public class KitPVPGame extends Game {

	DuelCommand duelCommand = new DuelCommand();
	KitCommand kitCommand = new KitCommand();
	
	public KitPVPGame(Map map) {
		super(GameType.KITPVP, map);
	}

	@Override
	protected void registerListeners() {
		CommandManager.registerCommand(duelCommand);
		CommandManager.registerCommand(kitCommand);
		
		registerEvent(new ArrowHitEvent());
		registerEvent(new ClickEntityEvent());
		registerEvent(new DeathEvent());
		registerEvent(new InteractEvent());
		registerEvent(new InteractInventoryEvent());
		registerEvent(new InventoryShutEvent());
		registerEvent(new JoinEvent());
		registerEvent(new MovementEvent());
		registerEvent(new PlayerDamageEvent());
		registerEvent(new RespawnEvent());
		registerEvent(GameListeners.getPreventDestroyingBlocks());
		registerEvent(GameListeners.getPreventPlacingBlocks());
		registerEvent(GameListeners.getPreventRain());
		registerEvent(GameListeners.getPreventItemDrop());
		
	}

	@Override
	protected void start() {
		
		Listener l = GameListeners.getPreventDamage();
		registerEvent(l);
		
		registerListeners();
		
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.getInventory().clear();
			p.teleport(new Location(Bukkit.getWorld("world"), -151.49849408118314, 93.5, -36.48180761416791, 91.0011f, 8.532448f));
		});
		
		broadcastIntroduction("This is the old Max Poo PvP 2.0 from many years ago.",
				"There's going to be loads of bugs.",
				"Apologies for the racism and the icky rubbish.",
				"Have fun!");
		
		doCountdown(() -> {
			deregisterEvent(l);
			Bukkit.getOnlinePlayers().forEach(RespawnEvent::giveKit);
		}, 6, true);
		
	}

	@Override
	protected void end() {
		deregisterAllEvents();
		CommandManager.deregisterCommand(duelCommand);
	}	
	
}

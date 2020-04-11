package games.game.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Listener;

import command.meta.CommandManager;
import games.meta.Game;
import games.meta.GameType;
import games.meta.Map;
import utils.game.GameListeners;
import utils.npc.NPC;

public class KitPVPGame extends Game {

	DuelCommand duelCommand = new DuelCommand();
	KitCommand kitCommand = new KitCommand();
	NPC kitNPC;
	
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
			p.setMaximumNoDamageTicks(2);
			p.setNoDamageTicks(2);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2);
		});
		
		broadcastIntroduction("This is the old Max Poo PvP 2.0 from many years ago.",
				"There's going to be loads of bugs.",
				"Apologies for the racism and the icky rubbish.",
				"Have fun!");
		
		doCountdown(() -> {
			deregisterEvent(l);
			Bukkit.getOnlinePlayers().forEach(RespawnEvent::giveKit);
		}, 6, true);
		
		kitNPC = new NPC(new Location(Bukkit.getWorld("world"), -148.44813484458615, 93.0, -33.46543847613573, 134.8274f, 0.029884668f), "90e7a40cff694c28817a47f6751772ab", GOLD + BOLD + "Choose Kit");
		kitNPC.register();
		kitNPC.showForAll();
	}

	@Override
	protected void end() {
		deregisterAllEvents();
		CommandManager.deregisterCommand(duelCommand);
		CommandManager.deregisterCommand(kitCommand);
		
		kitNPC.despawn();
		kitNPC.deregister();
		
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.setMaximumNoDamageTicks(16);
			p.setNoDamageTicks(16);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
		});
	}	
	
}

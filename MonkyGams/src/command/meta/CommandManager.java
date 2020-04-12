package command.meta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import command.commands.game.CountdownCommand;
import command.commands.game.GameCommand;
import command.commands.game.KitCommand;
import command.commands.game.MapCommand;
import command.commands.hub.GadgetCommand;
import command.commands.other.AnvilCommand;
import command.commands.other.DebugCommand;
import command.commands.other.LobbyCommand;
import command.commands.other.RankCommand;
import command.commands.other.StatsCommand;
import command.commands.other.TestCommand;
import command.commands.other.VoidWorldCommand;
import command.commands.utils.FeedCommand;
import command.commands.utils.GamemodeCommand;
import command.commands.utils.HealCommand;
import command.commands.utils.HealthCommand;
import command.commands.utils.KillCommand;
import command.commands.utils.LocationCommand;
import command.commands.utils.TeleportCommand;
import core.Main;

public class CommandManager {

	public static void registerCommands() {
		
		new AnvilCommand();
		new GameCommand();	
		new VoidWorldCommand();
		new FeedCommand();
		new HealCommand();
		new HealthCommand();
		new KillCommand();
		new TeleportCommand();
		new GamemodeCommand();
		new MapCommand();
		new RankCommand();
		new LocationCommand();
		new CountdownCommand();
		new DebugCommand();
		new TestCommand();
		new LobbyCommand();
		new StatsCommand();
		new KitCommand();
		new GadgetCommand();

		CommandMap map = getCommandMap();
		
		for (MonkyCommand c : MonkyCommand.getCommands()) {
			PluginCommand pc = getPluginCommand(c.getName(), Main.getPlugin());
			pc.setAliases(Arrays.asList(c.getAliases()));
			map.register(Main.getPluginName(), pc);
			
			pc.setExecutor(new CommandListener());
		}

	}
	
	public static void registerCommand(MonkyCommand c) {
		CommandMap map = getCommandMap();
		PluginCommand pc = getPluginCommand(c.getName(), Main.getPlugin());
		pc.setAliases(Arrays.asList(c.getAliases()));
		map.register(Main.getPluginName(), pc);
		pc.setExecutor(new CommandListener());
	}
	
	public static void deregisterCommand(MonkyCommand c) {
		
		try {
			CommandMap map = getCommandMap();
			PluginCommand pc = getPluginCommand(c.getName(), Main.getPlugin());
			pc.unregister(map);
//			Field f = map.getClass().getDeclaredField("knownCommands");
//			f.setAccessible(true);
//			@SuppressWarnings("unchecked")
//			HashMap<String, Command> knownCommands = (HashMap<String, Command>) f.get(map);
//			knownCommands.remove(c.getName());
		} catch (Exception e) {
			
		}
	}

	private static CommandMap getCommandMap() {

		try {
			Field f = SimplePluginManager.class.getDeclaredField("commandMap");
			f.setAccessible(true);
			return (CommandMap) f.get(Bukkit.getPluginManager());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 

	}

	private static PluginCommand getPluginCommand(String name, Plugin plugin) {
	 
		try {
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
			return c.newInstance(name, plugin);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	
	}
	
}

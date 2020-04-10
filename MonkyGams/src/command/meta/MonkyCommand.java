package command.meta;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import core.Main;
import net.md_5.bungee.api.ChatColor;

public abstract class MonkyCommand {
	
	private static ArrayList<MonkyCommand> commands = new ArrayList<MonkyCommand>();
	
	public static ArrayList<MonkyCommand> getCommands() {
		return commands;
	}
	
	public static void registerCommand(MonkyCommand c) {
		commands.add(c);
	}
	
	public static void deregisterCommand(MonkyCommand c) {
		commands.remove(c);
	}
	
	public static void deregisterAll() {
		commands.clear();
	}
	
	public static MonkyCommand getCommand(String message) {
		
		for (MonkyCommand c : commands) {
			if (c.getName().equalsIgnoreCase(message)) {
				return c;
			}
			for (String alias : c.getAliases()) {
				if (alias.equalsIgnoreCase(message)) {
					return c;
				}
			}
		}
		
		return null;
		
	}
	
	public MonkyCommand() {
		if (getCommandCompatibility() == CommandCompatibility.UNIVERSAL) {
			MonkyCommand.registerCommand(this);
		}
		else if (getCommandCompatibility() == CommandCompatibility.LOBBY && Main.isHubMode()) {
			MonkyCommand.registerCommand(this);
		}
		else if (getCommandCompatibility() == CommandCompatibility.GAME &! Main.isHubMode()) {
			MonkyCommand.registerCommand(this);
		}
	}
	
	public abstract void onCommand(Player p, Args args);
	public abstract String getName();
	public abstract String getDescription();
	public abstract String getUsage();
	public abstract String[] getAliases();
	public abstract PermissionLevel getPermissionLevel();
	public abstract CommandCompatibility getCommandCompatibility();
	
	/* Shortcuts */
	public void b(String msg) {
		Bukkit.broadcastMessage(msg);
	}
	public void s(Player p, String msg) {
		p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + getName().toUpperCase() + ": " + ChatColor.RESET + "" + ChatColor.GRAY + msg);
	}
	public void f(Player p, String msg) {
		p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + getName().toUpperCase() + ": " + ChatColor.RESET + "" + ChatColor.GRAY + msg);
	}
	
	public String getStarterSuccess() {
		return ChatColor.GREEN + "" + ChatColor.BOLD + getName().toUpperCase() + ": " + ChatColor.RESET + "" + ChatColor.GRAY;
	}
	public String getStarterFailure() {
		return ChatColor.RED + "" + ChatColor.BOLD + getName().toUpperCase() + ": " + ChatColor.RESET + "" + ChatColor.GRAY;
	}
	
	public Player getPlayer(String name) {
		
		Player search = Bukkit.getPlayer(name);
		if (search != null) return search;
		
		try { 
			search = Bukkit.getPlayer(UUID.fromString(name));
			if (search != null) return search;
		} catch (Exception e) {}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().toLowerCase().startsWith(name.toLowerCase())) {
				return search;
			}
		}
		
		return null;
		
	}
	
	public Player getPlayerExact(String name) {
		
		Player search = Bukkit.getPlayerExact(name);
		if (search != null) return search;
		
		try { 
			search = Bukkit.getPlayer(UUID.fromString(name));
			if (search != null) return search;
		} catch (Exception e) {}
		
		return null;
		
	}
	
	public ChatColor AQUA = ChatColor.AQUA;
	public ChatColor BLACK = ChatColor.BLACK;
	public ChatColor BLUE = ChatColor.BLUE;
	public ChatColor BOLD = ChatColor.BOLD;
	public ChatColor DARK_AQUA = ChatColor.DARK_AQUA;
	public ChatColor DARK_BLUE = ChatColor.DARK_BLUE;
	public ChatColor DARK_GREY = ChatColor.DARK_GRAY;
	public ChatColor DARK_GREEN = ChatColor.DARK_GREEN;
	public ChatColor DARK_PUPLE = ChatColor.DARK_PURPLE;
	public ChatColor DARK_RED = ChatColor.DARK_RED;
	public ChatColor GOLD = ChatColor.GOLD;
	public ChatColor GREY = ChatColor.GRAY;
	public ChatColor GREEN = ChatColor.GREEN;
	public ChatColor ITALIC = ChatColor.ITALIC;
	public ChatColor LIGHT_PURPLE = ChatColor.LIGHT_PURPLE;
	public ChatColor MAGIC = ChatColor.MAGIC;
	public ChatColor RED = ChatColor.RED;
	public ChatColor RESET = ChatColor.RESET;
	public ChatColor STRIKETHROUGH = ChatColor.STRIKETHROUGH;
	public ChatColor UNDERLINE = ChatColor.UNDERLINE;
	public ChatColor WHITE = ChatColor.WHITE;
	public ChatColor YELLOW = ChatColor.YELLOW;
}

package hub.gadgets.meta;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import core.Main;
import net.md_5.bungee.api.ChatColor;
import utils.game.GameUtils;

public abstract class Gadget {

	private static HashMap<String, Gadget> gadgetsInUse = new HashMap<>();
	
	public static void disableAllGadgets() {
		gadgetsInUse.entrySet().forEach(entry -> {
			entry.getValue().disable(Bukkit.getPlayer(entry.getKey()));
		});
	}
	public static void disableAllGadgets(Player p) {
		gadgetsInUse.entrySet().forEach(entry -> {
			if (entry.getKey().equalsIgnoreCase(p.getName()))
				entry.getValue().disable(p);
		});
	}
	
	private ArrayList<Listener> listeners = new ArrayList<>();
	
	/**
	 * This describes the behaviour when a gadget is initially used.
	 */
	public abstract void onEnable(Player p);
	/**
	 * This describes the behaviour when a gadget is disabled, either by force (lobby ending for example) or by choice, or by expiry.
	 * You do not have to handle removal of the icon from the hotbar.
	 */
	public abstract void onDisable(Player p);
	
	public void enable(Player p) {
		if (gadgetsInUse.containsKey(p.getName())) {
			p.sendMessage(GameUtils.getFailureMessage("GADGET", "You cannot use two gadgets at once."));
			return;
		}
		gadgetsInUse.put(p.getName(), this);
		onEnable(p);
	}
	
	public void disable(Player p) {
		gadgetsInUse.remove(p.getName());
		onDisable(p);
	}
	
	public void registerEvent(Listener l) {
		Bukkit.getPluginManager().registerEvents(l, Main.getPlugin());
		listeners.add(l);
	}
	public void registerEvents(Listener... listeners) {
		for (Listener l : listeners) {
			registerEvent(l);
		}
	}
	public void deregisterAllEvents() {
		ArrayList<Listener> toRemove = new ArrayList<>();
		listeners.forEach((l) -> {
			HandlerList.unregisterAll(l);
			toRemove.add(l);
		});
		toRemove.forEach(listeners::remove);
	}
	public void deregisterEvent(Listener l) {
		HandlerList.unregisterAll(l);
	}
	
	public String AQUA = ChatColor.AQUA.toString();
	public String BLACK = ChatColor.BLACK.toString();
	public String BLUE = ChatColor.BLUE.toString();
	public String BOLD = ChatColor.BOLD.toString();
	public String DARK_AQUA = ChatColor.DARK_AQUA.toString();
	public String DARK_BLUE = ChatColor.DARK_BLUE.toString();
	public String DARK_GREY = ChatColor.DARK_GRAY.toString();
	public String DARK_GREEN = ChatColor.DARK_GREEN.toString();
	public String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
	public String DARK_RED = ChatColor.DARK_RED.toString();
	public String GOLD = ChatColor.GOLD.toString();
	public String GREY = ChatColor.GRAY.toString();
	public String GREEN = ChatColor.GREEN.toString();
	public String ITALIC = ChatColor.ITALIC.toString();
	public String LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();
	public String MAGIC = ChatColor.MAGIC.toString();
	public String RED = ChatColor.RED.toString();
	public String RESET = ChatColor.RESET.toString();
	public String STRIKETHROUGH = ChatColor.STRIKETHROUGH.toString();
	public String UNDERLINE = ChatColor.UNDERLINE.toString();
	public String WHITE = ChatColor.WHITE.toString();
	public String YELLOW = ChatColor.YELLOW.toString();
	
}

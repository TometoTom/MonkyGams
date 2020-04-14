package utils.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import utils.MonkyItemStack;

public class MonkyGUI {
	
	protected static HashMap<String, MonkyGUI> guis = new HashMap<>();

	private String name;
	private Inventory inventory;
	private HashMap<ClickType, ClickEvent> clickEvents = new HashMap<>();
	
	public MonkyGUI(Player p, String name, int rows) {
		this.name = name;
		this.inventory = Bukkit.createInventory(null, rows * 9, name);
	}
	
	public MonkyGUI(Player p, String name, int rows, MonkyItemStack[] contents) {
		this.name = name;
		this.inventory = Bukkit.createInventory(null, rows * 9, name);
		
		for (MonkyItemStack i : contents) {
			inventory.addItem(i);
		}
	}
	
	public void present(Player p) {
		p.openInventory(inventory);
		guis.put(p.getName(), this);
	}
	
	public void setClickEvent(ClickType type, ClickEvent e) {
		clickEvents.put(type, e);
	}
	
	public HashMap<ClickType, ClickEvent> getClickEvents(){
		return clickEvents;
	}
	
	public ClickEvent getClickEvent(ClickType type) {
		return clickEvents.get(type);
	}
	
	public String getName() {
		return name;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}

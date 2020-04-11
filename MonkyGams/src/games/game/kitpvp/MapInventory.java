package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MapInventory {

	public MapInventory(Player p){
		
		p.openInventory(getInventory("CHOOSE A MAP"));
	}
	
	public static ItemStack getMap(DuelMap map){
		
		ItemStack i = new ItemStack(map.getMaterial());
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + map.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD + "Creator: " + ChatColor.RESET + "" + ChatColor.GRAY + map.getBuilder());
		lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD + "ID: " + ChatColor.RESET + "" + ChatColor.GRAY + map.getID());
		meta.setLore(lore);
		meta.setUnbreakable(true);
		i.setItemMeta(meta);
		
		return i;
	}
	
	public static Inventory getInventory(String name){
		
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + name);
		
		int i = 0;
		for (DuelMap map : DuelMap.values()){
			
			inv.setItem(i, getMap(map));
			i++;
		}
		
		return inv;
		
	}
	
}

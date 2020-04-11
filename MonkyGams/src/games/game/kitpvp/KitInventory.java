package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class KitInventory {

	public KitInventory(Player p){
		
		p.openInventory(getInventory("KITS"));
	}
	
	public KitInventory(Player p, String s){
		
		p.openInventory(getInventory(s));
	}
	
	public static ItemStack getItem(Kit kit){
		
		ItemStack i;
		
		if (kit==Kit.CAMERON){
			i = new ItemStack(kit.getMaterial(), 10);
		}
		else{
			i = new ItemStack(kit.getMaterial());
		}
		
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + kit.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD + "Skill: " + ChatColor.RESET + "" + ChatColor.GRAY + kit.getSkillName());
		lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD + "Cooldown: " + ChatColor.RESET + "" + ChatColor.GRAY + kit.getCooldown() + " seconds");
		lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD + "Description: " + ChatColor.RESET + "" + ChatColor.GRAY + kit.getDescription());
		if (kit.isEnabled()) lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "ENABLED");
		if (!(kit.isEnabled())) lore.add(ChatColor.RED + "" + ChatColor.BOLD + "DISABLED");
		meta.setLore(lore);
		meta.setUnbreakable(true);
		i.setItemMeta(meta);
		
		return i;
	}
	
	public static Inventory getInventory(String name){
		
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.GOLD + "" + ChatColor.BOLD + name);
		
		int i = 0;
		for (Kit kit : Kit.values()){
			
			inv.setItem(i, getItem(kit));
			i++;
		}
		
		
		return inv;
		
	}
	
}

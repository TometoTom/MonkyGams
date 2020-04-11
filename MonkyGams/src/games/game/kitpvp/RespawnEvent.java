package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;


public class RespawnEvent implements Listener{

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		
		giveKit(e.getPlayer());
		e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -151.49849408118314, 93.5, -36.48180761416791, 91.0011f, 8.532448f));
		
	}
	
	public static void giveKit(Player p){
		
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack trousers = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		
		applyEnchantment(helmet);
		applyEnchantment(chestplate);
		applyEnchantment(trousers);
		applyEnchantment(boots);
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 3, (short) 1);
		ItemStack food = new ItemStack(Material.BREAD, 64);
		
		ItemMeta swordm = sword.getItemMeta();
		ItemMeta bowm = bow.getItemMeta();
		ItemMeta arrowm = arrow.getItemMeta();
		ItemMeta rodm = rod.getItemMeta();
		ItemMeta applem = apple.getItemMeta();
		ItemMeta foodm = food.getItemMeta();
		
		swordm.setUnbreakable(true);
		bowm.setUnbreakable(true);
		arrowm.setUnbreakable(true);
		rodm.setUnbreakable(true);
		applem.setUnbreakable(true);
		foodm.setUnbreakable(true);
		
		swordm.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
		bowm.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		bowm.addEnchant(Enchantment.ARROW_DAMAGE, 30, true);
		bowm.addEnchant(Enchantment.ARROW_FIRE, 3, true);
		
		swordm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Sword");
		bowm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bow");
		arrowm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Arrow");
		rodm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Rod");
		applem.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Golden Apple");
		foodm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Non-meat food");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GOLD + "+1 when you kill someone.");
		applem.setLore(lore);
		lore.clear();
		lore.add(ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GOLD + "Bread does not contain meat.");
		foodm.setLore(lore);
		
		sword.setItemMeta(swordm);
		bow.setItemMeta(bowm);
		arrow.setItemMeta(arrowm);
		rod.setItemMeta(rodm);
		food.setItemMeta(foodm);
		apple.setItemMeta(applem);
		
		PlayerInventory i = p.getInventory();
		i.setHelmet(helmet);
		i.setChestplate(chestplate);
		i.setLeggings(trousers);
		i.setBoots(boots);
		
		i.setItem(0, sword);
		i.setItem(1, bow);
		i.setItem(2, rod);
		i.setItem(3, apple);
		i.setItem(4, food);
		i.setItem(9, arrow);
		
	}
	
	public static ItemStack applyEnchantment(ItemStack i){
		
		ItemMeta im = i.getItemMeta();
		im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Armour");
		im.setUnbreakable(true);
		i.setItemMeta(im);
		return i;
	}
	
}

package utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MonkyItemStack extends ItemStack {
	
	public MonkyItemStack(Material m) {
		super(m);
	}
	
	public MonkyItemStack setName(String name) {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(name);
		setItemMeta(meta);
		return this;
	}
	public MonkyItemStack setLore(String... lore) {
		ItemMeta meta = getItemMeta();
		meta.setLore(Arrays.asList(lore));
		setItemMeta(meta);
		return this;
	}
	public MonkyItemStack setNumber(int amount) {
		setAmount(amount);
		return this;
	}
	public String getName() {
		return getItemMeta().getDisplayName();
	}
	public List<String> getLore(){
		return getItemMeta().getLore();
	}
	
	public MonkyItemStack addMonkyEnchant(Enchantment e, int level) {
		ItemMeta meta = getItemMeta();
		meta.addEnchant(e, level, true);
		setItemMeta(meta);
		return this;
	}
	
}

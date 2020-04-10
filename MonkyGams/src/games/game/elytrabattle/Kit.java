package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;

public enum Kit {

	MINER("Miner", Material.DIAMOND_PICKAXE, 500, "Spawn in with a Rope and an Efficiency 6 pickaxe to aid with your mining trip!", (p, g) -> {
		p.getInventory().remove(Material.DIAMOND_PICKAXE);
		p.getInventory().addItem(new MonkyItemStack(Material.DIAMOND_PICKAXE)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Miner's Pickaxe")
				.setLore(ChatColor.GRAY + "An upgraded pickaxe.")
				.addMonkyEnchant(Enchantment.DIG_SPEED, 6)
				.addMonkyEnchant(Enchantment.DURABILITY, 3));
		p.getInventory().addItem(new MonkyItemStack(Material.LEAD)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Rope")
				.setLore(ChatColor.GRAY + "Teleports you to surface level on click."));
	}),
	XRAYER("Xrayer", Material.ENDER_EYE, 600, "Spawn in with a compass which highlights the nearest diamonds. However, it is lost on death.", (p, g) -> {
		p.getInventory().addItem(new MonkyItemStack(Material.COMPASS)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Xrayer's Compass")
				.setLore(ChatColor.GRAY + "Highlights diamonds in a 10x5x10 block radius!", "", ChatColor.GRAY + "3 uses left."));
	}),
	CHEST_HUNTER("Chest Hunter", Material.CHEST, 800, "Spawn in with a compass which highlights the nearest chests. However, it is lost on death.", (p, g) -> {
		p.getInventory().addItem(new MonkyItemStack(Material.COMPASS)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Chest Hunter's Compass")
				.setLore(ChatColor.GRAY + "Highlights nearby chests!", "", ChatColor.GRAY + "3 uses left."));
	});
	
	public String getName() {
		return name;
	}
	
	public Material getIcon() {
		return icon;
	}

	public int getPoundsCost() {
		return poundsCost;
	}

	public String getDescription() {
		return description;
	}

	public KitImplementation getGameStart() {
		return gameStart;
	}

	private String name;
	private Material icon;
	private int poundsCost;
	private String description;
	private KitImplementation gameStart;
	
	private Kit(String name, Material icon, int poundsCost, String description, KitImplementation gameStart) {
		this.name = name;
		this.icon = icon;
		this.poundsCost = poundsCost;
		this.description = description;
		this.gameStart = gameStart;
	}
	
}

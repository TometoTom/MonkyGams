package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import games.meta.GameType;
import games.meta.Kit;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;

public class ElytraBattleKits {

	public static void setup() {

		Kit.getKits().add(new Kit("Miner", GameType.ELYTRABATTLE, Material.DIAMOND_PICKAXE, 3, 500, "Spawn in with a Rope and an Efficiency 6 pickaxe to aid with your mining trip!", (p, g) -> {
			p.getInventory().remove(Material.DIAMOND_PICKAXE);
			p.getInventory().addItem(new MonkyItemStack(Material.DIAMOND_PICKAXE)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Miner's Pickaxe")
					.setLore(ChatColor.GRAY + "An upgraded pickaxe.")
					.addMonkyEnchant(Enchantment.DIG_SPEED, 6)
					.addMonkyEnchant(Enchantment.DURABILITY, 3));
			p.getInventory().addItem(new MonkyItemStack(Material.LEAD)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Rope")
					.setLore(ChatColor.GRAY + "Teleports you to surface level on click."));
		}));

		Kit.getKits().add(new Kit("Xrayer", GameType.ELYTRABATTLE, Material.ENDER_EYE, 5, 600, "Spawn in with a compass which highlights the nearest diamonds. However, it is lost on death.", (p, g) -> {
			p.getInventory().addItem(new MonkyItemStack(Material.COMPASS)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Xrayer's Compass")
					.setLore(ChatColor.GRAY + "Highlights diamonds in a 10x5x10 block radius!", "", ChatColor.GRAY + "3 uses left."));
		}));
		
		Kit.getKits().add(new Kit("Chest Hunter", GameType.ELYTRABATTLE, Material.CHEST, 10, 800, "Spawn in with a compass which highlights the nearest chests. However, it is lost on death.", (p, g) -> {
			p.getInventory().addItem(new MonkyItemStack(Material.COMPASS)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Chest Hunter's Compass")
					.setLore(ChatColor.GRAY + "Highlights nearby chests!", "", ChatColor.GRAY + "3 uses left."));
		}));

	}

}

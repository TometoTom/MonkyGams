package hub.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import core.data.PlayerStatisticsManager;
import games.meta.GameType;
import games.meta.Kit;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.Utils;
import utils.game.GameUtils;
import utils.gui.MonkyGUI;
import utils.gui.PurchaseGUI;

public class KitsGUI {

	MonkyGUI gui;
	Player p;
	
	public void present() {
		gui.present(p);
	}
	
	public KitsGUI(Player p) {
		gui = new MonkyGUI(p, "      Game                 Kits", 6);
		this.p = p;
		
		MonkyItemStack separator = new MonkyItemStack(Material.WHITE_STAINED_GLASS_PANE).setName(" ");
		gui.getInventory().setItem(4, separator);
		gui.getInventory().setItem(13, separator);
		gui.getInventory().setItem(22, separator);
		gui.getInventory().setItem(31, separator);
		gui.getInventory().setItem(40, separator);
		gui.getInventory().setItem(49, separator);

		int column = 0;
		int row = 0;
		for (GameType gt : GameType.values()) {

			if (column >= 4) {
				row++;
				column = 0;
			}

			int numKits = Kit.getKits(gt).size();
			gui.getInventory().setItem(row * 9 + column, new MonkyItemStack(gt.getIcon())
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + gt.getName())
					.setLore(ChatColor.GRAY + "" + numKits + " kit" + Utils.plural(numKits)));

			column++;
		}
		
		gui.setClickEvent(ClickType.LEFT, (clicker, item) -> {
			
			/* To identify the difference: The second line of lore in Kit items begins with 'Cost' */
			
			/* This is a kit item */
			if (item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() >= 2 && ChatColor.stripColor(item.getItemMeta().getLore().get(1)).startsWith("Cost")) {
				if (!ChatColor.stripColor(item.getItemMeta().getLore().get(4)).equalsIgnoreCase("UNLOCKED")) {
					for (ItemStack i : gui.getInventory().getContents()) {
						if (Utils.isGlowing(i)) {
							for (GameType gt : GameType.values()) {
								if (i.getType() == gt.getIcon()) {
									for (Kit k : Kit.getKits(gt)) {
										if (k.getIcon() == item.getType()) {
											int level = PlayerStatisticsManager.getStatistics(clicker).getLevel();
											if (k.getLevelMinimum() > level) {
												clicker.sendMessage(GameUtils.getFailureMessage("KIT SHOP", "You need to be level " + k.getLevelMinimum() + " to purchase this kit."
														+ " You are currently level " + level + "."));
												clicker.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
												return;
											}
											new PurchaseGUI(clicker, item.getType(), ChatColor.GOLD + "" + ChatColor.BOLD + k.getName() + " (" + k.getGame().getName() + " kit)", ChatColor.GRAY + k.getDescription(), 1, k.getPoundsCost(), (amount) -> {
												PlayerStatisticsManager.getStatistics(clicker).getUnlockedKits().add(k.getKitId());
											}).present(p);
										}
									}
								}
							}
						}
					}
				}
			}
			/* This is a game item */
			else {
				/* If it is already selected */
				if (Utils.isGlowing(item)) {
					return;
				}
				for (ItemStack i : gui.getInventory().getContents()) {
					
					if (i == null)
						continue;
					
					i.removeEnchantment(Enchantment.DURABILITY);
				}
				
				for (GameType gt : GameType.values()) {
					if (gt.getIcon() == item.getType()) {
						loadGame(gui, gt, clicker);
						Utils.glow(item);
					}
				}
				
			}

		});
	
	}
	
	public void loadGame(MonkyGUI gui, GameType gt, Player p) {

		for (int i = 0; i != 54; i++) {
			if (i % 9 > 4) {
				gui.getInventory().setItem(i, null);
			}
		}

		int column = 5;
		int row = 0;
		for (Kit k : Kit.getKits(gt)) {
			if (column > 8) {
				row++;
				column = 5;
			}
			gui.getInventory().setItem(row * 9 + column, new MonkyItemStack(k.getIcon())
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + k.getName())
					.setLore(ChatColor.GRAY + k.getDescription(),
							ChatColor.GRAY + Utils.bold("Cost: ") + k.getPoundsCost() + " pound" + Utils.plural(k.getPoundsCost()),
							ChatColor.GRAY + Utils.bold("Required Level: ") + "Level " + k.getLevelMinimum(), "",
							k.isUnlocked(p)
								? ChatColor.GREEN + "" + ChatColor.BOLD + "UNLOCKED"
								: ChatColor.RED + "" + ChatColor.BOLD + "NOT PURCHASED" + ChatColor.RESET + "" + ChatColor.GRAY + " (Click to purchase)"));
			column++;
		}

	}
	
}

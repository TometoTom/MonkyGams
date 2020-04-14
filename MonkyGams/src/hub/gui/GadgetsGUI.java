package hub.gui;

import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import hub.gadgets.meta.GadgetType;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.Utils;
import utils.gui.MonkyGUI;
import utils.gui.PurchaseGUI;

public class GadgetsGUI {
	
	MonkyGUI gui;
	Player p;

	public void present() {
		gui.present(p);
	}

	public GadgetsGUI(Player p) {

		this.p = p;
		
		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(p);

		gui = new MonkyGUI(p, "Cosmetics & Gadgets", 6);
		for (GadgetType gt : GadgetType.values()) {
			gui.getInventory().addItem(new MonkyItemStack(gt.getIconInInventory())
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + gt.getName() + " (" + ps.getGadgets().getOrDefault(gt, 0) + ")")
					.setLore(ChatColor.GRAY + gt.getDescription(), "",
							ChatColor.GRAY + Utils.bold("Left click") + " to equip",
							ChatColor.GRAY + Utils.bold("Right click") + " to purchase more"));
		}
		gui.setClickEvent(ClickType.LEFT, (clicker, item) -> {
			for (GadgetType gt : GadgetType.values()) {
				if (gt.getIconInInventory() == item.getType()) {
					if (p.getInventory().contains(gt.getIconInHotbar()))
						return;
					for (GadgetType alreadyExistingGadget : GadgetType.values()) {
						p.getInventory().remove(alreadyExistingGadget.getIconInHotbar());
					}
					clicker.closeInventory();
					clicker.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10, 1);
					clicker.getInventory().addItem(new MonkyItemStack(gt.getIconInHotbar())
							.setName(ChatColor.GOLD + "" + ChatColor.BOLD + gt.getName())
							.setLore(ChatColor.GRAY + gt.getDescription(), "", ChatColor.GRAY + "" + ChatColor.BOLD + "Cooldown: " + gt.getCooldown() + " seconds"));
				}
			}
		});
		gui.setClickEvent(ClickType.RIGHT, (clicker, item) -> {
			Arrays.asList(GadgetType.values()).forEach((gt) -> {
				if (gt.getIconInInventory() == item.getType()) {
					new PurchaseGUI(clicker, gt.getIconInInventory(), ChatColor.GOLD + "" + ChatColor.BOLD + gt.getName(), ChatColor.GRAY + gt.getDescription(), 99999, gt.getPrice(), (amount) -> {
						ps.getGadgets().put(gt, ps.getGadgets().getOrDefault(gt, 0) + amount);
					}).present(clicker);
				}
			});
		});

	}

}

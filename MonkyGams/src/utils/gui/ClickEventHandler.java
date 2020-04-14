package utils.gui;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.Utils;
import utils.game.GameUtils;

public class ClickEventHandler implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {

		if (e.getCurrentItem() == null)
			return;

		if (e.getCurrentItem().getType() == Material.BARRIER) {
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
		}
		
		for (Entry<String, MonkyGUI> gui : MonkyGUI.guis.entrySet()) {
			if (gui.getValue().getName().equalsIgnoreCase(e.getView().getTitle()) && e.getWhoClicked().getName().equalsIgnoreCase(gui.getKey())) {
				e.setCancelled(true);
				ClickEvent ce = gui.getValue().getClickEvent(e.getClick());
				if (ce != null) {
					Player p = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());
					p.playSound(p.getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 10, 1);
					ce.onClick(p, e.getCurrentItem());
				}
			}
		}

	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {

		Player p = Bukkit.getPlayer(e.getPlayer().getName());
		MonkyGUI closedGUI = MonkyGUI.guis.remove(p.getName());
		
		if (closedGUI == null)
			return;
		
		if (closedGUI.getInventory().getViewers().stream().anyMatch(he -> he.getName().equalsIgnoreCase(p.getName()))) {
			
			if (closedGUI instanceof PurchaseGUI) {

				PurchaseGUI purchase = (PurchaseGUI) closedGUI;
				purchase.clearButton(p);

				MonkyGUI confirm = new MonkyGUI(p, "Abandon the purchase?", 3);
				MonkyItemStack yes = new MonkyItemStack(Material.LIME_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "Yes, I'd like to abandon the purchase.");
				MonkyItemStack no = new MonkyItemStack(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "No, I'd like to continue with my purchase");

				for (int i = 0; i!=3*9; i++) {
					int column = i % 9;
					if (column < 3) {
						confirm.getInventory().setItem(i, yes);
					}
					if (column > 5) {
						confirm.getInventory().setItem(i, no);
					}
				}
				confirm.setClickEvent(ClickType.LEFT, (clicker, item) -> {
					if (item.getType() == Material.LIME_STAINED_GLASS_PANE) {
						clicker.sendMessage(GameUtils.getFailureMessage("PURCHASE", "You cancelled your purchase of "
								+ Utils.bold((purchase.maxQuantity == 1 ? "" : purchase.getTotalItems() + "x ") + purchase.itemName) + ChatColor.RESET + ChatColor.GRAY + "."));
						clicker.closeInventory();
					}
					if (item.getType() == Material.RED_STAINED_GLASS_PANE) {
						purchase.present(clicker);
					}
				});
				GameUtils.delayTask(() -> confirm.present(p), 1);
			}

		}

	}
}

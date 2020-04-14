package games.game.elytrabattle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import games.meta.GameController;
import games.meta.GameType;
import games.meta.Kit;
import utils.Utils;
import utils.game.GameUtils;

public class KitSelectorEvent implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Choose Kit")) {
			
			ItemStack i = e.getCurrentItem();
			
			if (i == null)
				return;
			
			Bukkit.getPlayer(e.getWhoClicked().getName()).playSound(e.getWhoClicked().getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 10, 1);
			
			for (Kit k : Kit.getKits(GameType.ELYTRABATTLE)) {
				if (k.getIcon() == i.getType()) {
					
					if (k.isUnlocked(Bukkit.getPlayer(e.getWhoClicked().getName()))) {
						e.getWhoClicked().closeInventory();
						GameController.getCurrentGame().getStats().getKits().put(e.getWhoClicked().getName(), k);
						e.getWhoClicked().sendMessage(GameUtils.getSuccessMessage("KIT", "You selected " + Utils.bold(k.getName()) + "."));
						return;
					}
					else {
						e.getWhoClicked().sendMessage(GameUtils.getFailureMessage("KIT", "You haven't unlocked this kit yet!"));
						return;
					}
					
				}
			}
			
		}
		
	}
	
}

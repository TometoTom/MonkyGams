package hub.gadgets.meta;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import core.data.PlayerStatisticsManager;
import games.meta.GameController;
import games.meta.GameType;
import utils.game.GameUtils;

public class GadgetUseEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (GameController.getCurrentGame().getType() != GameType.LOBBY)
			return;
		
		if (e.getItem() == null) return;
		if (e.getItem().getType() == Material.AIR) return;
		
		ItemStack item = e.getItem();
		
		for (GadgetType gt : GadgetType.values()) {
			if (item.getType() == gt.getIconInHotbar()) {
				try {
					HashMap<GadgetType, Integer> gadgets = PlayerStatisticsManager.getStatistics(e.getPlayer()).getGadgets();
					if (gadgets.getOrDefault(gt, 0) <= 0) {
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
						e.getPlayer().sendMessage(GameUtils.getFailureMessage("GADGET", "You don't have any more of this gadget left. Buy some more in the shop!"));
						return;
					}
					else {
						if (gt.getGadget().newInstance().enable(e.getPlayer()))
							gadgets.put(gt, gadgets.get(gt) - 1);
					}
				} catch (Exception ex) {
					e.getPlayer().sendMessage(GameUtils.getFailureMessage("GADGET", "Something went wrong when using your gadget."));
				}
			}
		}
		
	}
	
}

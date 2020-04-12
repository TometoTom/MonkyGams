package hub.gadgets.meta;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import utils.game.GameUtils;

public class GadgetUseEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (e.getItem() == null) return;
		if (e.getItem().getType() == Material.AIR) return;
		
		ItemStack item = e.getItem();
		
		for (GadgetType gt : GadgetType.values()) {
			if (item.getType() == gt.getIconInHotbar()) {
				try {
					gt.getGadget().newInstance().enable(e.getPlayer());
				} catch (Exception ex) {
					e.getPlayer().sendMessage(GameUtils.getFailureMessage("GADGET", "Something went wrong when using your gadget."));
				}
			}
		}
		
	}
	
}

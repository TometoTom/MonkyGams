package utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ClickEvent {

	public void onClick(Player p, ItemStack i);
	
}

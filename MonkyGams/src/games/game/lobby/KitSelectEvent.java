package games.game.lobby;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import games.meta.GameController;
import games.meta.Kit;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import utils.Utils;
import utils.game.GameUtils;

public class KitSelectEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		
		if (e.getHand() != EquipmentSlot.HAND) return;
		
		if (e.getRightClicked().getType() == EntityType.VILLAGER) {
			e.setCancelled(true);
			for (Kit k : Kit.getKits(GameController.getCurrentGame(LobbyGame.class).getQueuedGame())) {
				if (k.getIcon() == ((Villager) e.getRightClicked()).getEquipment().getItemInMainHand().getType()) {
					if (k.isUnlocked(e.getPlayer())) {
						GameController.putKit(e.getPlayer(), k);
						TextComponent here = new TextComponent(Utils.bold(k.getName()));
						here.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + k.getDescription()).create()));
						e.getPlayer().spigot().sendMessage(new ComponentBuilder().append(GameUtils.getSuccessMessage("KIT", "You chose kit ")).append(here).append(ChatColor.GRAY + ".").create());
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					}
					else {
						e.getPlayer().sendMessage(GameUtils.getFailureMessage("KIT", "You haven't unlocked this kit yet! Use /kits to view the kit shop."));
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
						return;
					}
				}
			}
		}
	}
	
}

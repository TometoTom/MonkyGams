package games.game.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;

public class GameMenuEvents implements Listener {
	
	public static MonkyItemStack ENDER_EYE = new MonkyItemStack(Material.ENDER_EYE)
			.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Game Menu")
			.setLore(ChatColor.GRAY + "Game server controller");
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.hasItem()) return;
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if (e.getHand() != EquipmentSlot.HAND) return;
		if (e.getItem().getType() == Material.ENDER_EYE) {
			e.setCancelled(true);
			e.getPlayer().openInventory(getInventory());
		}
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		if (e.getView().getTitle().equalsIgnoreCase("Choose Game")) {
			for (GameType gt : GameType.values()) {
				if (gt.getIcon() == e.getCurrentItem().getType()) {
					if (e.isRightClick()) {
						e.getWhoClicked().openInventory(getMapInventory(gt));
					}
					else {
						((LobbyGame) GameController.getCurrentGame()).setQueuedGame(gt);
						((LobbyGame) GameController.getCurrentGame()).setQueuedMap(null);
						Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "LOBBY: " + ChatColor.RESET + "" + ChatColor.GRAY
								+ e.getWhoClicked().getName() + " has changed the game to " + gt.getName() + ".");
						Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1));
						e.getWhoClicked().closeInventory();
					}
					return;
				}
			}
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME SELECTOR: " + ChatColor.RESET + "" + ChatColor.GRAY + "There isn't a game registered under that icon.");
		}
		if (e.getView().getTitle().toLowerCase().startsWith("choose map for ")) {
			
			for (GameType gt : GameType.values()) {
				if (gt.getName().equalsIgnoreCase(e.getView().getTitle().substring("Choose Map for ".length()).toUpperCase())) {
					Map m = Map.getMap(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()), gt);
					((LobbyGame) GameController.getCurrentGame()).setQueuedGame(gt);
					((LobbyGame) GameController.getCurrentGame()).setQueuedMap(m);
					Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "LOBBY: " + ChatColor.RESET + "" + ChatColor.GRAY
							+ e.getWhoClicked().getName() + " has changed the game to " + gt.getName() + ".");
					Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1));
					e.getWhoClicked().closeInventory();
					return;
				}
			}
			e.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME SELECTOR: " + ChatColor.RESET + "" + ChatColor.GRAY + "There isn't a game registered under that title.");
			return;
		}
	}
	
	public Inventory getInventory() {
		Inventory i = Bukkit.createInventory(null, 3 * 9, "Choose Game");
		for (GameType type : GameType.values()) {
			if (type != GameType.LOBBY) {
				i.addItem(new MonkyItemStack(type.getIcon())
						.setName(ChatColor.GOLD + "" + ChatColor.BOLD + type.getName())
						.setLore(ChatColor.GRAY + "Left click to select game", ChatColor.GRAY + "Right click to select map"));
			}
		}
		return i;
	}
	
	public Inventory getMapInventory(GameType gt) {
		Inventory i = Bukkit.createInventory(null, 3 * 9, "Choose Map for " + gt.getName());
		for (Map m : Map.getMaps(gt)) {
			i.addItem(new MonkyItemStack(Material.PAPER)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + m.getMapName())
					.setLore(ChatColor.GRAY + "Click to choose this map"));
		}
		return i;
	}
	
}

package games.game.lobby;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;
import net.md_5.bungee.api.ChatColor;

public class MapChooserEvents implements Listener {
	
	public Inventory getGUI(Player p, GameType t) {
		
		ArrayList<Map> maps = Map.getMaps(t);
		int rows = maps.size() / 9 + 1;
		int remainder = maps.size() % 9;
		
		/* Add another row because of integer division if there is a remainder */
		Inventory i = Bukkit.createInventory(null, rows * 9 + ((remainder > 0 ? 0 : 1) * 9), "Map Selection");
		
		String chosenMap = ((LobbyGame) GameController.getCurrentGame()).playerNameMapNameVotes.get(p.getName());
		
		for (Map m : Map.getMaps(t)) {
			
			int count = 0;
			for (Entry<String, String> e : ((LobbyGame) GameController.getCurrentGame()).playerNameMapNameVotes.entrySet()) {
				if (e.getValue().equalsIgnoreCase(m.mapName)) count++;
			}
			
			ItemStack map = new ItemStack(Material.PAPER);
			map.setAmount(count == 0 ? 1 : count);
			
			ItemMeta meta = map.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + m.mapName);
			if (t == GameType.ELYTRAPARKOUR) {
				String wr = "None set";
				if (m.getExtraInfo().size() != 0) {
					DecimalFormat df = new DecimalFormat("#.##");
					df.setRoundingMode(RoundingMode.CEILING);
					wr = df.format(((float) Long.valueOf(m.getExtraInfo().get(0))) / 1000F);
				}
				meta.setLore(Arrays.asList(ChatColor.GRAY + (count == 0 ? "No votes" : count + " vote" + (count == 1 ? "" : "s")), "", ChatColor.GRAY + "World Record: " + wr));
			}
			else {
				meta.setLore(Arrays.asList(ChatColor.GRAY + (count == 0 ? "No votes" : count + " vote" + (count == 1 ? "" : "s"))));
			}
			
			if (chosenMap != null && chosenMap.equalsIgnoreCase(m.mapName)) {
				meta.addEnchant(Enchantment.DURABILITY, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			map.setItemMeta(meta);
			
			i.addItem(map);
		}
		
		return i;
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if (e.getCurrentItem() == null) return;
		
		if (e.getView().getTitle().equalsIgnoreCase("Map Selection")) {
			String mapName = e.getCurrentItem().getItemMeta().getDisplayName();
			
			mapName = ChatColor.stripColor(mapName);
			
			HashMap<String, String> votes = ((LobbyGame) GameController.getCurrentGame()).playerNameMapNameVotes;
			votes.put(e.getWhoClicked().getName(), mapName);
			
			((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 10, 1);
			
			e.getWhoClicked().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "MAPS: " + ChatColor.RESET + ChatColor.GRAY + "You voted for " + mapName);
			
			LobbyGame lobbyGame = ((LobbyGame) GameController.getCurrentGame());
			lobbyGame.setQueuedMap(Map.getMap(lobbyGame.getVotedMap(), lobbyGame.getQueuedGame()));
			
			e.getWhoClicked().closeInventory();

		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getItem() == null) return;
		if (e.getItem().getType() == Material.PAPER) {
			e.setCancelled(true);
			e.getPlayer().openInventory(getGUI(e.getPlayer(), ((LobbyGame) GameController.getCurrentGame()).getQueuedGame()));
			return;
		}
	}
	
	
	
}

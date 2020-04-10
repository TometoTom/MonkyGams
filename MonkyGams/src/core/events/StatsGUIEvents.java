package core.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import games.meta.GameType;
import games.meta.statistics.GameStatistics;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.game.GameUtils;

public class StatsGUIEvents implements Listener {
	
	public static Inventory getInventory(Player p) {
		Inventory i = Bukkit.createInventory(null, 9 * 6, p.getName() + "'s Statistics");
		
		@SuppressWarnings("deprecation")
		ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwningPlayer(p);
		skullMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + p.getName() + "'s Statistics");
		skull.setItemMeta(skullMeta);
		
		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(p);
		
		MonkyItemStack stat1 = new MonkyItemStack(Material.PAPER)
				.setName(ChatColor.RED + "" + ChatColor.BOLD + "Pounds")
				.setLore(ChatColor.GRAY + "" + ps.getCurrency() + " pounds");
		MonkyItemStack stat2 = new MonkyItemStack(Material.PAPER)
				.setName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Level")
				.setLore(ChatColor.GRAY + "Level " + ps.getLevel(), ChatColor.GRAY + "" + ps.getTotalXP() + " XP");
		MonkyItemStack stat3 = new MonkyItemStack(Material.PAPER)
				.setName(ChatColor.GREEN + "" + ChatColor.BOLD + "Blocks Walked")
				.setLore(ChatColor.GRAY + "" + ps.getTotalBlocksWalked() + " blocks");
		MonkyItemStack stat4 = new MonkyItemStack(Material.PAPER).setName(ChatColor.AQUA + "" + ChatColor.BOLD + "Chat Messages Sent")
				.setLore(ChatColor.GRAY + "" + ps.getTotalChatMessages() + " messages");
		
		MonkyItemStack close = new MonkyItemStack(Material.BARRIER)
				.setName(ChatColor.RED + "" + ChatColor.BOLD + "Close");
		
		i.setItem(4, skull);
		i.setItem(8, close);
		i.setItem(19, stat1);
		i.setItem(21, stat2);
		i.setItem(23, stat3);
		i.setItem(25, stat4);
		
		int count = 36;
		for (GameType g : GameType.values()) {
			if (g.getStatsClass() == null) continue;
			i.setItem(count, new MonkyItemStack(g.getIcon())
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + g.getName())
					.setLore(ChatColor.GRAY + "Click here to see stats for this game."));
			count++;
		}
		while (count != 54) {
			i.setItem(count, new MonkyItemStack(Material.WHITE_STAINED_GLASS_PANE).setName(" "));
			count++;
		}
		
		return i;
		
	}
	
	public static Inventory getGameInventory(Player p, GameType gt) {
		
		if (p == null) {
			return null;
		}
		
		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(p);
		
		/* This means the player has gone offline in the time it has taken the other player to click on the button! */
		if (ps == null) {
			return null;
		}
		
		GameStatistics gs = ps.getStatistics(gt);
		
		if (gs == null) {
			System.out.println("Game stats is null");
		}
		
		Inventory i = Bukkit.createInventory(null, 6 * 9, p.getName() + "'s Statistics");
		
		i.setItem(0, new MonkyItemStack(Material.PAINTING)
				.setName(ChatColor.RED + "" + ChatColor.BOLD + "Back"));
		i.setItem(4, new MonkyItemStack(gt.getIcon())
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + gt.getName()));
		i.setItem(8, new MonkyItemStack(Material.BARRIER)
				.setName(ChatColor.RED + "" + ChatColor.BOLD + "Close"));
		
		int count = 0;
		for (MonkyItemStack stack : gt.getStatsClass().cast(ps.getStatistics(gt)).getInventory()) {
			if (stack != null) i.setItem(count, stack);
			count++;
		}
		
		return i;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if (e.getCurrentItem() == null) return;
		
		String title = ChatColor.stripColor(e.getView().getTitle());
		
		if (title.endsWith("'s Statistics")) {
			e.setCancelled(true);
			((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 10, 1);
			
			if (e.getCurrentItem().getType() == Material.BARRIER) {
				e.getWhoClicked().closeInventory();
				return;
			}
			if (e.getCurrentItem().getType() == Material.PAINTING) {
				try {
					Player stats = Bukkit.getPlayer(title.substring(0, title.indexOf("'")));
					if (stats == null) throw new Exception();
					e.getWhoClicked().openInventory(getInventory(stats));
					return;
				} catch (Exception exception) {
					e.getWhoClicked().sendMessage(GameUtils.getFailureMessage("STATS", "That player has gone offline."));
					return;
				}
			}
			
			for (GameType gt : GameType.values()) {
				if (gt.getStatsClass() == null) continue;
				if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(gt.getName())) {
					Inventory i = getGameInventory(Bukkit.getPlayer(title.substring(0, title.indexOf("'"))), gt);
					if (i == null) {
						e.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "STATS: " + ChatColor.RESET + "" + ChatColor.GRAY + "This player has now gone offline.");
						return;
					}
					e.getWhoClicked().openInventory(i);
				}
			}

		}
	}

}

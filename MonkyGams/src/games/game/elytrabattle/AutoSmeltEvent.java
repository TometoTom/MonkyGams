package games.game.elytrabattle;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;

public class AutoSmeltEvent implements Listener {

	@EventHandler
	public void onMine(BlockBreakEvent e) {
		
		e.setExpToDrop(0);
		e.setDropItems(false);
		
		Material m = e.getBlock().getType();
		
		if (m == Material.WHITE_STAINED_GLASS || m == Material.CHEST) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME: " + ChatColor.RESET + "" + ChatColor.GRAY + "You cannot break this block.");
			return;
		}
	
		if (m == Material.IRON_ORE) {
			e.getPlayer().getInventory().addItem(new MonkyItemStack(Material.IRON_INGOT).setNumber(3));
		}
		else if (m == Material.GOLD_ORE) {
			e.getPlayer().getInventory().addItem(new MonkyItemStack(Material.GOLD_INGOT).setNumber(3));
		}
		else if (m == Material.DIAMOND_ORE) {
			e.getPlayer().getInventory().addItem(new MonkyItemStack(Material.DIAMOND).setNumber(3));
			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).diamondsMined++;
		}
		else if (m == Material.EMERALD_ORE) {
			e.getPlayer().getInventory().addItem(new MonkyItemStack(Material.EMERALD).setNumber(3));
		}
		else if (m == Material.LAPIS_ORE) {
			e.getPlayer().getInventory().addItem(new MonkyItemStack(Material.LAPIS_BLOCK).setNumber(1).setLore(ChatColor.GRAY + "Creates a water source block when placed."));
		}
		else if (m == Material.REDSTONE_ORE) {
			e.getPlayer().getInventory().addItem(new MonkyItemStack(Material.REDSTONE_BLOCK).setNumber(1).setLore(ChatColor.GRAY + "Creates a lava source block when placed."));
		}
		else {
			e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand()).forEach(item -> e.getPlayer().getInventory().addItem(item));
		}
		
	}
	
}

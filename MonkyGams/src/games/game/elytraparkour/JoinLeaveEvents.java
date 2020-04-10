package games.game.elytraparkour;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import games.meta.GameController;
import net.md_5.bungee.api.ChatColor;

public class JoinLeaveEvents implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		ElytraParkourGame game = (ElytraParkourGame) GameController.getCurrentGame();
		
		e.getPlayer().teleport(game.getMap().getSpawn());
		e.getPlayer().getInventory().clear();
		
		ItemStack elytra = new ItemStack(Material.ELYTRA);
		ItemMeta meta = elytra.getItemMeta();
		meta.setUnbreakable(true);
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Elytra");
		elytra.setItemMeta(meta);
		e.getPlayer().getInventory().setChestplate(elytra);
		e.getPlayer().setExp(1F);
		
		e.getPlayer().getInventory().setItem(8, ShowHidePlayerEvents.GREEN_DYE);
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		
		GameController.getCurrentGame().getStats().getDeaths().remove(e.getPlayer().getName());
		
	}
	
}

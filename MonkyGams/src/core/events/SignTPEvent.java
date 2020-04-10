package core.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import core.Main;
import net.md_5.bungee.api.ChatColor;

public class SignTPEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		Block b = e.getClickedBlock();
		
		if (b.getType() == Material.BIRCH_WALL_SIGN && b.getLocation().getBlockX() == -156 && b.getLocation().getBlockY() == 98 && b.getLocation().getBlockZ() == 265) {
			e.getPlayer().sendMessage("you're a fucking idiot");
		}
		
		if (b.getType() != Material.OAK_WALL_SIGN) return;
		
		Sign sign = (Sign) b.getState();
		
		try {
			String line = sign.getLine(1).replaceAll("[\\[\\] ]", "");
			line = ChatColor.stripColor(line);
			
			if (line.startsWith("Server")) {
				line = line.substring(line.indexOf("Server") + "Server".length());
				e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SERVER: " + ChatColor.GRAY + "Sending you to Server " + line + "...");
				Main.sendPlayerToServer(e.getPlayer(), line);
				return;
			}
			
			String[] coords = line.split(",");
			
			e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TELEPORT: " + ChatColor.RESET + "" + ChatColor.GRAY + "You teleported to " + coords[0] + ", " + coords[1] + ", " + coords[2] + ".");
			e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2])));
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
	}
	
}

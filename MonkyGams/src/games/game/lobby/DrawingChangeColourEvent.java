package games.game.lobby;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import games.meta.GameController;
import net.md_5.bungee.api.ChatColor;

public class DrawingChangeColourEvent implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (e.getAction() != Action.RIGHT_CLICK_AIR) return;

		if (e.getItem() == null) return;
		if (e.getItem().getType() != Material.SPECTRAL_ARROW) return;

		Block b = e.getPlayer().getTargetBlock(null, 20);
		
		if (b.getType().toString().endsWith("_CONCRETE")) {
			
			Material m = Material.valueOf(b.getType().toString().substring(0, b.getType().toString().indexOf("_")) + "_WOOL");
			
			((LobbyGame) GameController.getCurrentGame()).getDrawingManager().selectedColours.put(e.getPlayer().getName(), m);

			e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "LOBBY: " + ChatColor.RESET + "" + ChatColor.GRAY + "Switched colour to "
					+ b.getType().toString().substring(0, b.getType().toString().indexOf("_")).toLowerCase() + ".");
		}

	}

}

package games.game.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;


public class InventoryShutEvent implements Listener{

	@EventHandler
	public void onClose(InventoryCloseEvent e){
		
		boolean a = false;
		
		for (Player p : Bukkit.getOnlinePlayers()){
			if (e.getView().getTitle().equalsIgnoreCase(p.getName())) a = true;
		}
		
		if (a==true){
			
			Player p = (Player) e.getPlayer();
			Player t = Bukkit.getPlayer(e.getView().getTitle());
			
			t.getInventory().setContents(e.getInventory().getContents());
			p.sendMessage("Saved inventory.");
			return;
			
		}
		else return;
		
	}
	
}

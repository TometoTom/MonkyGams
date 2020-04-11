package games.game.kitpvp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InteractInventoryEvent implements Listener{

	@EventHandler
	public void onInteractEvent(InventoryClickEvent e){
		
		if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "KITS")){
			
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			Player p = (Player) e.getWhoClicked();
			Material m = item.getType();
			
			for (Kit kit : Kit.values()){
				
				if (m==kit.getMaterial()){
					
					if (kit.isEnabled()){
						Kit.kits.put(p, kit);
						p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "KIT: " + ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD +  "You selected " + kit.getName() + ".");
						ArrowHitEvent.players.remove(p);
						PlayerDamageEvent.gymlads.remove(p);
						PlayerDamageEvent.players.remove(p);
						CooldownManager.cooldowns.remove(p);
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 10);
						p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 40);
						p.closeInventory();
						return;
					}
					else{
						p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "KIT: " + ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD +  "You cannot select " + kit.getName() + "; it's disabled.");
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
						p.closeInventory();
						return;
					}
					
				}
				
			}
			
		}
		
		if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "CHOOSE A MAP")){
			
			e.setCancelled(true);
			
			Duel d = DuelCommand.dl;
			
			for (DuelMap map : DuelMap.values()){
				
				if (map.getMaterial()==e.getCurrentItem().getType()){
					
					d.setMap(map);
					
				}
				
			}
			
			e.getWhoClicked().closeInventory();
			
			new KitInventory(d.p1, "CHOOSE YOUR KIT");
			new KitInventory(d.p2, "CHOOSE YOUR KIT");
		}
		
		if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "CHOOSE YOUR KIT")){
			
			e.setCancelled(true);

			Duel d = DuelCommand.dl;
			
			ItemStack item = e.getCurrentItem();
			Player p = (Player) e.getWhoClicked();
			p.sendMessage("a");
			Material m = item.getType();
			
			for (Kit kit : Kit.values()){
				
				if (m==kit.getMaterial()){
					
					DuelData.kits.put(p, kit);
					
					p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DUEL: " + ChatColor.RESET + "" + ChatColor.GRAY + "You chose " + kit.getName() + " for the duel.");
					
					ArrowHitEvent.players.remove(p);
					PlayerDamageEvent.gymlads.remove(p);
					PlayerDamageEvent.players.remove(p);
					CooldownManager.cooldowns.remove(p);
					p.closeInventory();
				}
				
			}
			
			if (DuelData.kits.containsKey(d.p1)){
				if (DuelData.kits.containsKey(d.p2)){
					d.startDuel();
					return;
				}
				
				else{
					p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DUEL: " + ChatColor.RESET + "" + ChatColor.GRAY + "Waiting for " + d.p2.getName() + " to select a kit.");
					return;
				}
				
			}
		}
	}
}

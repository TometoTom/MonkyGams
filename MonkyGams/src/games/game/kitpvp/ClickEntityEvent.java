package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

import core.Main;

public class ClickEntityEvent implements Listener{

	String s = ChatColor.GOLD + "" + ChatColor.BOLD + "SKILL: " + ChatColor.RESET + "" + ChatColor.GRAY;
	public static boolean hitEntity = false;
	public static ArrayList<Player> minecarts = new ArrayList<Player>();
	
	@EventHandler
	public void onEntityRightClick(PlayerInteractEntityEvent e) {
	    final Player p = e.getPlayer();
	    final Player t = (Player) e.getRightClicked();
	    if (t==null) return;
		if (p.getInventory().getItemInMainHand().getType()!=Material.DIAMOND_SWORD) return;
	    if (Kit.kits.containsKey(p)){
	    	if (Kit.kits.get(p)==Kit.ICE){
				if (CooldownManager.cooldowns.contains(p)){
					p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
					return;
				}
				hitEntity = true;
				p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " on " + t.getName() + ".");
				MovementEvent.players.add(t.getName());
				t.sendMessage(s + "You've been frozen by " + p.getName());
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
				t.playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
				CooldownManager.addCooldown(p);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					 
                    public void run() {
                        
                    	t.sendMessage(s + "You've been unfrozen!");
        				MovementEvent.players.remove(t.getName());
        				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 10);
        				t.playSound(t.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 10);
        				hitEntity = false;
                    }
                }, 60);
				
	    	}
	    	
	    	if (Kit.kits.get(p)==Kit.CLINTON){
				if (CooldownManager.cooldowns.contains(p)){
					p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
					return;
				}
				
				final String name = t.getName();
				final ItemStack sword = t.getInventory().getItem(0);
				
				hitEntity = true;
				p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " on " + t.getName() + ".");
				t.sendMessage(s + p.getName() + " gave you pneumonia!");
				p.playSound(p.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
				t.playSound(p.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
				t.damage(20);
				t.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Hillary Clinton");
				t.setVelocity(p.getLocation().getDirection().setY(0.5).multiply(2.5));
				t.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
				final Item ent = (Item) t.getWorld().dropItemNaturally(t.getLocation(), new ItemStack(Material.WHITE_WOOL));
				ent.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "Hillary Clinton");
				t.addPassenger(ent);
				ent.setPickupDelay(99999999);
				ent.setCustomNameVisible(true);
				ent.setVelocity(p.getLocation().getDirection().multiply(2.5));
				CooldownManager.addCooldown(p);
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					 
                    public void run() {
                        
                    	t.sendMessage(s + "You recovered from pneumonia!");
        				p.playSound(p.getLocation(), Sound.ENTITY_BAT_HURT, 1, 10);
        				t.playSound(t.getLocation(), Sound.ENTITY_BAT_HURT, 1, 10);
        				hitEntity = false;
        				
        				t.setDisplayName(name);
        				t.getInventory().setItemInMainHand(sword);

        				ent.remove();
        				
                    }
                }, 60);
				
	    	}
	    	
	    	if (Kit.kits.get(p)==Kit.HUNT){
				if (CooldownManager.cooldowns.contains(p)){
					p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
					return;
				}
				
				hitEntity = true;
				p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " on " + t.getName() + ".");
				t.sendMessage(s + p.getName() + " took away your heathcare!");
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
				t.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
				CooldownManager.cooldowns.add(p);
				
				final int taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
                    public void run() {
						
        				t.damage(0);
                    	
                }
            }, 0, 1);
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					 
                    public void run() {
                        
						p.sendMessage(s + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " wore off.");
						t.sendMessage(s + "You got your free healthcare back.");
						Bukkit.getServer().getScheduler().cancelTask(taskID);
						
                    }
                }, 80);
	    	}
	    	
	    	if (Kit.kits.get(p)==Kit.JARVIS){
				if (CooldownManager.cooldowns.contains(p)){
					p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
					return;
				}
				
				hitEntity = true;
				p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " on " + t.getName() + ".");

				CooldownManager.cooldowns.add(p);
				
				t.kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "Network Banned");
				
	    	}
	    	
	    	
	    	
	    	if (Kit.kits.get(p)==Kit.KHAN){
				if (CooldownManager.cooldowns.contains(p)){
					p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
					return;
				}
				
				hitEntity = true;
				p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " on " + t.getName() + ".");
				t.sendMessage(s + p.getName() + " gave you a ticket for the District Line Night Tube!");
				p.playSound(p.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1, 10);
				t.playSound(p.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1, 10);
				
				final Minecart m = (Minecart) t.getWorld().spawnEntity(t.getLocation().add(0, 2, 0), EntityType.MINECART);
				m.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "NightTube DISTRICT LINE");
				m.setCustomNameVisible(true);
				m.setDerailedVelocityMod(t.getVelocity());
				m.addPassenger(t);
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					 
                    public void run() {
                        
						p.sendMessage(s + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " wore off.");
						t.sendMessage(s + "You got off the NightTube.");
						m.remove();
						
                    }
                }, 80);
	    	}
	    	
	    }
	    
	}
	
		@EventHandler
		public void onExitWithMinecraft(VehicleExitEvent e) {
			if (minecarts.contains((Player) e.getExited())){
				e.setCancelled(true);
				e.getExited().sendMessage(s + "You can't leave the Night Tube!");
			}
	}
}

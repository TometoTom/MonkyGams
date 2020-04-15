package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import core.Main;

@SuppressWarnings("deprecation")
public class PlayerDamageEvent implements Listener{

	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Player> gymlads = new ArrayList<Player>();
	String s = ChatColor.GOLD + "" + ChatColor.BOLD + "SKILL: " + ChatColor.RESET + "" + ChatColor.GRAY;
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		
		if (e.getDamager().getType()==EntityType.ZOMBIE){
			Zombie z = (Zombie) e.getDamager();
			if (z.getCustomName().contains("immigrant")){
				if (e.getEntity().getType()==EntityType.PLAYER){
					Player p = (Player) e.getEntity();
					p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10, 10);
					p.damage(7);
					p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_DESTROY_DOOR, 10);
					z.setVelocity(z.getLocation().getDirection().multiply(-2));
				}
			}
		}
		
		if (e.getDamager() instanceof Snowball){
			
			Snowball s = (Snowball) e.getEntity();
			
			if (e.getEntity()==s.getShooter()){
				e.setCancelled(true);
			}
			
			else{
				if (e.getEntity().getType()==EntityType.PLAYER){
					Player t = (Player) e.getEntity();
					t.damage(7);
					t.playSound(t.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 10);
				}
			}
			
		}
		
		if (e.getDamager().getType()==EntityType.PLAYER){
			Player p = (Player) e.getDamager();
				if (e.getEntity().getType()==EntityType.ZOMBIE){
					Zombie z = (Zombie) e.getEntity();
					if (z.getCustomName().contains("immigrant")){
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 10);
						z.setVelocity(p.getLocation().getDirection().multiply(8).setY(5));
					}
				}
		}
		
		if (e.getEntityType()==EntityType.PLAYER){
			if(e.getDamager() instanceof Arrow) {
				   Arrow a = (Arrow)e.getDamager();
				   if(a.getShooter() instanceof Player) {
				      final Player p = (Player) a.getShooter();
				      final Player t = (Player) e.getEntity();
				      if (players.contains(p)){
				    	  
				    	  p.sendMessage(s + "You hit " + t.getName() + " with " + Kit.PUMPKIN.getSkillName());
				    	  t.sendMessage(s + p.getName() + " hit you with " + Kit.PUMPKIN.getSkillName());
				    	  players.remove(p);
				    	  
				    	  final ItemStack temp = t.getInventory().getHelmet();
				    	  t.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
				    	  t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 10000));
				    	  t.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10000, 10000));
				    	  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				    			 
			                    public void run() {
			                        
			        				t.getInventory().setHelmet(temp);
			                    	t.removePotionEffect(PotionEffectType.SLOW);
			                    	t.removePotionEffect(PotionEffectType.BLINDNESS);
			                    }
			                }, 100);
				    	  
				      }
				   }

				}
			
			else{
				
				if (e.getCause()==DamageCause.ENTITY_ATTACK){
					if (e.getEntityType()==EntityType.PLAYER){
						
						if (e.getDamager().getType()==EntityType.PLAYER){
							
							final Player p = (Player) e.getEntity();
							Player t = (Player) e.getDamager();
							if (!gymlads.contains(p)) return;
							
							/*Location l = t.getLocation().subtract(p.getLocation());
							double distance = t.getLocation().distance(p.getLocation());
							Vector v = l.toVector().multiply(2/distance);
							t.setVelocity(v);*/
							
							Double damage = e.getDamage(DamageModifier.ARMOR);
							e.setCancelled(true);
							p.damage(damage);

							t.playSound(t.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10, 1);
							
						}
						
					}
				}
				
			}
			
		}
		
	}
	
}

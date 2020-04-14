package games.game.kitpvp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import core.Main;

public class InteractEvent implements Listener{

	String s = ChatColor.GOLD + "" + ChatColor.BOLD + "SKILL: " + ChatColor.RESET + "" + ChatColor.GRAY;
	public static boolean canUseIce = false;
	ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	ArrayList<Item> items = new ArrayList<Item>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		
		final Player p = e.getPlayer();
		
		if (Kit.kits.containsKey(p)){
				
				if (Kit.kits.get(p)==Kit.AGILE){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						p.setWalkSpeed(0.4F);
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 10);
						CooldownManager.addCooldown(p);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		 
		                    public void run() {
		                        
		        				p.setWalkSpeed(0.2F);
		        				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		                    }
		                }, 100);
						
						return;
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.ICE){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							return;
						}
						
						canUseIce = true;
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		 
		                    public void run() {
		                        
		        				if (ClickEntityEvent.hitEntity==true) return;
		        				else {
		        					CooldownManager.addCooldown(p);
		        					p.sendMessage(s + "You missed " + Kit.kits.get(p).getSkillName() + "...");
		        				}
		                    }
		                }, 1);
						
						return;
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.CLINTON){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							return;
						}
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		 
		                    public void run() {
		                        
		        				if (ClickEntityEvent.hitEntity==true) return;
		        				else {
		        					CooldownManager.addCooldown(p);
		        					p.sendMessage(s + "You missed " + Kit.kits.get(p).getSkillName() + "...");
		        				}
		                    }
		                }, 1);
						
						return;
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.HUNT){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							return;
						}
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		 
		                    public void run() {
		                        
		        				if (ClickEntityEvent.hitEntity==true) return;
		        				else {
		        					CooldownManager.addCooldown(p);
		        					p.sendMessage(s + "You missed " + Kit.kits.get(p).getSkillName() + "...");
		        				}
		                    }
		                }, 1);
						
						return;
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.KHAN){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							return;
						}
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		 
		                    public void run() {
		                        
		        				if (ClickEntityEvent.hitEntity==true) return;
		        				else {
		        					CooldownManager.addCooldown(p);
		        					p.sendMessage(s + "You missed " + Kit.kits.get(p).getSkillName() + "...");
		        				}
		                    }
		                }, 1);
						
						return;
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.JARVIS){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							return;
						}
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		 
		                    public void run() {
		                        
		        				if (ClickEntityEvent.hitEntity==true) return;
		        				else {
		        					CooldownManager.addCooldown(p);
		        					p.sendMessage(s + "You missed " + Kit.kits.get(p).getSkillName() + "...");
		        				}
		                    }
		                }, 1);
						
						return;
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.CORBYN){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 10);

						
						final int taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
	                        public void run() {
	    						
	                        	if (p.getHealth() <= 4){
	                        		
	                        		p.setHealth(20);
	                        		p.sendMessage(s + "You were saved by " + Kit.kits.get(p).getSkillName());
	                        		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 10);
	                        		
	                        	}
	                        	
	                    }
	                }, 0, 2);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							 
		                    public void run() {
		                        
								p.sendMessage(s + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + " wore off.");
								Bukkit.getServer().getScheduler().cancelTask(taskID);
								CooldownManager.addCooldown(p);
		                    }
		                }, 140);
						
						
						return;
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.TRUMP){
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						
						
						ArrayList<Block> sight = (ArrayList<Block>) p.getLineOfSight((Set<Material>) null, 20);
						
						int i = 0;
						for (final Block b : sight){
							
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
								 
			                    public void run() {
			                        
			                    			b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 45, 10);
					                    	b.setType(Material.BRICKS);
					                    	
					                    	Location loc = b.getLocation();
					                    	
					                    	while(loc.subtract(0, 1, 0).getBlock().getType()==Material.AIR){
						                    	loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 45, 30);
					                    		loc.getBlock().setType(Material.BRICKS);
					                    	}
			                    }
			                }, i);
							
							i++;
						}
						
					}
					
				}
				
				if (Kit.kits.get(p)==Kit.EXPLOSIVE){
					
					if (e.getAction()==Action.RIGHT_CLICK_AIR) return;
					if (e.getAction()==Action.RIGHT_CLICK_BLOCK) return;
					
					if (e.getItem().getType()==Material.BOW){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						ArrowHitEvent.players.add(p);
						p.sendMessage(s + "You charged " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);
						CooldownManager.addCooldown(p);
					}
				}
				
				if (Kit.kits.get(p)==Kit.PUMPKIN){
					
					if (e.getAction()==Action.RIGHT_CLICK_AIR) return;
					if (e.getAction()==Action.RIGHT_CLICK_BLOCK) return;
					
					if (e.getItem().getType()==Material.BOW){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						PlayerDamageEvent.players.add(p);
						p.sendMessage(s + "You charged " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);
						CooldownManager.addCooldown(p);
					}
				}
				
				if (Kit.kits.get(p)==Kit.CAMERON){
	
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						if (p.getLocation().subtract(0, 1, 0).getBlock().getType()==Material.AIR){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " while in the air.");
							return;
						}
						
						p.setVelocity(p.getLocation().getDirection().multiply(-2).setY(0.75));
						
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						CooldownManager.addCooldown(p);
						p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 10);
						
						ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
						 
						SkullMeta meta = (SkullMeta) skull.getItemMeta();
						meta.setOwner("DavidCameron");
						skull.setItemMeta(meta);
						final ItemStack helm = p.getInventory().getHelmet();
						p.getInventory().setHelmet(skull);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							 
		                    public void run() {
		                        
								p.getInventory().setHelmet(helm);
		                    	
		                    }
		                }, 40);
					}
				}
				
				if (Kit.kits.get(p)==Kit.FARAGE){
	
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						
						final int taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
		                        public void run() {
		    						if (p.isBlocking()){
			                        	p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 10);
			                        	Zombie v = (Zombie) p.getWorld().spawnEntity(p.getLocation().add(0, 2, 0), EntityType.ZOMBIE);
			                        	String name = Names.getName();
			                        	ChatColor color = Names.getRandomColour();
			                        	v.setCustomName(color + "" + ChatColor.BOLD + name + " the immigrant");
			                        	v.setCustomNameVisible(false);
			                        	v.setCanPickupItems(false);
			                        	v.setVelocity(p.getLocation().getDirection().multiply(3));
			                        	v.setBaby(false);
			                        	EntityEquipment ee = v.getEquipment();
			                            ee.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
			                            ee.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			                            ee.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
			                            ee.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
			                            ee.setBootsDropChance(0);
			                            ee.setLeggingsDropChance(0);
			                            ee.setChestplateDropChance(0);
			                            ee.setHelmetDropChance(0);
			                            
			                            Item i = p.getWorld().dropItemNaturally(v.getEyeLocation(), new ItemStack(Material.CLAY_BALL));
			                        	i.setCustomName(color + "" + ChatColor.BOLD + name + " the immigrant");
			                        	i.setCustomNameVisible(true);
			                        	i.setPickupDelay(99999999);
			                        	v.addPassenger(i);
			                            
			                        	zombies.add(v);
			                        	items.add(i);
		    						}
		                        }
		                }, 0, 2);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							 
		                    public void run() {
		                        
								Bukkit.getServer().getScheduler().cancelTask(taskID);
								CooldownManager.addCooldown(p);
								
		                    }
		                }, 40);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							 
		                    public void run() {
		                        
		                    	for (Zombie z : zombies){
		                    		z.damage(9999);
		                    	}
		                    	for (Item i : items){
		                    		i.remove();
		                    	}
								
		                    }
		                }, 160);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							 
		                    public void run() {
		                        
								for (Zombie v : zombies){
									if (v.isOnGround()){
										v.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4));
										for (Entity ent : v.getNearbyEntities(20, 20, 20)){
											if (ent instanceof Player){
												if (ent.getName()==p.getName()) continue;
												else{
													Player t = (Player) ent;
													v.setTarget(t);
												}
											}
										}
									}
								}
								
		                    }
		                }, 20);
						
					}
				}
				
				if (Kit.kits.get(p)==Kit.KEEMSTAR){
	
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						
						final int taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
								public void run() {
		    						if (p.isBlocking()){
			                        	
		    							ItemStack it = new ItemStack(Material.YELLOW_DYE);
		    							
		    							Item i = p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), it);
		    							i.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "POPCORN");
		    							i.setCustomNameVisible(true);
		    							i.setPickupDelay(9999999);
		    							i.setVelocity(p.getLocation().getDirection().multiply(4));
		    							
		    							Snowball s = (Snowball) p.getWorld().spawnEntity(p.getLocation(), EntityType.SNOWBALL);
		    							s.setShooter(p);
		    							s.setBounce(true);
		    							s.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "POPCORN");
		    							s.setCustomNameVisible(true);
		    							s.setVelocity(p.getLocation().getDirection().multiply(4));
		    						}
		                        }
		                }, 0, 1);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							 
		                    public void run() {
		                        
								Bukkit.getServer().getScheduler().cancelTask(taskID);
								CooldownManager.addCooldown(p);
								
		                    }
		                }, 40);
						
					}
				}
				
				if (Kit.kits.get(p)==Kit.SNEAK){
	
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						HashSet<Material> transparent = new HashSet<Material>();
						transparent.add(Material.AIR);
						Block block = p.getTargetBlock(transparent, 15);
						
						for (Block b : p.getLineOfSight(transparent, 15)){
							b.getWorld().spawnParticle(Particle.CRIT_MAGIC, b.getLocation(), 1);
						}
						
						Location l = p.getLocation();
						Location n = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), l.getYaw(), l.getPitch());
						p.teleport(n.add(0, 2, 0));
						
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						CooldownManager.addCooldown(p);
						p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 10);
					}
				}
				
				if (Kit.kits.get(p)==Kit.GYMLAD){
	
					
					if (e.getAction()==Action.LEFT_CLICK_AIR) return;
					if (e.getAction()==Action.LEFT_CLICK_BLOCK) return;
					if (e.getAction()==Action.PHYSICAL) return;
					
					if (e.getItem().getType()==Material.DIAMOND_SWORD){

						if (CooldownManager.cooldowns.contains(p)){
							p.sendMessage(s + "You cannot use " + Kit.kits.get(p).getSkillName() + " yet.");
							return;
						}
						
						PlayerDamageEvent.gymlads.add(p);
						p.sendMessage(s + "You used " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ChatColor.RESET + "" + ChatColor.GRAY + ".");
						p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 10);
						CooldownManager.addCooldown(p);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			    			 
		                    public void run() {
		                        
		        				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 10, 1);
		        				PlayerDamageEvent.gymlads.remove(p);
		                    	
		                    }
		                }, 80);
						
				}
			}
		}
	}
}

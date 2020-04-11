package games.game.kitpvp;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class DeathEvent implements Listener{

	public static HashMap<Player, Integer> killstreak = new HashMap<Player, Integer>();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){

		String with;
		String by;
		String msg;
		
		Player p = e.getEntity();
		LivingEntity killer = e.getEntity().getKiller();
		DamageCause dc = p.getLastDamageCause().getCause();
		
		e.getDrops().clear();
		
		if (dc==DamageCause.CUSTOM){
			e.setDeathMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DEATH: " + ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + "died with Skill.");
			return;
		}
		
		if (killer==null){
			
			if (dc==DamageCause.BLOCK_EXPLOSION) msg = "is a terrorist Alan Who Akber XD XD XD boom boom boom!!!";
			if (dc==DamageCause.CONTACT) msg = "got pricked by a cactus.";
			if (dc==DamageCause.DROWNING) msg = "can't swim very well and drowned themself.";
			if (dc==DamageCause.FALL) msg = "fell to their death because they're a dullard.";
			if (dc==DamageCause.FALLING_BLOCK) msg = "got squished by a falling anvil?";
			if (dc==DamageCause.FIRE) msg = "was unintentionally cremated alive.";
			if (dc==DamageCause.FIRE_TICK) msg = "was burnt so black that the gorilla population has increased by one.";
			if (dc==DamageCause.LAVA) msg = "had a nice swim in some red water and perished.";
			if (dc==DamageCause.LIGHTNING) msg = "was somehow struck by lightning?";
			if (dc==DamageCause.MAGIC) msg = "was killed with a MineKraft Potion Bottle (All rights reserved).";
			if (dc==DamageCause.POISON) msg = "got potioned to death?";
			if (dc==DamageCause.PROJECTILE) msg = "got arrow'd to death.";
			if (dc==DamageCause.STARVATION) msg = "starved to death. Should've gone to Tesco.";
			if (dc==DamageCause.SUFFOCATION) msg = "suffocated in a block looked quite stupid whilst doing so.";
			if (dc==DamageCause.SUICIDE) msg = "killed themself :( stop saying kys!!!";
			if (dc==DamageCause.THORNS) msg = "got pricked with the thorns enchantment which nobody actually uses anyway";
			if (dc==DamageCause.VOID) msg = "wasn't looking where they were going and fell into the void.";
			if (dc==DamageCause.WITHER) msg = "was killed by a wither.";
			else msg = "died.";
			e.setDeathMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DEATH: " + ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + " " + msg);
			return;
		}
		
		if (killer.getType()!=EntityType.PLAYER){
			by = killer.getCustomName();
			e.setDeathMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DEATH: " + ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + " was killed by " + by + ".");
			return;
		}
		
		if (killer.getType()==EntityType.PLAYER){
			
			Player k = (Player) killer;
			
			if ((p.getInventory().getItemInMainHand().getType() == Material.AIR) || (p.getInventory().getItemInMainHand() == null)){
				with = "Air";
			}
			else{
				with = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
			}
			
			by = k.getName();
			msg = "was killed by ";
			
			if (!killstreak.containsKey(k)) killstreak.put(k, 1);
			else killstreak.put(k, killstreak.get(k) + 1);
			
			int kills = killstreak.get(k);
			
			if (kills % 5 == 0){
				Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "KILLSTREAK: " + ChatColor.GRAY + k.getName() + " is on a killstreak of " + kills + "!");

			}
			
			if (!killstreak.containsKey(p)) killstreak.put(p, 0);
			else{
				if (killstreak.get(p) > 3){
					Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "KILLSTREAK: " + ChatColor.GRAY + k.getName() + " shut down " + p.getName() + "'s killstreak of " + killstreak.get(p) + "!");
					for (Player all : Bukkit.getOnlinePlayers()){
						all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 1);
					}
				}
			}
			killstreak.put(p, 0);
			
			e.setDeathMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "DEATH: " + ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + " was killed by " + k.getName() + " with " + with + ".");
			
			ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 3, (short) 1);
			ItemMeta applem = apple.getItemMeta();
			applem.setUnbreakable(true);
			applem.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Golden Apple");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GOLD + "+1 when you kill someone.");
			applem.setLore(lore);
			apple.setItemMeta(applem);
			e.getDrops().add(apple);
			
			return;
		}
		
	}
	
}

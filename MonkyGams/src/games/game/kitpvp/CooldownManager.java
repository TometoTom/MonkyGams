package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import core.Main;

public class CooldownManager {
	
	static String s = ChatColor.GOLD + "" + ChatColor.BOLD + "SKILL: " + ChatColor.RESET + "" + ChatColor.GRAY;
	public static ArrayList<Player> cooldowns = new ArrayList<Player>();
	
	public static void addCooldown(final Player p){
		
		if (!Kit.kits.containsKey(p)){
			Bukkit.getLogger().severe("Couldn't add a cooldown to the player. This is because they are not in the kits list.");
			return;
		}
		
		Kit kit = Kit.kits.get(p);
		
		int cooldown = kit.getCooldown();
		
		cooldowns.add(p);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			 
            public void run() {
                
            	cooldowns.remove(p);
            	p.sendMessage(s + "You can now use " + ChatColor.BOLD + Kit.kits.get(p).getSkillName() + ".");
            }
        }, cooldown * 20);
		
	}
	
}

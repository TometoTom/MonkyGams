package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class MovementEvent implements Listener{

	public static ArrayList<String> players = new ArrayList<String>();
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		
		Player p = e.getPlayer();
		
		if (DuelData.players.contains(p)){
			if (DuelData.cannotMove.contains(p)){
				e.setCancelled(true);
			}
		}
		
		if (players.contains(p.getName())) e.setCancelled(true);
		if (!Kit.kits.containsKey(p)) return;
		if (Kit.kits.get(p)!=Kit.FAT) return;
		if (p.getGameMode()==GameMode.CREATIVE) return;
		if (p.getLocation().subtract(0, 1, 0).getBlock().getType()!=Material.AIR) return;
		if (p.isFlying()) return;
		p.setAllowFlight(true);
	}
	
	@EventHandler
	public void onFlight(PlayerToggleFlightEvent e){
		
		Player p = e.getPlayer();
		
		if (p.getGameMode()==GameMode.CREATIVE) return;
		e.setCancelled(true);
		p.setAllowFlight(false);
		p.setFlying(false);
		
		if (CooldownManager.cooldowns.contains(p)){
			p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "SKILL: " + ChatColor.RESET + "" + ChatColor.GRAY + "You can't use " + Kit.FAT.getSkillName() + " yet.");
			return;
		}
		
		Entity ent = p.getWorld().spawnEntity(p.getLocation(), EntityType.FALLING_BLOCK);
		for (Entity entity : ent.getNearbyEntities(5, 5, 5)){
			
			if (entity instanceof Player){
				
				Player pl = (Player) entity;
				if (pl==p) continue;
				
				((Player) entity).damage(30);
				Vector v = pl.getLocation().getDirection().multiply(2);
				pl.setVelocity(v);
				pl.playSound(pl.getLocation(), Sound.ENTITY_SLIME_ATTACK, 10, 1);
				
			}

			if (entity instanceof Villager){
				
				((Villager) entity).damage(30);
				
			}
			
		}
		
		int points = 60;
		int size = 5;
				
		for (int i = 0; i < 360; i += 360/points) {
			double angle = (i * Math.PI / 180);
			double x = size * Math.cos(angle);
			double z = size * Math.sin(angle);
			Location loc = p.getLocation().add(x, 1, z);
			p.getWorld().spawnParticle(Particle.SLIME, loc, 1);
		}
		
		p.setVelocity(p.getLocation().getDirection().multiply(2.5));
		p.playSound(p.getLocation(), Sound.ENTITY_SLIME_ATTACK, 10, 1);
		CooldownManager.addCooldown(p);
		p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "SKILL: " + ChatColor.RESET + "" + ChatColor.GRAY + "You used " + Kit.FAT.getSkillName() + ".");
	}
	
}

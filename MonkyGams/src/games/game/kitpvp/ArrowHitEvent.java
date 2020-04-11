package games.game.kitpvp;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class ArrowHitEvent implements Listener{

	String s = ChatColor.GOLD + "" + ChatColor.BOLD + "SKILL: " + ChatColor.RESET + "" + ChatColor.GRAY;
	public static ArrayList<Player> players = new ArrayList<Player>();

	@EventHandler
	public void onHit(ProjectileHitEvent e){
		
		if (e.getEntity() instanceof Arrow){

			if (PlayerDamageEvent.players.contains(e.getEntity().getShooter())){
				((Player) e.getEntity().getShooter()).sendMessage(s + "You missed " + Kit.kits.get(e.getEntity().getShooter()).getSkillName() + "...");
				return;
			}
			
			if (players.contains(e.getEntity().getShooter())){
				
				BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
				Block hitBlock = null;
				
				while (iterator.hasNext()){
					hitBlock = iterator.next();
					if (hitBlock.getType() != Material.AIR){
						break;
					}
				}
				
				hitBlock.getLocation().getWorld().createExplosion(hitBlock.getLocation().getX(), hitBlock.getLocation().getY(), hitBlock.getLocation().getZ(), 0F, false, false);
				hitBlock.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, hitBlock.getLocation(), 1);
				Location loc = hitBlock.getLocation();
				Entity ent = hitBlock.getWorld().spawnEntity(loc, EntityType.FALLING_BLOCK);
				for (Entity entity : ent.getNearbyEntities(5, 5, 5)){
					
					if (entity instanceof Player){
						
						((Player) entity).damage(7);
						
						Player p = (Player) entity;
						Vector v = p.getLocation().getDirection().multiply(2);
						v.add(new Vector(0, 3, 0));
						p.setVelocity(v);
						
					}
					
				}
				ent.remove();
				players.remove(e.getEntity().getShooter());
				
			}			
		}
	}
}

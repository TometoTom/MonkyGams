package utils.game;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import core.Main;
import games.meta.Map;
import net.md_5.bungee.api.ChatColor;

public class GameUtils {

	/**
	 * 
	 * @param first
	 * @param second
	 * @return True or False depending on whether the player is within the locations (INCLUSIVE).
	 */
	public static boolean isWithinLocations(Location loc, Location first, Location second) {

		int minX = first.getBlockX();
		int minY = first.getBlockY();
		int minZ = first.getBlockZ();

		int maxX = second.getBlockX();
		int maxY = second.getBlockY();
		int maxZ = second.getBlockZ();

		if (minX > maxX) {
			int temp = minX;
			minX = maxX;
			maxX = temp;
		}
		if (minY > maxY) {
			int temp = minY;
			minY = maxY;
			maxY = temp;
		}
		if (minZ > maxZ) {
			int temp = minZ;
			minZ = maxZ;
			maxZ = temp;
		}

		return loc.getBlockX() >= minX && loc.getBlockX() <= maxX
				&& loc.getBlockY() >= minY && loc.getBlockY() <= maxY
				&& loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ;

	}
	
	public static void setInvisible(LivingEntity e) {
		PotionEffect pe = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false);
		e.addPotionEffect(pe);
	}

	public static String getStringFromDamageCause(DamageCause d) {
		
		switch (d) {
		case BLOCK_EXPLOSION: return "Explosion";
		case CRAMMING: return "Claustrophobia";
		case ENTITY_EXPLOSION: return "Explosion";
		case FALLING_BLOCK: return "Falling Blocks";
		case FIRE_TICK: return "Fire";
		case FLY_INTO_WALL: return "Flying into the wall";
		case HOT_FLOOR: return "Burning";
		case PROJECTILE: return "Archery";
		case CONTACT: return "Contact";
		case CUSTOM: return "Custom";
		case DRAGON_BREATH: return "Dragon Breath";
		case DROWNING: return "Drowning";
		case DRYOUT: return "Dryout";
		case ENTITY_ATTACK: return "Mobs";
		case ENTITY_SWEEP_ATTACK: return "Mobs";
		case FALL: return "Fall Damage";
		case FIRE: return "Fire";
		case LAVA: return "Lava";
		case LIGHTNING: return "Lightning";
		case MAGIC: return "Spells";
		case MELTING: return "Melting";
		case POISON: return "Poison";
		case STARVATION: return "Starvation";
		case SUFFOCATION: return "Suffocation";
		case SUICIDE: return "Suicide";
		case THORNS: return "Thorns";
		case VOID: return "Void";
		case WITHER: return "Wither";
		default: return d.name();
		}
		
	}
	
	public static void fireworks(Location loc){

		Location copy = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
		for (int i = 8; i > 0; i--){
			Firework f = (Firework) loc.getWorld().spawnEntity(copy, EntityType.FIREWORK);
			FireworkMeta fm = f.getFireworkMeta();
			Random ran = new Random();
			fm.addEffect(FireworkEffect.builder()
					.flicker(false)
					.trail(true)
					.with(Type.BALL_LARGE)
					.withColor(Color.fromRGB(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)))
					.withFade(Color.fromRGB(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)))
					.build());
			fm.setPower(2);
			f.setFireworkMeta(fm);
			copy.subtract(1, 0, 0);
		}

	}

	public static void fireworks(Location l, int rounds, int delay) {
		repeatTask(() -> fireworks(l), delay, rounds);
	}
	
	public static void fireworks(Map map, int rounds, int delay) {

		repeatTask(() -> fireworks(map.getSpawn()), delay, rounds);

	}

	public static boolean areLocationsEqual(Location first, Location second) {

		return first.getBlockX() == second.getBlockX()
				&& first.getBlockY() == second.getBlockY()
				&& first.getBlockZ() == second.getBlockZ();

	}

	public static void repeatTask(Runnable task, int initialDelay, int periodBetween, int times) {

		BukkitRunnable runnable = new BukkitRunnable() {
			private int count = 1;

			@Override
			public void run() {
				if (count >= times) {
					cancel();
				}
				task.run();
				count++;
			}
		};

		runnable.runTaskTimer(Main.getPlugin(), initialDelay, periodBetween);

	}

	public static void repeatTask(Runnable task, int periodBetween, int times) {
		repeatTask(task, 0, periodBetween, times);
	}
	
	public static int repeatTaskForever(Runnable task, int initialDelay, int periodBetween) {
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), task, initialDelay, periodBetween);
	}

	public static void delayTask(Runnable task, int delay) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), task, delay);
	}

	public static void doDeathEffect(Player p) {

		p.spawnParticle(Particle.FLASH, p.getLocation(), 1);
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 5, false, false));

	}

	public static String getSuccessMessage(String title, String contents) {
		return ChatColor.GREEN + "" + ChatColor.BOLD + title + ": " + ChatColor.RESET + "" + ChatColor.GRAY + contents;
	}
	public static String getFailureMessage(String title, String contents) {
		return ChatColor.RED + "" + ChatColor.BOLD + title + ": " + ChatColor.RESET + "" + ChatColor.GRAY + contents;
	}

}

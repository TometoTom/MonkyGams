package games.game.monkykart;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PowerUp {

	BLOOPER("Blooper", Material.INK_SAC, RelativeCoursePosition.MIDDLE, (game, player) -> {
		Bukkit.getOnlinePlayers().forEach((p) -> {
//			if (p.getName().equalsIgnoreCase(player.getName())) return;
			p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation().add(0, 1, 0), 1);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8 * 20, 1, false, false, false));
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
		});
	}),
	POTATO("Potato", Material.POISONOUS_POTATO, RelativeCoursePosition.FRONT, null),
	BULLET("Bullet Bill", Material.FIREWORK_ROCKET, RelativeCoursePosition.BACK, null);

	public enum RelativeCoursePosition {
		FRONT, TRAILING_FRONT, MIDDLE, BACK, LAST;
	}

	private String name;
	private Material icon;
	private RelativeCoursePosition position;
	private PowerUpExecution execution;

	private PowerUp(String name, Material icon, RelativeCoursePosition position, PowerUpExecution execution) {
		this.name = name;
		this.icon = icon;
		this.position = position;
		this.execution = execution;
	}

	public String getName() {
		return name;
	}
	public Material getIcon() {
		return icon;
	}
	public RelativeCoursePosition getPosition() {
		return position;
	}
	public PowerUpExecution getExecution() {
		return execution;
	}

}

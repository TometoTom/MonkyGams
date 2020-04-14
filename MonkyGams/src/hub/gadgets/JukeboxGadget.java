package hub.gadgets;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import hub.gadgets.meta.Gadget;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import utils.game.GameUtils;

public class JukeboxGadget extends Gadget {
	
	public static boolean isPlaying = false;
	
	Location jukebox;
	int particlesId = -1;
	Player owner;
	
	@Override
	public void onEnable(Player p) {
		
		if (isPlaying) {
			p.sendMessage(GameUtils.getFailureMessage("GADGET", "There's already a jukebox playing in this lobby."));
			disable(p);
			return;
		}
		
		jukebox = p.getLocation();
		owner = p; 
		
		p.getLocation().getBlock().setType(Material.JUKEBOX);
		
		Jukebox j = (Jukebox) jukebox.getBlock().getState();
		ItemStack record = new ItemStack(getRandomRecord());
		j.setRecord(record);
		j.setPlaying(record.getType());
		j.update(true);
		startParticles();
		
		registerEvent(new Listener() {
			@EventHandler
			public void onInteract(PlayerInteractEvent e) {
				if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.JUKEBOX && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (e.getPlayer().getName().equalsIgnoreCase(owner.getName())) {
						e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "To disable your jukebox, just break it!"));
					}
				}
			}
		});
		
		registerEvent(new Listener() {
			@EventHandler
			public void onBlockBreak(BlockBreakEvent e) {
				if (e.getBlock().getType() == Material.JUKEBOX) {
					disable(owner);
				}
			}
		});
		
	}
	
	public void startParticles() {
		
		particlesId = GameUtils.repeatTaskForever(() -> {
			Random r = new Random();
			jukebox.getWorld().spawnParticle(Particle.NOTE,
				new Location(jukebox.getWorld(), jukebox.getBlockX() + (r.nextInt(3) - 1), jukebox.getBlockY() + r.nextInt(2) + 1, jukebox.getBlockZ() + (r.nextInt(3) - 1)),
				1);
			
			Jukebox j = (Jukebox) jukebox.getBlock().getState();
			if (!j.isPlaying()) {
				disable(owner);
			}
		}, 20, 10);
		
	}
	
	public Material getRandomRecord() {
		
		Material[] records = new Material[] {
			Material.MUSIC_DISC_BLOCKS,
			Material.MUSIC_DISC_CAT,
			Material.MUSIC_DISC_CHIRP,
			Material.MUSIC_DISC_FAR,
			Material.MUSIC_DISC_MALL,
			Material.MUSIC_DISC_MELLOHI,
			Material.MUSIC_DISC_STAL,
			Material.MUSIC_DISC_STRAD,
			Material.MUSIC_DISC_WAIT,
			Material.MUSIC_DISC_WARD
		};
		
		return records[new Random().nextInt(records.length)];
		
	}

	@Override
	public void onDisable(Player p) {
		
		if (jukebox == null) {
			return;
		}
		
		Jukebox j = (Jukebox) jukebox.getBlock().getState();
		j.setPlaying(null);
		j.setRecord(null);
		
		jukebox.getBlock().setType(Material.AIR);
		jukebox.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, jukebox, 1);
		Bukkit.getOnlinePlayers().forEach((player) -> player.playSound(jukebox, Sound.ENTITY_GENERIC_EXPLODE, 1, 1));
		Bukkit.getScheduler().cancelTask(particlesId);
		
		deregisterAllEvents();
	}

}

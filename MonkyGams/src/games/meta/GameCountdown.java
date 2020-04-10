package games.meta;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import core.Main;
import core.events.StopMovementEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import utils.game.GameUtils;

public class GameCountdown {

	private StopMovementEvent e = new StopMovementEvent();
	private int countdownID = -1;
	private int countDown;
	private int countDownRemaining;
	private Runnable countdownEnd;
	private boolean stopMovement;
	private HashMap<Chicken, String> chickens = new HashMap<>();
	
	public GameCountdown(Runnable countdownEnd, int seconds, boolean stopMovement) {
		this.countdownEnd = countdownEnd;
		this.countDown = seconds;
		this.countDownRemaining = seconds;
		this.stopMovement = stopMovement;
	}
	
	public void start() {
		
		if (stopMovement) {
			Bukkit.getOnlinePlayers().forEach(this::stopMovement);
		}
		
		countdownID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Thread(() -> {

			if (countDownRemaining <= 0) {

				for (Player p : Bukkit.getOnlinePlayers()) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("GO").color(ChatColor.BOLD).color(ChatColor.GREEN).create());
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 9);
				}

				Bukkit.getScheduler().cancelTask(countdownID);
				HandlerList.unregisterAll(e);
				countdownEnd.run();

			}

			String countdownLetters = "";
			for (int i = 0; i < countDown - countDownRemaining; i++) {
				countdownLetters += ChatColor.GREEN + "●";
			}
			for (int i = 0; i != countDownRemaining; i++) {
				countdownLetters += ChatColor.RED + "●";
			}

			for (Player p : Bukkit.getOnlinePlayers()) {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(countdownLetters).create());
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1);
			}

			chickens.entrySet().forEach(e -> {
				e.getKey().removePassenger(Bukkit.getPlayer(e.getValue()));
				e.getKey().remove();
			});
			
			countDownRemaining--;
		}), 0, 20);

	}
	public void stop() {
		Bukkit.getScheduler().cancelTask(countdownID);
		
		chickens.entrySet().forEach(e -> {
			e.getKey().removePassenger(Bukkit.getPlayer(e.getValue()));
			e.getKey().remove();
		});
	}
	
	public void stopMovement(Player p) {
		Chicken c = (Chicken) p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
		c.setInvulnerable(true);
		c.setAI(false);
		c.setBaby();
		GameUtils.setInvisible(c);
		c.setSilent(true);
		c.addPassenger(p);
		chickens.put(c, p.getName());
	}
	
}

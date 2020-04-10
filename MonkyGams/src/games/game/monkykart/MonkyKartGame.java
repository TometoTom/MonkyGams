package games.game.monkykart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.comphenix.packetwrapper.WrapperPlayClientSteerVehicle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import core.Main;
import games.meta.Game;
import games.meta.GameType;
import games.meta.Map;
import utils.MonkyItemStack;
import utils.game.GameUtils;

public class MonkyKartGame extends Game {

	HashMap<String, Entity> carts = new HashMap<String, Entity>();
	int schedulerId = -1;

	public MonkyKartGame(Map map) {
		super(GameType.MONKYKART, map);
	}

	@Override
	protected void registerListeners() {
		MonkyKartGame game = this;
		
		registerEvent(new Listener() {
			@EventHandler
			public void onInteract(PlayerInteractEntityEvent e) {
				if (e.getRightClicked() instanceof EnderCrystal) {
					e.getRightClicked().remove();
					e.getPlayer().spawnParticle(Particle.BUBBLE_POP, e.getRightClicked().getLocation(), 10);
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					e.setCancelled(true);
					doRoll(e.getPlayer());
				}
			}
		});
		registerEvent(new Listener() {
			@EventHandler
			public void onInteract(PlayerInteractEvent e) {
				if (e.getItem() == null) return;
				for (PowerUp pu : PowerUp.values()) {
					if (pu.getIcon() == e.getItem().getType()) {
						pu.getExecution().doPowerUp(game, e.getPlayer());
						e.getPlayer().getInventory().clear();
					}
				}
			}
		});
	}

	@Override
	protected void start() {

		Bukkit.getOnlinePlayers().forEach(p -> {
			p.teleport(getMap().getSpawn());
		});

		broadcastIntroduction("Mario Kart in Minecraft!","Complete 3 laps before everyone else.", "Use the power-ups to help you along the way.");
		doCountdown(() -> {

			deregisterAllEvents();
			registerListeners();

			Bukkit.getOnlinePlayers().forEach(p -> {
				Chicken b = (Chicken) p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
				b.addPassenger(p);
				b.setSilent(true);
				b.setInvulnerable(true);
				b.setRemoveWhenFarAway(false);
				b.setAware(false);
				carts.put(p.getName(), b);
			});

			ProtocolManager manager = ProtocolLibrary.getProtocolManager();
			manager.addPacketListener(new PacketAdapter(Main.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Client.STEER_VEHICLE) {
				@Override
				public void onPacketReceiving(PacketEvent e) {
					WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(e.getPacket());
					if (wrapper.getUnmount()) {
						e.setCancelled(true);
						return;
					}

					Chicken c = (Chicken) carts.get(e.getPlayer().getName());

					if (wrapper.getJump()) {
						Bukkit.broadcastMessage("Jump!");
						if (c.isOnGround()) {
							Vector currentVelocity = c.getVelocity();
							c.setVelocity(currentVelocity.setY(currentVelocity.getY() + 0.5));
						}
					}

					if (wrapper.getForward() > 0) {
						c.setVelocity(e.getPlayer().getLocation().getDirection().setY(0).multiply(0.5));
					}
					else if (wrapper.getForward() < 0) {
						c.setVelocity(e.getPlayer().getLocation().getDirection().setY(0).multiply(-0.5));
					}

					if (wrapper.getSideways() > 0) {
						c.setVelocity(e.getPlayer().getLocation().getDirection().setY(0).rotateAroundY(90).multiply(0.3));
						Bukkit.broadcastMessage("Left!");
					}
					else if (wrapper.getSideways() < 0) {
						c.setVelocity(e.getPlayer().getLocation().getDirection().setY(0).rotateAroundY(-90).multiply(0.3));
						Bukkit.broadcastMessage("Right!");
					}
				}
			});

			Location[] spawns = new Location[] {
					new Location(Bukkit.getWorld("void"), 9997, 102, 78),
					new Location(Bukkit.getWorld("void"), 9999, 102, 78),
					new Location(Bukkit.getWorld("void"), 10001, 102, 78),
					new Location(Bukkit.getWorld("void"), 10003, 102, 78),
					new Location(Bukkit.getWorld("void"), 10005, 102, 78),
			};
			for (Location spawn : spawns) {
				EnderCrystal box = (EnderCrystal) spawn.getWorld().spawnEntity(spawn, EntityType.ENDER_CRYSTAL);
				box.setInvulnerable(true);
				box.setGlowing(true);
				box.setShowingBottom(false);
			}

		});

		getScoreboard().addLine(RED + BOLD + "Time");
		getScoreboard().addLine(GREY + getScoreboardFriendlyTime());
		getScoreboard().addLine("");
		getScoreboard().addLine(YELLOW + BOLD + "Lap");
		getScoreboard().addLine(GREY + "Lap 1 of 3");
		getScoreboard().addLine("");
		getScoreboard().addLine(GREEN + BOLD + "Position");
		getScoreboard().addLine(GREY + "1st");
		getScoreboard().addLine("");
		getScoreboard().addLine(AQUA + BOLD + "Leaderboard");
		getScoreboard().addLine(GREY + "Placeholder");
		getScoreboard().showScoreboardToAll();
	}

	public void completeTrack(Player p) {

	}

	@Override
	protected void end() {
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.removePacketListeners(Main.getPlugin());
		carts.values().forEach(c -> c.remove());
		carts.clear();
		Bukkit.getScheduler().cancelTask(schedulerId);
	}

	public void doRoll(Player p) {
		int standardDelay = 2;
		
		PowerUp lastPowerUp = null;
		for (int i = 0; i != 25; i++) {
			int index = i;
			GameUtils.delayTask(() -> {
				List<PowerUp> possiblePowerUps = Arrays.asList(PowerUp.values());
				if (lastPowerUp != null)
					possiblePowerUps.remove(lastPowerUp);
				PowerUp pu = possiblePowerUps.get(new Random().nextInt(possiblePowerUps.size()));
				
				MonkyItemStack item = new MonkyItemStack(pu.getIcon()).setName(pu.getName());
				for (int hotbarIndex = 0; hotbarIndex != 9; hotbarIndex++) {
					p.getInventory().setItem(hotbarIndex, item);
				}
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, (float) ((index % 2) + 1));
			}, (int) ((standardDelay * i) + i / 2));
		}
		
		GameUtils.delayTask(() -> {
			PowerUp pu = PowerUp.BLOOPER;
			MonkyItemStack item = new MonkyItemStack(pu.getIcon()).setName(pu.getName());
			for (int hotbarIndex = 0; hotbarIndex != 9; hotbarIndex++) {
				p.getInventory().setItem(hotbarIndex, item);
			}
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 9);

			for (int i = 0; i!=6; i++) {
				int index = i;
				GameUtils.delayTask(() -> {
					for (int h = 0; h!=9; h++) {
						p.getInventory().setItem(h, new MonkyItemStack(pu.getIcon()).setName((index % 2 == 0 ? GREEN : GREY) + BOLD + pu.getName().toUpperCase()));
					}
				}, i * 7);
			}

		}, (standardDelay * 26) + 26 / 2);
	}

}

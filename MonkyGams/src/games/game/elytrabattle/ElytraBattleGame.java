package games.game.elytrabattle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import core.Main;
import core.data.PlayerStatisticsManager;
import games.game.lobby.LobbyGame;
import games.meta.Game;
import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;
import games.meta.statistics.ElytraBattleStatistics;
import utils.MonkyItemStack;
import utils.Utils;
import utils.game.GameListeners;
import utils.game.GameUtils;
import utils.npc.NPC;
import utils.npc.NPCManager;

public class ElytraBattleGame extends Game {

	public static Map MAP = new Map("Elytra Battle", "Minecraft", null, GameType.ELYTRABATTLE, null);

	public World playingWorld;

	private Reminders reminders = new Reminders();
	public Chests chests = new Chests();
	public HashMap<String, Kit> kits = new HashMap<>();
	private int chatRemindersId = -1;
	private int worldBorderId = -1;
	private boolean deathmatch = false;

	public ElytraBattleGame() {
		super(GameType.ELYTRABATTLE, MAP);
	}

	@Override
	protected void registerListeners() {

		registerEvent(new Listener() {
			@EventHandler
			public void onWorldInit(WorldInitEvent e) {
				e.getWorld().setKeepSpawnInMemory(false);
			}
		});

		registerEvent(GameListeners.getPreventRain());
		registerEvent(new Listener() {
			@EventHandler
			public void onDamage(EntityDamageEvent e) {
				if (e.getCause() == DamageCause.FALL) {
					e.setDamage(0);
					e.setCancelled(true);
				}
			}
		});
		registerEvent(new Listener() {
			@EventHandler
			public void onPortal(PlayerPortalEvent e) {
				e.setCancelled(true);
			}
		});
		registerEvent(new DamageEvent());
		registerEvent(new AutoSmeltEvent());
		registerEvent(new CoalInteractEvent());
		registerEvent(new TNTPhysicsEvents());
		registerEvent(new CobwebPhysics());
		registerEvent(new WaterLavaPlaceEvents());
		registerEvent(new SoupEvent());
		registerEvent(new RopeEvent());
		registerEvent(new DeathEvent());
		registerEvent(new FireballPhysics());
		registerEvent(new CompassEvents());
		registerEvent(new KitSelectorEvent());
		registerEvent(chests);
		deregisterEvent(new JoinEvent());

	}

	@Override
	protected void start() {

		/* Prevent breaking the hub */
		Bukkit.getOnlinePlayers().forEach(p -> p.setGameMode(GameMode.ADVENTURE));
		Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(GOLD + BOLD + "Loading map...", GREY + "Please wait...", 5, 5 * 20, 5));
		
		registerListeners();
		ElytraBattleBoost.start();
		chests.startParticles();

		if (Bukkit.getWorld("ELYTRABATTLE") != null) {
			Bukkit.unloadWorld("ELYTRABATTLE", true);
			File destDir = new File("." + File.separator + "ELYTRABATTLE");
			try {
				FileUtils.deleteDirectory(destDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		playingWorld = Bukkit.createWorld(new WorldCreator("ELYTRABATTLE").generateStructures(false));
		playingWorld.getWorldBorder().setSize(400);
		playingWorld.getWorldBorder().setDamageAmount(0.5);
		playingWorld.getWorldBorder().setDamageBuffer(0);
		playingWorld.setTime(1000);
		playingWorld.setDifficulty(Difficulty.NORMAL);
		playingWorld.setAnimalSpawnLimit(100000);    
		chests.spawnChests(playingWorld);

		Bukkit.getOnlinePlayers().forEach(p -> {
			teleportAboveMap(p, true);
			p.setExp(0);
			p.setGameMode(GameMode.SURVIVAL);

			MonkyItemStack pickaxe = new MonkyItemStack(Material.DIAMOND_PICKAXE)
					.setName(GOLD + BOLD + "Default Pickaxe")
					.addMonkyEnchant(Enchantment.DIG_SPEED, 5)
					.addMonkyEnchant(Enchantment.DURABILITY, 5);
			MonkyItemStack axe = new MonkyItemStack(Material.DIAMOND_AXE)
					.setName(GOLD + BOLD + "Default Axe")
					.addMonkyEnchant(Enchantment.DIG_SPEED, 5)
					.addMonkyEnchant(Enchantment.DURABILITY, 5);
			MonkyItemStack spade = new MonkyItemStack(Material.DIAMOND_SHOVEL)
					.setName(GOLD + BOLD + "Default Spade")
					.addMonkyEnchant(Enchantment.DIG_SPEED, 5)
					.addMonkyEnchant(Enchantment.DURABILITY, 5);
			p.getInventory().clear();
			p.getInventory().addItem(pickaxe);
			p.getInventory().addItem(axe);
			p.getInventory().addItem(spade);

			MonkyItemStack elytra = new MonkyItemStack(Material.ELYTRA).setName(GOLD + BOLD + "Elytra");
			ItemMeta meta = elytra.getItemMeta();
			meta.setUnbreakable(true);
			elytra.setItemMeta(meta);
			p.getInventory().setChestplate(elytra);
			LegacyPVP.enableLegacyPvp(p);
		});

		broadcastIntroduction("Use the 5m preparation time to get resources.",
				"There are chests around the map with resources.",
				"You can use coal to charge up your elytra.",
				"After 5m, the world border will " + BOLD + "rapidly" + RESET + GREY + " shrink.",
				"Be the last one standing!");

		getScoreboard().addLine("");
		getScoreboard().addLine(GREEN + BOLD + "Time until DM");
		getScoreboard().addLine(GREY + (getStartTime() == 0 ? "5 minutes" : Utils.getMinsSeconds(getStartTime() + 300000 - System.currentTimeMillis())));
		getScoreboard().addLine("");
		getScoreboard().addLine(YELLOW + BOLD + "Border Size");
		getScoreboard().addLine(GREY + "400 blocks");
		getScoreboard().showScoreboardToAll();

		doScoreboardUpdating(() -> {
			getScoreboard().setLine(2, GREY + (getStartTime() == 0 ? "5 minutes" : Utils.getMinsSeconds(getStartTime() + 300000 - System.currentTimeMillis())));
			getScoreboard().setLine(5, GREY + ((int) playingWorld.getWorldBorder().getSize()) + " blocks"); 
		});

		doCountdown(() -> {
			
			for (Entry<String, Kit> e : kits.entrySet()) {
				Player kitter = Bukkit.getPlayer(e.getKey());
				kitter.sendMessage(GameUtils.getSuccessMessage("GAME", "You are using kit " + e.getValue().getName() + "."));
				e.getValue().getGameStart().doKit(kitter, this);
			}
			
			resetStartTime();
			chatRemindersId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
				int secs = (int) (System.currentTimeMillis() - getStartTime());
				secs /= 1000;

				if (secs % 30 == 0 && secs % 60 != 0) {
					reminders.sayRandomReminder();
				}
				if (secs % 60 == 0) {
					int mins = 5 - (secs/60);
					Bukkit.broadcastMessage(GREEN + BOLD + "GAME: " + RESET + GREY + mins + " minute" + (mins == 1 ? "" : "s") + " until Deathmatch.");
					Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1));
				}

				int secsUntil = (5 * 60) - secs;
				if (secsUntil <= 5 && secsUntil > 0) {
					Bukkit.broadcastMessage(GREEN + BOLD + "GAME: " + RESET + GREY + secsUntil + " second" + (secsUntil == 1 ? "" : "s") + " until Deathmatch.");
					Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1));
				}
				if (secsUntil == 0) {
					Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1));
					startDeathmatch();
					Bukkit.getScheduler().cancelTask(chatRemindersId);
				}
			}, 0, 20);
		}, 10, false);

	}

	public void win(Player winner) {
		
		stopScoreboardUpdates();
		
		if (winner == null) {
			Bukkit.getOnlinePlayers().forEach((p) -> {
				p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Nobody won the game...", getScoreboardFriendlyTime(), 5, 7 * 20, 5);
			});
		}
		else {
			winner.setGlowing(true);
			Bukkit.getOnlinePlayers().forEach((p) -> {
				p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + winner.getName() + " won!", getScoreboardFriendlyTime(), 5, 7 * 20, 5);
			});
		}
		
		Random r = new Random();
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);	
			p.teleport(playingWorld.getHighestBlockAt(r.nextInt(10) - 10, r.nextInt(10) - 10).getLocation());
			p.setGameMode(GameMode.SURVIVAL);
		});
		
		GameUtils.fireworks(new Location(playingWorld, 0, 70, 0), 4, 40);
		
		Bukkit.getScheduler().cancelTask(chatRemindersId);
		
		Bukkit.broadcastMessage(getChatHeader());
		Bukkit.broadcastMessage(ChatColor.GRAY + "Winner: " + (winner == null ? "Nobody... " : winner.getName()));
		Bukkit.broadcastMessage(ChatColor.GRAY + "Time: " + getScoreboardFriendlyTime());
		Bukkit.broadcastMessage("");
		for (Entry<String, Integer> entry : getStats().getKills().entrySet()) {
			Bukkit.broadcastMessage(ChatColor.GRAY + entry.getKey() + " - " + entry.getValue() + " kill" + (entry.getValue() == 1 ? "" : "s"));
		}
		Bukkit.broadcastMessage(getChatFooter());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Thread(() -> {
			GameController.endCurrentGame();
		}), 20 * 8);
		
	}
	
	@Override
	protected void end() {

		Bukkit.getOnlinePlayers().forEach(p -> {
			p.getInventory().clear();
			p.setGameMode(GameMode.SURVIVAL);
			p.setGlowing(false);
		});
		
		Bukkit.getScheduler().cancelTask(chatRemindersId);
		Bukkit.getScheduler().cancelTask(worldBorderId);
		
		deregisterAllEvents();
		stopScoreboardUpdates();
		getScoreboard().unshowScoreboardForAll();

		ElytraBattleBoost.stop();
		chests.stopParticles();

		Bukkit.getOnlinePlayers().forEach(LobbyGame::teleport);

		Bukkit.unloadWorld("ELYTRABATTLE", true);
		File destDir = new File("." + File.separator + "ELYTRABATTLE");
		try {
			FileUtils.deleteDirectory(destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void startDeathmatch() {

		worldBorderId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			WorldBorder border = playingWorld.getWorldBorder();
			if (border.getSize() <= 100) {
				Bukkit.getScheduler().cancelTask(worldBorderId);
			}
			else {
				border.setSize(border.getSize() - 0.15);
			}
		}, 0, 1);
		
		deathmatch = true;

		broadcastIntroduction("This is deathmatch!", "The border is shrinking to 100x100 very quickly.", "Get to the centre and be the last one standing.");
		
		stopScoreboardUpdates();
		resetStartTime();
		getScoreboard().setLine(0, "");
		getScoreboard().setLine(1, GREEN + BOLD + "Deathmatch Time");
		getScoreboard().setLine(2, GREY + getScoreboardFriendlyTime());
		getScoreboard().setLine(3, "");
		getScoreboard().setLine(4, YELLOW + BOLD + "Border Size");
		getScoreboard().setLine(5, GREY + "400 blocks");
		getScoreboard().setLine(6, "");
		getScoreboard().setLine(7, AQUA + BOLD + "Players Alive");
		
		doScoreboardUpdating(() -> {
			getScoreboard().setLine(2, GREY + getScoreboardFriendlyTime());
			getScoreboard().setLine(5, GREY + ((int) playingWorld.getWorldBorder().getSize()) + " blocks"); 
			
			int count = 8;
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getGameMode() == GameMode.SURVIVAL) {
					getScoreboard().setLine(count, GREY + p.getName() + " " + getKills(p.getName()));
				}
				else {
					getScoreboard().setLine(count, GREY + STRIKETHROUGH + p.getName() + " " + getKills(p.getName()));
				}
				count++;
			}
			
		});
	}
	
	public String getKills(String p) {
		
		if (!getStats().getKills().containsKey(p)) {
			return "";
		}
		else {
			return "(" + getStats().getKills().get(p) + " ⚔️)";
		}
		
	}

	public void die(Player p) {

		GameUtils.doDeathEffect(p);
		
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setExp(0);
		p.setFireTicks(0);

		PlayerStatisticsManager.getStatistics(p).getStatistics(ElytraBattleStatistics.class).deaths++;
		getStats().getDeaths().increment(p.getName());
		
		if (!deathmatch) {
			teleportAboveMap(p, false);
			
			for (ItemStack i : p.getInventory().getContents()) {
				if (i == null || i.getType() != Material.COMPASS)
					continue;
				p.getWorld().dropItem(p.getLocation(), i).setPickupDelay(20);
				p.getInventory().remove(i);
				p.sendMessage(GameUtils.getFailureMessage("GAME", "You lost your compass due to death..."));
			}
			p.sendTitle(RED + BOLD + "You died!", GREY + "Please wait 10 seconds to respawn...", 5, 3 * 20, 5);
			
		}
		if (deathmatch) {
			p.setGameMode(GameMode.SPECTATOR);
			for (ItemStack i : p.getInventory().getContents()) {
				if (i == null)
					continue;
				p.getWorld().dropItem(p.getLocation(), i).setPickupDelay(20);
			}
			p.getInventory().clear();
			
			if (getAlivePlayers().size() == 1) {
				win(getAlivePlayers().get(0));
			}
			if (getAlivePlayers().size() == 0) {
				win(null);
			}
			else {
				p.sendTitle(RED + BOLD + "You died!", GREY + "Better luck next time...", 5, 3 * 20, 5);
			}
			
		}

	}

	public void teleportAboveMap(Player p, boolean firstTime) {

		Random r = new Random();
		Location randomLocation = new Location(playingWorld, r.nextInt(400) - 200 + 0.5, 200, r.nextInt(400) - 200 + 0.5);

		p.teleport(randomLocation);
		cage(p);
		
		if (firstTime) {
			NPC npc = new NPC(randomLocation.add(1, 0, 1), "7a5b3c66f4d145c2871e28cfc241bc4c", GOLD + BOLD + "CHOOSE KIT");
			npc.setClickEvent((player) -> {
				Inventory i = Bukkit.createInventory(null, ((Kit.values().length / 9) * 9) + 9, GOLD + BOLD + "Choose Kit");
				for (Kit k : Kit.values()) {
					MonkyItemStack item = new MonkyItemStack(k.getIcon())
					.setName(GOLD + BOLD + k.getName())
					.setLore(ChatColor.GRAY + k.getDescription(), "", GREEN + BOLD + "UNLOCKED");
					if (kits.containsKey(p.getName()) && kits.get(p.getName()) == k) {
						item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
						ItemMeta meta = item.getItemMeta();
						meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						item.setItemMeta(meta);
					}
					i.addItem(item);
				}
				player.openInventory(i);
			});
			npc.show(p);
			npc.register();
		}
		
		GameUtils.delayTask(() -> {
			p.spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation().subtract(0, 1, 0), 1);
			
			for (int x = randomLocation.getBlockX() - 2; x <= randomLocation.getBlockX() + 2; x++) {
				for (int y = randomLocation.getBlockY() - 1; y <= randomLocation.getBlockY() + 3; y++) {
					for (int z = randomLocation.getBlockZ() - 2; z <= randomLocation.getBlockZ() + 2; z++) {
						p.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
					}
				}
			}

			if (firstTime) {
				
				ArrayList<NPC> toRemove = new ArrayList<>();
				NPCManager.getNpcs().forEach(npc -> {
					npc.despawn();
					toRemove.add(npc);
				});
				toRemove.forEach((npc) -> {
					npc.deregister();
				});
				
			}
			
		}, 10 * 20);

	}

	public ArrayList<Player> getAlivePlayers(){
		
		ArrayList<Player> players = new ArrayList<>();
		
		Bukkit.getOnlinePlayers().forEach(p -> {
			if (p.getGameMode() == GameMode.SURVIVAL) {
				players.add(p);
			}
		});
		return players;
		
	}
	
	public void cage(Player p){

		ClipboardFormat format = ClipboardFormats.findByFile(getSchematic());
		try (ClipboardReader reader = format.getReader(new FileInputStream(getSchematic()))) {
			Clipboard clipboard = reader.read();

			com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(p.getWorld());
			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
			ClipboardHolder ch = new ClipboardHolder(clipboard);
			Operation operation = ch.createPaste(editSession)
					.to(BlockVector3.at(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()))
					.ignoreAirBlocks(false)
					.build();
			Operations.complete(operation);
			editSession.flushSession();
		}
		catch (Exception e) {
			p.getLocation().subtract(0, 1, 0).getBlock().setType(Material.WHITE_STAINED_GLASS);
			e.printStackTrace();
		}

	}

	public File getSchematic() {
		return new File(Main.getPlugin().getDataFolder() + "/schematics/Cage.schem");
	}
}

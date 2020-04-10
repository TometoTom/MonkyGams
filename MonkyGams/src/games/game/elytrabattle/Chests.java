package games.game.elytrabattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import core.Main;
import core.data.PlayerStatisticsManager;
import games.meta.statistics.ElytraBattleStatistics;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;

public class Chests implements Listener {

	private HashMap<MonkyItemStack, Float> items = new HashMap<>();
	private ArrayList<Chest> chests = new ArrayList<>();
	private int sID = -1;

	public Chests() {
		
		items.put(new MonkyItemStack(Material.MUSHROOM_STEW)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Instant Soup")
				.setLore(ChatColor.GRAY + "So healthy that it heals!"), 0.3F);
		items.put(new MonkyItemStack(Material.TNT)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Throwable TNT")
				.setLore(ChatColor.GRAY + "Can be thrown by left clicking!"), 0.45F);
		items.put(new MonkyItemStack(Material.DIAMOND)
				.setNumber(new Random().nextInt(3) + 1), 0.5F);
		items.put(new MonkyItemStack(Material.COAL)
				.setNumber(new Random().nextInt(3) + 1), 0.5F);
		items.put(new MonkyItemStack(Material.OAK_LOG)
				.setNumber(new Random().nextInt(3) + 1), 0.3F);
		items.put(new MonkyItemStack(Material.FEATHER)
				.setNumber(new Random().nextInt(7) + 1), 0.2F);
		items.put(new MonkyItemStack(Material.FLINT)
				.setNumber(new Random().nextInt(7) + 1), 0.2F);
		items.put(new MonkyItemStack(Material.SADDLE)
				.setNumber(1), 0.05F);
		items.put(new MonkyItemStack(Material.WOODEN_SWORD)
				.setNumber(1), 0.1F);
		items.put(new MonkyItemStack(Material.FIRE_CHARGE)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Fireball")
				.setLore(ChatColor.GRAY + "Can be thrown by left clicking!"), 0.3F);
		items.put(new MonkyItemStack(Material.COBWEB)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Throwable Cobweb")
				.setLore(ChatColor.GRAY + "Can be thrown by left clicking!")
				.setNumber(new Random().nextInt(1) + 1), 0.4F);
		items.put(new MonkyItemStack(Material.LEAD)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Rope")
				.setLore(ChatColor.GRAY + "Teleports you to surface level on click.")
				.setNumber(1), 0.1F);

	}

	public MonkyItemStack getRandomItem() {

		ArrayList<MonkyItemStack> list = new ArrayList<MonkyItemStack>();
		
		for (Entry<MonkyItemStack, Float> e : items.entrySet()) {
			int rarity = (int) (e.getValue() * 100);
			for (int i = 0; i != rarity; i++) {
				list.add(e.getKey());
			}
		}

		return list.get(new Random().nextInt(list.size()));

	}

	public void spawnChests(World w) {

		Random r = new Random();
		
		for (int i = 0; i != 35; i++) {

			Location randomLocation = new Location(w, r.nextInt(400) - 200, 0, r.nextInt(400) - 200);
			Block b = w.getHighestBlockAt(randomLocation).getLocation().add(0, 1, 0).getBlock();
			if (b.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WATER) continue;
			b.setType(Material.CHEST);
			Chest chest = (Chest) b.getState();
	
			Inventory inv = chest.getBlockInventory();
			for (int j = 0; j <= r.nextInt(4) + 2; j++) {
				inv.setItem(r.nextInt(27), getRandomItem());
			}
			
			chests.add(chest);
		}

	}

	public void startParticles() {
		
		sID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			chests.forEach((c) -> {
				Location location = c.getLocation();
				for (int degree = 0; degree < 6; degree++) {
					double radians = Math.toRadians(degree * 60);
					double x = Math.cos(radians);
					double z = Math.sin(radians);
					location.add(x + 0.5, 1, z + 0.5);
					c.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
					location.subtract(x + 0.5, 1, z + 0.5);
				}
			});
		}, 100, 5);
		
	}
	
	public void stopParticles() {
		
		Bukkit.getScheduler().cancelTask(sID);
		
	}
	
	public ArrayList<Chest> getChests(){
		return chests;
	}
	
	@EventHandler
	public void onChestClose(InventoryCloseEvent e){
		if (e.getInventory().getHolder() instanceof Chest){
			Chest c = (Chest) e.getInventory().getHolder();
			
			int items = 0;
			for (ItemStack i : c.getBlockInventory().getContents()) {
				if (i != null) items++;
			}
			
			if (items == 0) {
				PlayerStatisticsManager.getStatistics(Bukkit.getPlayer(e.getPlayer().getName())).getStatistics(ElytraBattleStatistics.class).chestsOpened++;
				c.getBlock().setType(Material.AIR);
				c.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, c.getLocation(), 1);
				Bukkit.getPlayer(e.getPlayer().getName()).playSound(c.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
				chests.remove(c);
			}
		}
	}

}

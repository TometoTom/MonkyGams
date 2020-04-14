package games.game.lobby;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import games.meta.GameType;
import games.meta.Kit;
import utils.game.GameUtils;

public class KitEntities {

	public ArrayList<Block> blocks = new ArrayList<Block>();
	private int id = -1;
	private World w;
	
	public void spawn(World w, GameType gt) {
		
		this.w = w;
		
		int i = 0;
		for (Kit k : Kit.getKits(gt)){
			
			Location chooser = new Location(w, 40, 87, -34, 90, 0); 
			Location copy = chooser;
			copy.add(0, 0, i);
			Location spawn = constructPhysicalSelection(copy);
			spawn = new Location(spawn.getWorld(), spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 90, 0);
			
			Villager villager = (Villager) spawn.getWorld().spawnEntity(spawn, EntityType.VILLAGER);
			villager.setAI(false);
			villager.setInvulnerable(true);
			villager.setSilent(true);
			villager.setCollidable(false);
			villager.setCanPickupItems(false);
			villager.getEquipment().setItemInMainHand(new ItemStack(k.getIcon()));
			
			ArmorStand hologram = (ArmorStand) spawn.getWorld().spawnEntity(spawn.add(0, 0.5, 0), EntityType.ARMOR_STAND);
			hologram.setVisible(false);
			hologram.setAI(false);
			hologram.setInvulnerable(true);
			hologram.setCollidable(false);
			hologram.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "KIT " + k.getName().toUpperCase());
			hologram.setCustomNameVisible(true);
			hologram.setGravity(false);
			
			ArmorStand line2 = (ArmorStand) spawn.getWorld().spawnEntity(spawn.subtract(0, 0.5, 0), EntityType.ARMOR_STAND);
			line2.setVisible(false);
			line2.setAI(false);
			line2.setInvulnerable(true);
			line2.setCollidable(false);
			line2.setCustomName(ChatColor.GRAY + "Click to choose this kit");
			line2.setCustomNameVisible(true);
			line2.setGravity(false);
			
			Location location = spawn.clone();
			for (int degree = 0; degree < 6; degree++) {
				double radians = Math.toRadians(degree * 60);
				double x = Math.cos(radians);
				double z = Math.sin(radians);
				location.add(x, 1, z);
				
				Villager item = (Villager) spawn.getWorld().spawnEntity(location, EntityType.VILLAGER);
				item.setAI(false);
				item.setSilent(true);
				item.setInvulnerable(true);
				item.setCollidable(false);
				item.setBaby();
				item.getEquipment().setItemInMainHand(new ItemStack(k.getIcon()));
				GameUtils.setInvisible(item);
				
				location.subtract(x, 1, z);
			}
						
			i += 4;
		}
		
		startTurning();
	}
	
	public void startTurning() {
		
		id = GameUtils.repeatTaskForever(() -> {
			w.getEntities().stream().filter(e -> e instanceof Villager).collect(Collectors.toList()).forEach(e -> {
				
				if (((Villager) e).isAdult())
					return;
				
				Location l = e.getLocation();
				float yaw = l.getYaw();
				yaw += 10;
				if (yaw > 360)
					yaw = yaw - 360;
				
				e.teleport(new Location(w, l.getX(), l.getY(), l.getZ(), yaw, l.getPitch()));
			});
		}, 0, 1);
		
	}
	
	public Location constructPhysicalSelection(Location middle){
		
		middle.getBlock().setType(Material.OAK_LOG);
		blocks.add(middle.getBlock());
		
		middle.add(0, 1, 0).getBlock().setType(Material.OAK_LOG);
		blocks.add(middle.getBlock());
		middle.subtract(0, 1, 0);
		
		middle.add(1, 0, 0).getBlock().setType(Material.STONE_BRICK_SLAB);
		blocks.add(middle.getBlock());
		
		middle.subtract(1, 0, 0);
		
		middle.subtract(1, 0, 0).getBlock().setType(Material.STONE_BRICK_SLAB);
		blocks.add(middle.getBlock());
		
		middle.add(1, 0, 0);
		
		middle.add(0, 0, 1).getBlock().setType(Material.STONE_BRICK_SLAB);
		blocks.add(middle.getBlock());
		
		middle.subtract(0, 0, 1);
		
		middle.subtract(0, 0, 1).getBlock().setType(Material.STONE_BRICK_SLAB);
		blocks.add(middle.getBlock());
		
		middle.add(0, 0, 1);
		
		middle.add(0, 2, 0);
		
		return middle;
	}
	
	public void clearPhysicalSelections(World w){
		
		for (Block b : blocks){
			b.setType(Material.AIR);
		}
		
		w.getEntities().stream()
			.filter(e -> e instanceof Villager || e instanceof ArmorStand)
			.collect(Collectors.toList())
		.forEach(e -> e.remove());
		
		Bukkit.getScheduler().cancelTask(id);
		
	}
	
	public static float radiansToDegrees(float radians){
		return (float) ((radians * 180) / Math.PI);
	}
	
	public static float degreesToRadians(float degrees){
		return (float) ((degrees * Math.PI)/180);
	}
	
	
}

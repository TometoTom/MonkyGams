package utils.npc;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import core.Main;

public class NPCManager {

	private static ArrayList<NPC> npcs = new ArrayList<>();
	private static HashMap<String, String> cache = new HashMap<String, String>();
	private static boolean npcTracking = true;
	
	static {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			
			if (!npcTracking) return;
			
			npcs.forEach((npc) -> {
				
				double closestDistance = 99999;
				Entity closestEntity = null;
				for (Entity e : npc.getPlayer().getBukkitEntity().getWorld().getNearbyEntities(npc.getLocation(), 4, 4, 4, o -> o instanceof Player)) {
					double d = e.getLocation().distanceSquared(npc.getLocation());
					if (d < closestDistance) {
						closestDistance = d;
						closestEntity = e;
					}
				}
				
				if (closestEntity == null) return;
				
				Location npcLoc = npc.getLocation();
				Location lookDirection = new Location(npcLoc.getWorld(), npcLoc.getX(), npcLoc.getY(), npcLoc.getX(), npcLoc.getYaw(), npcLoc.getPitch());
				lookDirection.setDirection(closestEntity.getLocation().subtract(lookDirection).toVector());
				
				npc.moveHead(lookDirection.getYaw(), lookDirection.getPitch());
				
			});
			
		}, 20 * 5, 1);
	}
	
	public static ArrayList<NPC> getNpcs() {
		return npcs;
	}
	
	public static HashMap<String, String> getCache(){
		return cache;
	}

	public static void showAllForPlayer(Player p) {
		npcs.forEach(npc -> npc.show(p));
	}
	
	public static void readCache() {
		String c = Main.getPlugin().getConfig().getString("skinCache");
		if (c == null) {
			return;
		}
		cache = new Gson().fromJson(c, new TypeToken<HashMap<String, String>>(){}.getType());
	}
	
	public static void saveCache() {
		Main.getPlugin().getConfig().set("skinCache", new Gson().toJson(cache));
	}
	
	public static void setNPCTracking(boolean b) {
		npcTracking = b;
	}
	
}

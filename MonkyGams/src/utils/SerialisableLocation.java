package utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerialisableLocation {

	public String worldName;
	public double x;
	public double y;
	public double z;
	public float pitch;
	public float yaw;
	
	public SerialisableLocation(String worldName, int x, int y, int z, float yaw, float pitch) {
		
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		
	}
	
	public SerialisableLocation(Location l) {
		
		this.worldName = l.getWorld().getName();
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
		this.yaw = l.getYaw();
		this.pitch = l.getPitch();
		
	}
	
	public Location getLocation() {
		
		return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
		
	}
	
}

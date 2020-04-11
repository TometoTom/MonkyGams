package games.game.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public enum DuelMap {

	ARENA("I Don't Know",
			0,
			Bukkit.getWorld("world"),
			new Location(Bukkit.getWorld("world"), -130.26327214655862, 93.0, 79.80914847892623, -28.159529f, -1.4332514f),
			new Location(Bukkit.getWorld("world"), -107.63514205725099, 93.0, 121.36579468996509, 151.23361f, -4.542108f),
			"Tom", Material.GRASS_BLOCK);
	
	private final String name;
	private final int id;
	private final World world;
	private final Location spawn1;
	private final Location spawn2;
	private final String builder;
	private final Material material;
	
	DuelMap(String name, int id, World world, Location spawn1, Location spawn2, String builder, Material material){
		
		this.name = name;
		this.id = id;
		this.world = world;
		this.spawn1 = spawn1;
		this.spawn2 = spawn2;
		this.builder = builder;
		this.material = material;
		
	}
	
	public String getName(){
		
		return name;
		
	}
	
	public int getID(){
		
		return id;
		
	}
	
	public World getWorld(){
		
		return world;
		
	}
	
	public Location getSpawn1(){
		
		return spawn1;
		
	}
	
	public Location getSpawn2(){
		
		return spawn2;
		
	}
	
	public String getBuilder(){
		
		return builder;
		
	}
	
	public Material getMaterial(){
		
		return material;
		
	}
	
}

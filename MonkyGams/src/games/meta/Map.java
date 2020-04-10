package games.meta;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

import utils.SerialisableLocation;

public class Map {

	public transient static ArrayList<Map> maps = new ArrayList<Map>();
	
	public String mapName;
	public String author;
	public SerialisableLocation spawnPoint;
	public GameType game;
	public ArrayList<String> extraInfo;
	
	public Map(String mapName, String author, SerialisableLocation spawnPoint, GameType game, ArrayList<String> extraInfo) {
		this.mapName = mapName;
		this.author = author;
		this.spawnPoint = spawnPoint;
		this.game = game;
		this.extraInfo = extraInfo == null ? new ArrayList<String>() : extraInfo;
	}
	
	public static Map getMap(String mapName, GameType game) {
		
		for (Map m : maps) {
			if (m.mapName.equalsIgnoreCase(mapName) && m.game == game) {
				return m;
			}
		}
		return null;
		
	}
	
	public static ArrayList<Map> getMaps(GameType game){
		ArrayList<Map> foundMaps = new ArrayList<>();
		for (Map m : maps) {
			if (m.game == game) foundMaps.add(m);
		}
		return foundMaps;
	}
	
	public World getWorld() {
		return spawnPoint.getLocation().getWorld();
	}
	
	public Location getSpawn() {
		return spawnPoint.getLocation();
	}
	public String getMapName() {
		return mapName;
	}
	public String getAuthor() {
		return author;
	}
	public GameType getGame() {
		return game;
	}

	public ArrayList<String> getExtraInfo() {
		return extraInfo;
	}
	
}

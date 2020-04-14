package games.meta.statistics;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

import games.meta.Kit;
import utils.MonkyHashMap;

public class DataRecorder {

	private MonkyHashMap<String> deaths;
	private HashMap<String, Location> spawns;
	private ArrayList<String> winners;
	private MonkyHashMap<String> kills;
	private HashMap<String, Long> joinTimes;
	private HashMap<String, Long> leaveTimes;
	private HashMap<String, Kit> kits;
	
	public DataRecorder() {
		this.deaths = new MonkyHashMap<>();
		this.spawns = new HashMap<>();
		this.winners = new ArrayList<>();
		this.kills = new MonkyHashMap<>();
		this.joinTimes = new HashMap<>();
		this.leaveTimes = new HashMap<>();
		this.kits  = new HashMap<>();
	}
	
	public MonkyHashMap<String> getDeaths() {
		return deaths;
	}
	public void setDeaths(MonkyHashMap<String> deaths) {
		this.deaths = deaths;
	}
	public HashMap<String, Location> getSpawns() {
		return spawns;
	}
	public void setSpawns(HashMap<String, Location> spawns) {
		this.spawns = spawns;
	}
	public ArrayList<String> getWinners() {
		return winners;
	}
	public void setWinners(ArrayList<String> winners) {
		this.winners = winners;
	}
	public MonkyHashMap<String> getKills() {
		return kills;
	}
	public void setKills(MonkyHashMap<String> kills) {
		this.kills = kills;
	}
	public HashMap<String, Long> getJoinTimes() {
		return joinTimes;
	}

	public HashMap<String, Long> getLeaveTimes() {
		return leaveTimes;
	}

	public HashMap<String, Kit> getKits() {
		return kits;
	}

	public void setKits(HashMap<String, Kit> kits) {
		this.kits = kits;
	}
	
}

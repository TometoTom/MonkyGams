package games.meta;

import org.bukkit.Material;

import games.meta.statistics.DefaultStatistics;
import games.meta.statistics.ElytraBattleStatistics;
import games.meta.statistics.ElytraParkourStatistics;
import games.meta.statistics.GameStatistics;
import games.meta.statistics.ParkourRaceStatistics;

public enum GameType {
	
	ELYTRAPARKOUR("Elytra Parkour", 1, Material.ELYTRA, ElytraParkourStatistics.class),
	PARKOURRACE("Parkour Race", 1, Material.FEATHER, ParkourRaceStatistics.class),
	ELYTRABATTLE("Elytra Battle", 1, Material.GOLDEN_SWORD, ElytraBattleStatistics.class),
	KITPVP("Kit PvP", 1, Material.SHIELD, null),
	MONKYKART("Monky Kart", 1, Material.MINECART, null),
	DROPPER("Dropper", 1, Material.CAULDRON, DefaultStatistics.class),
	LOBBY("Lobby", 0, Material.RED_BED, null);
	
	private String name;
	private int requiredPlayers;
	private Material icon;
	private Class<? extends GameStatistics> statsClass;
	
	private GameType(String name, int requiredPlayers, Material icon, Class<? extends GameStatistics> statsClass) {
		this.name = name;
		this.requiredPlayers = requiredPlayers;
		this.icon = icon;
		this.statsClass = statsClass;
	}

	public Class<? extends GameStatistics> getStatsClass() {
		return statsClass;
	}

	public String getName() {
		return name;
	}
	
	public Material getIcon() {
		return icon;
	}
	
	public int getRequiredPlayers() {
		return requiredPlayers;
	}
	
}

package games.meta;

import org.bukkit.Material;

import games.game.elytrabattle.Kit;
import games.meta.statistics.DefaultStatistics;
import games.meta.statistics.ElytraBattleStatistics;
import games.meta.statistics.ElytraParkourStatistics;
import games.meta.statistics.GameStatistics;
import games.meta.statistics.ParkourRaceStatistics;

public enum GameType {
	
	ELYTRAPARKOUR("Elytra Parkour", 1, Material.ELYTRA, null, ElytraParkourStatistics.class),
	PARKOURRACE("Parkour Race", 1, Material.FEATHER, null, ParkourRaceStatistics.class),
	ELYTRABATTLE("Elytra Battle", 1, Material.GOLDEN_SWORD, Kit.class, ElytraBattleStatistics.class),
	MONKYKART("Monky Kart", 1, Material.MINECART, null, null),
	DROPPER("Dropper", 1, Material.CAULDRON, null, DefaultStatistics.class),
	LOBBY("Lobby", 0, Material.RED_BED, null, null);
	
	private String name;
	private int requiredPlayers;
	private Material icon;
	private Class<?> kitsClass;
	private Class<? extends GameStatistics> statsClass;
	
	private GameType(String name, int requiredPlayers, Material icon, Class<?> kitsClass, Class<? extends GameStatistics> statsClass) {
		this.name = name;
		this.requiredPlayers = requiredPlayers;
		this.icon = icon;
		this.kitsClass = kitsClass;
		this.statsClass = statsClass;
	}

	public Class<? extends GameStatistics> getStatsClass() {
		return statsClass;
	}
	
	public Class<?> getKitsClass() {
		return kitsClass;
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

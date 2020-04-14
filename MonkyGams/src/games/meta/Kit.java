package games.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import core.data.PlayerStatisticsManager;
import games.game.elytrabattle.KitImplementation;

public class Kit {

	private static ArrayList<Kit> kits = new ArrayList<>();
	
	public static ArrayList<Kit> getKits() {
		return kits;
	}
	
	public static List<Kit> getKits(GameType gt) {
		
		return getKits().stream().filter(kit -> kit.getGame() == gt).collect(Collectors.toList());
	}
	
	private String name;
	private GameType game;
	private Material icon;
	private int levelMinimum;
	private int poundsCost;
	private String description;
	private KitImplementation gameStart;
	
	public Kit(String name, GameType game, Material icon, int levelMinimum, int poundsCost, String description,
			KitImplementation gameStart) {
		this.name = name;
		this.game = game;
		this.icon = icon;
		this.levelMinimum = levelMinimum;
		this.poundsCost = poundsCost;
		this.description = description;
		this.gameStart = gameStart;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Material getIcon() {
		return icon;
	}
	public void setIcon(Material icon) {
		this.icon = icon;
	}
	public int getPoundsCost() {
		return poundsCost;
	}
	public void setPoundsCost(int poundsCost) {
		this.poundsCost = poundsCost;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public KitImplementation getGameStart() {
		return gameStart;
	}
	public void setGameStart(KitImplementation gameStart) {
		this.gameStart = gameStart;
	}
	public GameType getGame() {
		return game;
	}
	public void setGame(GameType game) {
		this.game = game;
	}
	public int getLevelMinimum() {
		return levelMinimum;
	}
	public void setLevelMinimum(int levelMinimum) {
		this.levelMinimum = levelMinimum;
	}
	
	public String getKitId() {
		return game + ":" + name;
	}
	
	public boolean isUnlocked(Player p) {
		return PlayerStatisticsManager.getStatistics(p).getUnlockedKits().contains(getKitId());
	}
	
	public void setUnlocked(Player p) {
		if (!isUnlocked(p)) {
			PlayerStatisticsManager.getStatistics(p).getUnlockedKits().add(getKitId());
		}
	}
	
}

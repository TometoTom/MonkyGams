package games.game.elytrarun;

import java.util.ArrayList;

import games.meta.GameType;
import games.meta.Map;
import utils.SerialisableLocation;

public class ElytraRunMap extends Map {
	
	public ElytraRunMap(String mapName, String author, SerialisableLocation spawnPoint, GameType game, ArrayList<String> extraInfo) {
		super(mapName, author, spawnPoint, game, extraInfo);
	}

}

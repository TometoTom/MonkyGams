package games.game.kitpvp;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class DuelData {

	public static ArrayList<Duel> duels = new ArrayList<Duel>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Player> cannotMove = new ArrayList<Player>();
	public static HashMap<Player, Kit> kits = new HashMap<Player, Kit>();
	
}

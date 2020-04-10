package games.game.elytrabattle;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class LegacyPVP {

	public static void enableLegacyPvp(Player p) {
		
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16D);
		
	}
	
}

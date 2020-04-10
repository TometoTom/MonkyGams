package games.meta.statistics;

import org.bukkit.Material;

import games.meta.GameType;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.Utils;

public class DefaultStatistics extends GameStatistics {

	public DefaultStatistics(GameType gt, long timePlayed, int gamesPlayed, int wins) {
		super(timePlayed, gamesPlayed, wins);
	}

	@Override
	public MonkyItemStack[] getInventory() {
		
		MonkyItemStack[] inv = new MonkyItemStack[6 * 9];
		inv[20] = getPaper(ChatColor.RED + "" + ChatColor.BOLD + "Wins", ChatColor.GRAY + "" + getWins() + " win" + Utils.plural(getWins()));
		inv[22] = getPaper(ChatColor.YELLOW + "" + ChatColor.BOLD + "Games Played", ChatColor.GRAY + "" + getGamesPlayed() + " game" + Utils.plural(getGamesPlayed()));
		inv[24] = getPaper(ChatColor.GREEN + "" + ChatColor.BOLD + "Time Played", ChatColor.GRAY + "" + getTimePlayedStr());

		int count = 36;
		while (count != 54) {
			inv[count] = new MonkyItemStack(Material.WHITE_STAINED_GLASS_PANE).setName(" ");
			count++;
		}
		
		return inv;
	}
	
	public DefaultStatistics() {
		
	}

}

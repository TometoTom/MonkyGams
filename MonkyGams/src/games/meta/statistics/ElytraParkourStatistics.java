package games.meta.statistics;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;

import games.meta.GameType;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.Utils;

public class ElytraParkourStatistics extends GameStatistics {
	
	public int deaths = 0;
	public HashMap<String, Long> personalBests = new HashMap<>();
	
	public ElytraParkourStatistics(GameType gt, long timePlayed, int gamesPlayed, int wins, int deaths, HashMap<String, Long> personalBests) {
		super(timePlayed, gamesPlayed, wins);
		this.deaths = deaths;
		this.personalBests = personalBests;
	}

	@Override
	public MonkyItemStack[] getInventory() {
		
		MonkyItemStack[] inv = new MonkyItemStack[6 * 9];
		inv[19] = getPaper(ChatColor.RED + "" + ChatColor.BOLD + "Wins", ChatColor.GRAY + "" + getWins() + " win" + Utils.plural(getWins()));
		inv[21] = getPaper(ChatColor.YELLOW + "" + ChatColor.BOLD + "Games Played", ChatColor.GRAY + "" + getGamesPlayed() + " game" + Utils.plural(getGamesPlayed()));
		inv[23] = getPaper(ChatColor.GREEN + "" + ChatColor.BOLD + "Time Played", ChatColor.GRAY + "" + getTimePlayedStr());
		inv[25] = getPaper(ChatColor.AQUA + "" + ChatColor.BOLD + "Deaths", ChatColor.GRAY + "" + deaths + " death" + Utils.plural(deaths),
				ChatColor.GRAY + "Avg. " + (deaths / (getGamesPlayed() == 0 ? 1 : getGamesPlayed())) + " per game");

		int count = 36;
		for (Entry<String, Long> entry : personalBests.entrySet()) {
			inv[count] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + entry.getKey(), ChatColor.GRAY + Utils.getMinsSeconds(entry.getValue()));
			count++;
		}
		while (count != 54) {
			inv[count] = new MonkyItemStack(Material.WHITE_STAINED_GLASS_PANE).setName(" ");
			count++;
		}
		
		return inv;
	}
	
	public ElytraParkourStatistics() {
		
	}

}

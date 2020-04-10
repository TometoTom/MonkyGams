package games.meta.statistics;

import org.bukkit.Material;

import games.meta.GameType;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.Utils;

public class ElytraBattleStatistics extends GameStatistics {

	public int deaths = 0; //
	public int kills = 0; //
	public int chestsOpened = 0;
	public int tntThrown = 0;
	public int cobwebsThrown = 0;
	public int fireballsThrown;
	public int ropesUsed = 0;
	public int coalUsed = 0;
	public int diamondsMined = 0;
	public int chestsXrayed = 0;
	public int diamondsXrayed = 0;
	public int soupUsed = 0;
	
	public ElytraBattleStatistics(GameType gt, long timePlayed, int gamesPlayed, int wins, int deaths, int kills, int chestsOpened,
			int tntThrown, int cobwebsThrown, int fireballsThrown, int ropesUsed, int coalUsed, int diamondsMined,
			int chestsXrayed, int diamondsXrayed, int soupUsed) {
		super(timePlayed, gamesPlayed, wins);
		this.deaths = deaths;
		this.kills = kills;
		this.chestsOpened = chestsOpened;
		this.tntThrown = tntThrown;
		this.cobwebsThrown = cobwebsThrown;
		this.fireballsThrown = fireballsThrown;
		this.ropesUsed = ropesUsed;
		this.coalUsed = coalUsed;
		this.diamondsMined = diamondsMined;
		this.chestsXrayed = chestsXrayed;
		this.diamondsXrayed = diamondsXrayed;
		this.soupUsed = soupUsed;
	}

	@Override
	public MonkyItemStack[] getInventory() {
		MonkyItemStack[] inv = new MonkyItemStack[6 * 9];
		inv[18] = getPaper(ChatColor.RED + "" + ChatColor.BOLD + "Wins", ChatColor.GRAY + "" + getWins() + " win" + Utils.plural(getWins()));
		inv[20] = getPaper(ChatColor.YELLOW + "" + ChatColor.BOLD + "Games Played", ChatColor.GRAY + "" + getGamesPlayed() + " game" + Utils.plural(getGamesPlayed()));
		inv[22] = getPaper(ChatColor.GREEN + "" + ChatColor.BOLD + "Time Played", ChatColor.GRAY + "" + getTimePlayedStr());
		inv[24] = getPaper(ChatColor.AQUA + "" + ChatColor.BOLD + "Deaths", ChatColor.GRAY + "" + deaths + " death" + Utils.plural(deaths),
				ChatColor.GRAY + "Avg. " + (deaths / (getGamesPlayed() == 0 ? 1 : getGamesPlayed())) + " per game");
		inv[26] = getPaper(ChatColor.BLUE + "" + ChatColor.BOLD + "Kills", ChatColor.GRAY + "" + kills + " kill" + Utils.plural(kills),
				ChatColor.GRAY + "Avg. " + (kills / (getGamesPlayed() == 0 ? 1 : getGamesPlayed())) + " per game");

		inv[36] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Chests Opened", ChatColor.GRAY + "" + chestsOpened + " chest" + Utils.plural(chestsOpened));
		inv[37] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "TNT Thrown", ChatColor.GRAY + "" + tntThrown + " TNT");
		inv[38] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Cobwebs Thrown", ChatColor.GRAY + "" + cobwebsThrown + " cobweb" + Utils.plural(cobwebsThrown));
		inv[39] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Fireballs Thrown", ChatColor.GRAY + "" + fireballsThrown + " fireball" + Utils.plural(fireballsThrown));
		inv[40] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Ropes Used", ChatColor.GRAY + "" + ropesUsed + " rope" + Utils.plural(ropesUsed));
		inv[41] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Coal Used", ChatColor.GRAY + "" + coalUsed + " coal");
		inv[42] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Diamonds Mined", ChatColor.GRAY + "" + diamondsMined + " diamond" + Utils.plural(diamondsMined));
		inv[43] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Chests Xrayed", ChatColor.GRAY + "" + chestsXrayed + " chest" + Utils.plural(chestsXrayed));
		inv[44] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Diamonds Xrayed", ChatColor.GRAY + "" + diamondsXrayed + " diamond" + Utils.plural(diamondsXrayed));
		inv[45] = getPaper(ChatColor.GOLD + "" + ChatColor.BOLD + "Instant Soup Used", ChatColor.GRAY + "" + soupUsed + " soup");

		for (int count = 46; count != 54; count++) {
			inv[count] = new MonkyItemStack(Material.WHITE_STAINED_GLASS_PANE).setName(" ");
		}
		
		return inv;
	}
	
	public ElytraBattleStatistics() {
		
	}

}

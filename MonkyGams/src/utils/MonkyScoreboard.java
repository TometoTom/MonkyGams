package utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class MonkyScoreboard {

	private Scoreboard scoreboard;
	private Objective objective;
	private ArrayList<Team> lines = new ArrayList<>();
	
	public MonkyScoreboard(String title) {
		
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		objective = scoreboard.registerNewObjective("title", "dummy", title);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
	}
	
	public void addLine(String line) {

		if (lines.size() > 15) return;
		
		int index = lines.size();
		setLine(index, line);
		
	}
	
	/*
	 * Submit an empty string if you want to add an empty line.
	 */
	public void setLine(int index, String line) {
		
		if (line.length() > 64) return;
		if (index > lines.size()) return;
		
		Team currentLine = null;
		try {
			currentLine = lines.get(index);
		} catch (Exception e) { }
		
		/* Append the line if it doesn't exist */
		if (currentLine == null) {
			String code = ChatColor.values()[index].toString();
			Team t = scoreboard.registerNewTeam(code);
			t.addEntry(code);
			objective.getScore(code).setScore(15 - index);
			lines.add(t);
			
			currentLine = t;
		}
		
		/* Edit an already existing line */
		if (line.length() > 32) {
			currentLine.setPrefix(line.substring(0, 32));
			currentLine.setSuffix(line.substring(32));
		}
		else {
			currentLine.setPrefix(line.equalsIgnoreCase("") ? ChatColor.values()[index].toString() : line);
			currentLine.setSuffix("");
		}

		
	}
	
	/*
	 * Causes flickering.
	 */
	@Deprecated
	public void clear() {
		
		int count = 0;
		for (Team t : lines) {
			
			String code = ChatColor.values()[count].toString();
			scoreboard.resetScores(code);
			t.removeEntry(code);
			t.unregister();
			
			count++;
		}
		
		lines.clear();
		
	}
	
	public void showScoreboard(Player p) {
		p.setScoreboard(scoreboard);
	}
	public void showScoreboardToAll() {
		Bukkit.getOnlinePlayers().forEach(this::showScoreboard);
	}
	public void unshowScoreboard(Player p) {
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	public void unshowScoreboardForAll() {
		Bukkit.getOnlinePlayers().forEach(this::unshowScoreboard);
	}
	
}

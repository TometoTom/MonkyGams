package games.game.elytrabattle;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import net.md_5.bungee.api.ChatColor;

public class Reminders {

	private ArrayList<String> reminders = new ArrayList<>();
	
	public Reminders() {
		reminders.add("Axes do less damage than swords.");
		reminders.add("You can use coal to recharge your Elytra.");
		reminders.add("Axes do less damage than swords.");
		reminders.add("You can find chests around the map with valuable items, like soup and TNT.");
		reminders.add("You can kill players during the preparation time, but they won't lose their items.");
		reminders.add("Bats drop coal!");
		reminders.add("Killing rabbits is a sin.");
		reminders.add("Lapis Lazuli ore drops lapis blocks. Lapis blocks are one-use water buckets!");
		reminders.add("Redstone ore drops redstone blocks. Redstone blocks are one-use lava buckets!");
		reminders.add("This game uses 1.8 PvP mechanics.");
		reminders.add("You can find Ropes in chests, which allow you to teleport to the surface.");
		reminders.add("You can find Throwable TNT, Fireballs, and Cobwebs in chests which can be used against players!");
		reminders.add("You can find diamonds in chests if you don't manage to find them underground.");
		
	}
	
	public void sayRandomReminder() {
		
		String reminder = reminders.remove(new Random().nextInt(reminders.size()));
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Reminder! " + ChatColor.RESET + "" + ChatColor.GRAY + reminder);
		Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1));

	}
	
}

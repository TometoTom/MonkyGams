package games.game.kitpvp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class KitCommand extends MonkyCommand {

	String s = ChatColor.GREEN + "" + ChatColor.BOLD + "KIT: " + ChatColor.RESET + "" + ChatColor.GRAY;

	@Override
	public void onCommand(Player p, Args args) {
		
		if (DuelData.players.contains(p)){
			p.sendMessage(s + "You cannot change your kit whilst in a duel.");
			return;
		}
		
		new KitInventory(p);
		
	}

	@Override
	public String getName() {
		return "kit";
	}

	@Override
	public String getDescription() {
		return "Change your kit!";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.NONE;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.GAME;
	}
	
}

package games.game.kitpvp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class DuelCommand extends MonkyCommand {

	String s = ChatColor.GOLD + "" + ChatColor.BOLD + "DUEL: " + ChatColor.RESET + "" + ChatColor.GRAY;
	public static Duel dl = null;

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			p.sendMessage(s + "Usage: /duel [player]");
			return;
		}
		
		Player t = getPlayer(args.get(0));
		if (t==null){
			p.sendMessage(s + args.get(0) + " is not online.");
			return;
		}
		
		new MapInventory(p);
		Duel d = new Duel(p, t, DuelMap.ARENA);
		dl = d;
		
	}

	@Override
	public String getName() {
		return "duel";
	}

	@Override
	public String getDescription() {
		return "Duel someone in Kit PvP!";
	}

	@Override
	public String getUsage() {
		return "[player]";
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

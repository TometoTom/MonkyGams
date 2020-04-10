package command.commands.other;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import core.data.RankManager;

public class RankCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			
			PermissionLevel lvl = RankManager.getRankManager().getRank(p);
			s(p, lvl == PermissionLevel.NONE ? "You don't have a rank." : "You are rank " + lvl.getDisplayName() + ".");
			return;
		}
		
		Player p2 = getPlayer(args.get(0));
		
		if (p2 == null) {
			
			f(p, "You must enter a player name.");
			return;
			
		}
		
		if (args.size() == 1) {
			
			PermissionLevel lvl = RankManager.getRankManager().getRank(p2);
			s(p, lvl == PermissionLevel.NONE ? p2.getName() + " doesn't have a rank." : p2.getName() + " is rank " + lvl.getDisplayName() + ".");
			return;
			
		}
		
		if (args.size() == 2) {
			
			String arg2 = args.get(1);
			
			try {
				PermissionLevel lvl = PermissionLevel.valueOf(arg2.toUpperCase());
				RankManager.getRankManager().setRank(p2, lvl);
				s(p, "Set " + p2.getName() + "'s rank to " + lvl.getDisplayName());
				return;
			} catch (Exception e) {
				f(p, "The rank '" + arg2 + "' does not exist.");
				return;
			}
			
		}
		
		else {
			
			f(p, "Usage: /rank [player] [new rank]");
			return;
		}
		
	}

	@Override
	public String getName() {
		return "rank";
	}

	@Override
	public String getDescription() {
		return "View and set ranks";
	}

	@Override
	public String getUsage() {
		return "[player] (new rank)";
	}

	@Override
	public String[] getAliases() {
		return new String[]{};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.OWNER;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}
	
}

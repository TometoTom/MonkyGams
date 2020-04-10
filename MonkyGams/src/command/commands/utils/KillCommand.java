package command.commands.utils;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class KillCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			p.damage(10000);
			return;
		}

		Player p2 = getPlayer(args.get(0));
		if (p2 == null) {
			f(p, "You must enter a valid player name.");
			return;
		}
		
		p2.damage(10000);
		return;
		
	}

	@Override
	public String getName() {
		return "kill";
	}

	@Override
	public String getDescription() {
		return "Kills others";
	}

	@Override
	public String getUsage() {
		return "(player)";
	}

	@Override
	public String[] getAliases() {
		return new String[] {};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.ADMIN;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}
	
}

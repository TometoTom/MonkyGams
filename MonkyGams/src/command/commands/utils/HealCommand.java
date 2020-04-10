package command.commands.utils;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class HealCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			s(p, "You've been healed.");
			p.setHealth(20);
			return;
		}

		Player p2 = getPlayer(args.get(0));
		if (p2 == null) {
			f(p, "You must enter a valid player name.");
			return;
		}
		
		s(p2, "You've been healed.");
		p2.setHealth(20);
		return;
		
	}

	@Override
	public String getName() {
		return "heal";
	}

	@Override
	public String getDescription() {
		return "Heals others";
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

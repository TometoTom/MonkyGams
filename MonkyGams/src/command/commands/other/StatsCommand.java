package command.commands.other;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import core.events.StatsGUIEvents;

public class StatsCommand extends MonkyCommand implements Listener {
	
	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			p.openInventory(StatsGUIEvents.getInventory(p));
			return;
		}
		
		Player p2 = getPlayer(args.get(0));
		
		if (p2 == null) {
			f(p, "That player is not online.");
			return;
		}
		
		p.openInventory(StatsGUIEvents.getInventory(p2));
		
	}

	@Override
	public String getName() {
		return "stats";
	}

	@Override
	public String getDescription() {
		return "Obtain your statistics!";
	}

	@Override
	public String getUsage() {
		return "player";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"statistics"};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.NONE;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}
	
}

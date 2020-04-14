package command.commands.hub;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import hub.gui.KitsGUI;

public class KitsCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		new KitsGUI(p).present();
		
	}

	@Override
	public String getName() {
		return "kits";
	}

	@Override
	public String getDescription() {
		return "View the kit shop";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"kitshop"};
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

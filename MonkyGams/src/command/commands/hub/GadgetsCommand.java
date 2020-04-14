package command.commands.hub;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import hub.gui.GadgetsGUI;

public class GadgetsCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		new GadgetsGUI(p).present();
		
	}

	@Override
	public String getName() {
		return "gadgets";
	}

	@Override
	public String getDescription() {
		return "View, equip, and purchase gadgets!";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"gadgetsshop"};
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

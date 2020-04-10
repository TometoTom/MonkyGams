package command.commands.other;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class VoidWorldCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		
		
	}

	@Override
	public String getName() {
		return "voidworld";
	}

	@Override
	public String getDescription() {
		return "Create a void world";
	}

	@Override
	public String getUsage() {
		return "name";
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

package command.commands.other;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import core.Main;

public class LobbyCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		s(p, "Sent you to the Lobby.");
		
		Main.sendPlayerToServer(p, "Lobby");
		
	}

	@Override
	public String getName() {
		return "lobby";
	}

	@Override
	public String getDescription() {
		return "Sends you to the lobby";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"l", "hub", "7hub", "leave", "quit"};
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

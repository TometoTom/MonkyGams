package command.commands.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class GamemodeCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			
			p.setGameMode(p.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL);
			s(p, "Switched your gamemode.");
			return;
			
		}
		
		GameMode searchedGm = getGamemode(args.get(0));
		if (searchedGm != null) {
			p.setGameMode(searchedGm);
			s(p, "Set your gamemode to " + searchedGm.toString() + ".");
			return;
		}
		
		Player p2 = getPlayer(args.get(0));
		
		if (p2 == null) {
			f(p, "You must enter a valid gamemode or player.");
			return;
		}
		
		if (args.size() == 1) {
			f(p, "You must enter a gamemode.");
			return;
		}
		
		searchedGm = getGamemode(args.get(1));
		if (searchedGm != null) {
			p2.setGameMode(searchedGm);
			s(p2, "Your gamemode has been set to " + searchedGm.toString() + ".");
			s(p, "Set " + p2.getName() + "'s gamemode to " + searchedGm);
			return;
		}
		
		f(p, "You must enter a valid gamemode.");
		return;
		
	}

	@Override
	public String getName() {
		return "gamemode";
	}

	@Override
	public String getDescription() {
		return "Changes your gamemode";
	}

	@Override
	public String getUsage() {
		return "(player) (gamemode)";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"gm"};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.ADMIN;
	}

	public GameMode getGamemode(String search) {
		GameMode gm = null;
		switch(search.toLowerCase()) {
		case "survival":
		case "0":
		case "s":
			gm = GameMode.SURVIVAL;
			break;
			
		case "creative":
		case "1":
		case "c":
			gm = GameMode.CREATIVE;
			break;
			
		case "adventure":
		case "2":
		case "a":
			gm = GameMode.ADVENTURE;
			break;
		
		case "spectator":
		case "3":
		case "sp":
		case "spec":
			gm = GameMode.SPECTATOR;
			break;
		}
		return gm;
	}
	
	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}
	
}

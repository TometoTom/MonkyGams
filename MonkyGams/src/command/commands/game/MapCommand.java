package command.commands.game;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import games.meta.GameType;
import games.meta.Map;
import utils.SerialisableLocation;

public class MapCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {

		if (!args.isEmpty() && args.get(0).equalsIgnoreCase("list")) {
			
			if (Map.maps.isEmpty()) {
				s(p, "There are no registered maps.");
				return;
			}
			
			s(p, "Map list");
			s(p, "Map name - World name - Game");
			for (Map m : Map.maps) {
				s(p, m.mapName + " - " + m.spawnPoint.worldName + " - " + m.game.getName());
			}
			
			return;
			
		}
		
		if (args.size() < 3) {
			f(p, "/map create [gameID] [name]");
			f(p, "/map delete [gameID] [name]");
			f(p, "/map setspawn [gameID] [name]");
			f(p, "/map list");
			return;
		}

		String arg = args.remove(0);

		String gameID = args.remove(0);
		GameType game = null;
		try {
			game = GameType.valueOf(gameID.toUpperCase());
		} catch (Exception e) {
			// Nothing
		}
		if (game == null) {
			f(p, "You must enter a valid game ID.");
			return;
		}

		String name = args.getText();

		if (arg.equalsIgnoreCase("create")) {

			if (Map.getMap(name, game) != null) {
				f(p, "A map with this name already exists.");
				return;
			}

			try {
				Map.maps.add(new Map(name, "MonkyGams", new SerialisableLocation(p.getLocation()), game, null));
				s(p, "Added map '" + name + "' for " + game.getName() + ".");
			} catch (Exception e) {
				f(p, "Failed to create map.");
				e.printStackTrace();
			}
			return;

		}

		if (arg.equalsIgnoreCase("delete")) {

			if (Map.maps.remove(Map.getMap(name, game))) {
				s(p, "Deleted map '" + name + "'.");
				return;
			}
			else {
				f(p, "Could not identify that map.");
				return;
			}

		}

		if (arg.equalsIgnoreCase("setspawn")) {

			Map m = Map.getMap(name, game);
			if (m == null) {
				f(p, "Could not identify that map.");
				return;
			}

			m.spawnPoint = new SerialisableLocation(p.getLocation());
			s(p, "Set the spawnpoint to " + p.getLocation().getX() + ", " + p.getLocation().getY() + ", " + p.getLocation().getZ() + ".");

		}
		
		else {
			
			f(p, "/map create [gameID] [name]");
			f(p, "/map delete [gameID] [name]");
			f(p, "/map setspawn [gameID] [name]");
			f(p, "/map list");
			return;
			
		}

	}

	@Override
	public String getName() {
		return "map";
	}

	@Override
	public String getDescription() {
		return "Set up maps for use on games";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.OWNER;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.GAME;
	}
	
}

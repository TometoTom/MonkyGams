package command.commands.game;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import games.game.lobby.LobbyGame;
import games.meta.GameController;
import games.meta.GameType;
import games.meta.Map;

public class GameCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			
			f(p, "/game start [gameID] [map]");
			f(p, "/game end");
			return;
			
		}
		
		String arg = args.remove(0);
		
		if (arg.equalsIgnoreCase("start")) {
			
			if (args.isEmpty() && GameController.getCurrentGameType() == GameType.LOBBY) {
				((LobbyGame) GameController.getCurrentGame()).setCountdownRemaining(0);
				b(getStarterSuccess() + p.getName() + " started the game.");
				return;
			}
			
			if (args.size() < 2) {
				
				f(p, "/game start [game] [map]");
				return;
				
			}
			
			GameType g = null;
			try{
				g = GameType.valueOf(args.remove(0).toUpperCase());
			} catch (Exception e) {
				// Nothing
			}
			Map m = Map.getMap(args.getText(), g);
			
			if (g == null || m == null) {
				
				f(p, "You must enter a correct game ID and map.");
				return;
				
			}
			
			boolean success = false;
			try {
				success = GameController.startNewGame(g, m);
			} catch (Exception e) {
				e.printStackTrace();
				f(p, "Could not start " + g.getName() + ", because the map is incompatible.");
				return;
			}
			
			if (success) b(getStarterSuccess() + g.getName() + " started on map '" + m.mapName + "'.");
			else f(p, "Could not start " + g.getName() + ".");
			return;
			
		}
		
		if (arg.equalsIgnoreCase("end")) {
			
			b(GREEN + "" + BOLD + "GAME: " + RESET + "" + GREY + "Game forcefully ended.");
			GameController.endCurrentGame();
			
		}
		
		else {
			f(p, "/game start [game] [map]");
			f(p, "/game end");
			return;
		}
		
	}

	@Override
	public String getName() {
		return "game";
	}

	@Override
	public String getDescription() {
		return "Starts and stops games.";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"g"};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.ADMIN;
	}
	
	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.GAME;
	}

}

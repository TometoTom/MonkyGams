package command.commands.game;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import games.game.elytrabattle.ElytraBattleGame;
import games.game.elytrabattle.Kit;
import games.meta.GameController;

public class KitCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			f(p, "You must enter a kit name.");
			return;
		}
		
		ElytraBattleGame g = GameController.getCurrentGame(ElytraBattleGame.class);
		
		if (g == null) {
			f(p, "There is no elytra battle game going on right now.");
			return;
		}
		
		for (Kit k : Kit.values()) {
			if (k.getName().equalsIgnoreCase(args.getText())) {
				s(p, "You picked kit " + k.getName() + ".");
				g.kits.put(p.getName(), k);
				return;
			}
		}
		
		f(p, "Couldn't find that kit.");
		return;
		
	}

	@Override
	public String getName() {
		return "kit";
	}

	@Override
	public String getDescription() {
		return "Choose your kit";
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
		return PermissionLevel.NONE;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.GAME;
	}

	
	
}

package command.commands.game;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import games.game.lobby.LobbyGame;
import games.meta.GameController;
import games.meta.GameType;

public class CountdownCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {

		if (args.isEmpty()) {

			f(p, "/countdown start");
			f(p, "/countdown stop");
			f(p, "/countdown set [seconds]");
			return;
		}

		String arg = args.get(0);

		if (arg.equalsIgnoreCase("start")) {

			GameController.startNewGame(GameType.LOBBY, null);
			b(getStarterSuccess() + "The lobby countdown has been started.");
			Bukkit.getOnlinePlayers().forEach(player -> player.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1));
			return;

		}

		if (!(GameController.getCurrentGame() instanceof LobbyGame)) {
			f(p, "You must be in the lobby to use these commands.");
			return;
		}

		LobbyGame g = (LobbyGame) GameController.getCurrentGame();

		if (arg.equalsIgnoreCase("stop")) {

			b(getStarterSuccess() + "The lobby countdown has been stopped.");
			Bukkit.getOnlinePlayers().forEach(player -> player.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1));
			GameController.endCurrentGame();
			return;

		}

		if (arg.equalsIgnoreCase("set")) {

			if (args.size() == 1) {
				f(p, "You must enter a time in seconds.");
				return;
			}

			String arg2 = args.get(1);

			try {
				int secs = Integer.parseInt(arg2);

				if (secs < 1) {
					f(p, "You must enter a positive number of seconds.");
					return;
				}

				g.setCountdownRemaining(secs);

				b(getStarterSuccess() + "The time on the lobby countdown has been set to " + secs + " seconds.");
				Bukkit.getOnlinePlayers().forEach(player -> player.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1));
				return;

			} catch (Exception e) {
				f(p, "You must enter a time in seconds.");
				return;
			}

		}

		try {

			int secs = Integer.valueOf(arg);
			if (secs < 1) {
				f(p, "You must enter a positive number of seconds.");
				return;
			}
			g.setCountdownRemaining(secs);
			b(getStarterSuccess() + "The time on the lobby countdown has been set to " + secs + " seconds.");
			Bukkit.getOnlinePlayers().forEach(player -> player.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 10, 1));
			return;

		} catch (Exception e) {
			// 
		}

		f(p, "/countdown start");
		f(p, "/countdown stop");
		f(p, "/countdown set [seconds]");
		return;

	}

	@Override
	public String getName() {
		return "countdown";
	}

	@Override
	public String getDescription() {
		return "Manages Lobby Countdown";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"cd"};
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

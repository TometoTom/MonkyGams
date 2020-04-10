package command.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class TeleportCommand extends MonkyCommand {

	Sound SOUND = Sound.AMBIENT_UNDERWATER_ENTER;
	
	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			f(p, "You must enter a valid player name, world name, or set of coordinates.");
			return;
		}
		
		if (args.size() == 1) {
			
			Player p2 = getPlayer(args.get(0));
			
			if (p2 != null) {
				p.teleport(p2);
				p.playSound(p.getLocation(), SOUND, 10, 2);
				s(p, "You teleported to " + p2.getName() + ".");
				return;
			}
			
			World w = Bukkit.getWorld(args.get(0));
			
			if (w != null) {
				p.teleport(w.getSpawnLocation());
				p.playSound(p.getLocation(), SOUND, 10, 2);
				s(p, "You teleported to " + w.getName() + ".");
				return;
			}
		
			f(p, "You must enter a valid player name, world name, or set of coordinates.");
			return;
		}
		
		if (args.size() == 3) {
			int x = 0, y = 0, z = 0;
			try {
				x = Integer.parseInt(args.get(0));
				y = Integer.parseInt(args.get(1));
				z = Integer.parseInt(args.get(2));
			} catch (Exception e) {
				f(p, "You must enter valid coordinates.");
				return;
			}
			p.teleport(new Location(p.getWorld(), x, y, z));
			p.playSound(p.getLocation(), SOUND, 10, 2);
			s(p, "You have teleported to " + x + ", " + y + ", " + z + ".");
			return;
		}
		
		Player p2 = getPlayer(args.remove(1));
		
		if (p2 == null) {
			f(p, "You must enter a valid player name.");
			return;
		}
		
		if (args.isEmpty()) {
			f(p, "You must enter a place to teleport them to.");
			return;
		}
		
		if (args.size() == 1) {
			
			Player p3 = getPlayer(args.get(0));
			
			if (p3 != null) {
				p3.teleport(p2);
				p2.playSound(p.getLocation(), SOUND, 10, 2);
				s(p, "You teleported " + p2.getName() + " to " + (p3.getName().equalsIgnoreCase(p.getName()) ? "you" : p3.getName()) + ".");
				s(p2, "You have been teleported to " + p3.getName());
				return;
			}
			
			World w = Bukkit.getWorld(args.get(0));
			
			if (w != null) {
				p2.teleport(w.getSpawnLocation());
				p2.playSound(p.getLocation(), SOUND, 10, 2);
				s(p, "You teleported " + p2.getName() + " to " + w.getName() + ".");
				s(p2, "You have been teleported to " + w.getName() + ".");
				return;
			}
		
			f(p, "You must enter a valid player name, world name, or set of coordinates.");
			return;
		}
		
		if (args.size() == 3) {
			int x = 0, y = 0, z = 0;
			try {
				x = Integer.parseInt(args.get(0));
				y = Integer.parseInt(args.get(1));
				z = Integer.parseInt(args.get(2));
			} catch (Exception e) {
				f(p, "You must enter valid coordinates.");
				return;
			}
			p2.teleport(new Location(p.getWorld(), x, y, z));
			p2.playSound(p.getLocation(), SOUND, 10, 2);
			s(p, "You have teleported " + p2.getName() + " to " + x + ", " + y + ", " + z + ".");
			s(p2, "You have been teleported to " + x + ", " + y + ", " + z + ".");
			return;
		}
	
		
	}

	@Override
	public String getName() {
		return "teleport";
	}

	@Override
	public String getDescription() {
		return "Teleports to places and people.";
	}

	@Override
	public String getUsage() {
		return "[player/world/coords]/[player]";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"tp"};
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

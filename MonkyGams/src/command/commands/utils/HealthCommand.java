package command.commands.utils;

import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class HealthCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			s(p, "Your health is " + p.getHealth() + ".");
			return;
		}

		Player specifiedPlayer = null;
		String specifiedHP = null;
		
		Player p2 = getPlayer(args.get(0));
		if (p2 == null) {
			specifiedPlayer = p;
			specifiedHP = args.get(0);
		}
		else {
			if (args.size() == 1) {
				s(p, p2.getName() + "'s health is " + p2.getHealth() + ".");
				return;
			}
			else {
				specifiedPlayer = p2;
				specifiedHP = args.get(1);
			}
		}
		
		Double hp = null;
		try { 
			hp = Double.parseDouble(specifiedHP);
		} catch (Exception e) {
			f(p, "You must enter a valid HP.");
			return;
		}
		
		specifiedPlayer.setHealth(hp);
		
		if (specifiedPlayer.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
			s(p, "Set your health to " + hp + ".");
		}
		else {
			s(p, "Set " + specifiedPlayer + "'s health to " + hp + ".");
			s(specifiedPlayer, "Your health has been set to " + hp + ".");
		}
		
		return;
		
	}

	@Override
	public String getName() {
		return "health";
	}

	@Override
	public String getDescription() {
		return "Sets health.";
	}

	@Override
	public String getUsage() {
		return "(player) [HP]";
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

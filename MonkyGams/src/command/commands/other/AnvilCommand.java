package command.commands.other;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;

public class AnvilCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		for (Block b : p.getLineOfSight(null, 50)) {
			b.setType(Material.ANVIL);
		}
		return;
		
	}

	@Override
	public String getName() {
		return "anvil";
	}

	@Override
	public String getDescription() {
		return "Spams anvils";
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
		return PermissionLevel.ADMIN;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}

}

package command.commands.hub;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import hub.gadgets.meta.GadgetType;
import utils.MonkyItemStack;

public class GadgetCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		if (args.isEmpty()) {
			f(p, "You must enter a gadget name to equip.");
			return;
		}
		
		String name = args.getText();
		for (GadgetType gt : GadgetType.values()) {
			if (gt.getName().equalsIgnoreCase(name)) {
				p.getInventory().addItem(new MonkyItemStack(gt.getIconInHotbar())
						.setName(GOLD + "" + BOLD + gt.getName())
						.setLore(GREY + gt.getDescription(), "", GREY + "" + BOLD + "Cooldown: " + gt.getCooldown() + " seconds"));
				p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10, 1);
				return;
			}
		}
		
		f(p, "That gadget doesn't exist.");
		return;
	}

	@Override
	public String getName() {
		return "gadget";
	}

	@Override
	public String getDescription() {
		return "Obtains a gadget for testing";
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
		return PermissionLevel.BUILDER;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}

}

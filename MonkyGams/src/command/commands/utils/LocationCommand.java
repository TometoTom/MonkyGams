package command.commands.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LocationCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {

		Location l = p.getLocation();
		
		TextComponent tcBefore = new TextComponent(getStarterSuccess() + "Your location is ");
		TextComponent tc = new TextComponent(BOLD + "" + (int) l.getX() + ", " + (int) l.getY() + ", " + (int) l.getZ());
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GOLD + "" + BOLD + "Click to copy").create()));
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, 
				"new Location(Bukkit.getWorld(\"" + l.getWorld().getName() + "\"), " + l.getX() + ", " + l.getY() + ", " + l.getZ() + ", " + l.getYaw() + "f, " + l.getPitch() + "f);"));
		TextComponent tcAfter = new TextComponent(GREY + ".");
		
		p.spigot().sendMessage(tcBefore, tc, tcAfter);

	}

	@Override
	public String getName() {
		return "location";
	}

	@Override
	public String getDescription() {
		return "Allows you to copy and paste location data";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"loc"};
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

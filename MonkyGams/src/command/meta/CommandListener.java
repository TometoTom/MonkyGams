package command.meta;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import core.data.RankManager;
import net.md_5.bungee.api.ChatColor;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		MonkyCommand c = MonkyCommand.getCommand(label);
		
		if (c == null) {
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("The console cannot execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if ((!RankManager.getRankManager().hasRank(p, c.getPermissionLevel())) &! p.getName().equalsIgnoreCase("Dimb")) {
		
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + c.getName().toUpperCase() + ": " + ChatColor.RESET + "" + ChatColor.GRAY +
					"This command requires " + c.getPermissionLevel().getChatName() + ChatColor.RESET + "" + ChatColor.GRAY + " rank.");
			return true;
			
		}
		
		c.onCommand(p, new Args(args));
		return true;
		
	}
	
}

package command.commands.other;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import games.game.elytrabattle.ElytraBattleGame;
import games.meta.GameController;

public class TestCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		
		Block b = p.getLocation().subtract(0, 1, 0).getBlock();
		b.setType(Material.CHEST);
		Chest c = (Chest) b.getState();
		
		for (int i = 0; i <= new Random().nextInt(4) + 1; i++) {
			c.getBlockInventory().setItem(new Random().nextInt(27), ((ElytraBattleGame) GameController.getCurrentGame()).chests.getRandomItem());
		}
		
		
//		s(p, "You've walked " + PlayerStatisticsManager.getStatistics(p).getTotalBlocksWalked() + " blocks in total.");
//		
//		s(p, "The ID is " + Bukkit.getServer().getMotd());
//		
//		Inventory inv = Bukkit.createInventory(null, 6 * 9);
//		
//		for (int i = 0; i != 54; i++) {
//			inv.setItem(i, new MonkyItemStack(Material.BIRCH_BUTTON).setNumber(i));
//		}
//		
//		p.openInventory(inv);
		
	}

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String getDescription() {
		return "Test";
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
		return CommandCompatibility.UNIVERSAL;
	}
	
}

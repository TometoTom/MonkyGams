package command.commands.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import core.events.StatsGUIEvents;
import hub.gui.GadgetsGUI;
import hub.gui.KitsGUI;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.gui.MonkyGUI;

public class ProfileCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {

		Player chosenPlayer = p;

		MonkyGUI gui = new MonkyGUI(p, chosenPlayer.getName() + "'s Profile", 6);
		gui.setClickEvent(ClickType.LEFT, (clicker, item) -> {
			if (item.getType() == Material.PAPER) {
				p.openInventory(StatsGUIEvents.getInventory(clicker));
			}
			if (item.getType() == Material.ENDER_CHEST) {
				new GadgetsGUI(clicker).present();
			}
			if (item.getType() == Material.GOLDEN_SWORD) {
				new KitsGUI(clicker).present();
			}
		});
		gui.getInventory().setItem(8, new MonkyItemStack(Material.BARRIER).setName(RED + "" + BOLD + "Close"));

		@SuppressWarnings("deprecation")
		ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwningPlayer(p);
		skullMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + p.getName() + "'s Profile");
		skull.setItemMeta(skullMeta);
		gui.getInventory().setItem(13, skull);

		gui.getInventory().setItem(28, new MonkyItemStack(Material.GOLDEN_SWORD)
				.setName(GOLD + "" + BOLD + "Kits")
				.setLore(GREY + "Click here to view and purchase kits"));

		gui.getInventory().setItem(31, new MonkyItemStack(Material.ENDER_CHEST)
				.setName(GOLD + "" + BOLD + "Cosmetics & Gadgets")
				.setLore(GREY + "Click here to view, equip, and purchase gadgets"));

		gui.getInventory().setItem(34, new MonkyItemStack(Material.PAPER)
				.setName(GOLD + "" + BOLD + "Statistics")
				.setLore(GREY + "Click here to view statistics"));

		gui.present(p);

	}

	@Override
	public String getName() {
		return "profile";
	}

	@Override
	public String getDescription() {
		return "View your profile";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"p"};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.NONE;
	}

	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}



}

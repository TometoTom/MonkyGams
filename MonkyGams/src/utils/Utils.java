package utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static String bold(String s) {
		return ChatColor.BOLD + s + ChatColor.RESET + "" + ChatColor.GRAY;
	}
	
	public static String plural(int number) {
		return number == 1 ? "" : "s";
	}
	
	public static String getHoursMins(long time) {
		if (time == 0) {
			return "0 hours 0 minutes";
		}

		int mins = (int) (time / 1000 / 60);

		return (mins / 60) + " hours " + (mins % 60) + " minutes";
	}
	
	public static String getMinsSeconds(long time) {
		if (time == 0) {
			return "0.00s";
		}

		if (time < 60000) {
			float f = time / 1000f;
			DecimalFormat df = new DecimalFormat("#.#");
			df.setRoundingMode(RoundingMode.CEILING);
			return df.format(f) + "s";
		}

		else {
			long timeSecs = time / 1000;

			int mins = (int) (timeSecs / 60);
			int secs = (int) (timeSecs % 60);

			return mins + "m " + secs + "s"; 
		}
	}
	
	public static ItemStack getSkull(String displayName, String uuid) {
		@SuppressWarnings("deprecation")
		ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
		skullMeta.setDisplayName(displayName);
		skull.setItemMeta(skullMeta);
		return skull;
	}
	
}

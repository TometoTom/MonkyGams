package command.meta;

import org.bukkit.ChatColor;

public enum PermissionLevel {

	NONE(0, "None", ""),
	BUILDER(1, "Builder", ChatColor.BLUE + "" + ChatColor.BOLD + "BUILDER"),
	ADMIN(1, "Admin", ChatColor.GOLD + "" + ChatColor.BOLD + "ADMIN"),
	OWNER(2, "Owner", ChatColor.GOLD + "" + ChatColor.BOLD + "OWNER");
	
	private int level;
	private String displayName;
	private String chatName;
	
	private PermissionLevel(int level, String displayName, String chatName) {
		this.level = level;
		this.displayName = displayName;
		this.chatName = chatName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public String getChatName() {
		return this.chatName;
	}
	
	public int getLevel() {
		return this.level;
	}
	
}

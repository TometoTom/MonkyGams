package hub.gadgets.meta;

import org.bukkit.Material;

import hub.gadgets.JukeboxGadget;

public enum GadgetType {

	JUKEBOX(JukeboxGadget.class, "Jukebox", "Place one of these to start playing some music!", 60, Material.JUKEBOX, Material.JUKEBOX);

	private Class<? extends Gadget> gadget;
	private String name;
	private String description;
	private int cooldown;
	private Material iconInInventory;
	private Material iconInHotbar;
	
	private GadgetType(Class<? extends Gadget> gadget, String name, String description, int cooldown, Material iconInInventory, Material iconInHotbar) {
		this.gadget = gadget;
		this.name = name;
		this.description = description;
		this.cooldown = cooldown;
		this.iconInInventory = iconInInventory;
		this.iconInHotbar = iconInHotbar;
	}
	
	public Class<? extends Gadget> getGadget() {
		return gadget;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getCooldown() {
		return cooldown;
	}

	public Material getIconInInventory() {
		return iconInInventory;
	}

	public Material getIconInHotbar() {
		return iconInHotbar;
	}
	
}

package hub.gadgets.meta;

import org.bukkit.Material;

import hub.gadgets.JukeboxGadget;
import hub.gadgets.TNTGadget;

public enum GadgetType {

	JUKEBOX(JukeboxGadget.class, "Jukebox", "Place one of these to start playing some music!", 4, 60, Material.JUKEBOX, Material.JUKEBOX),
	TNT(TNTGadget.class, "Highly Explosive TNT", "Throw this to create a massive explosion!", 2, 5, Material.TNT, Material.TNT);

	private Class<? extends Gadget> gadget;
	private String name;
	private String description;
	private int poundsPrice;
	private int cooldown;
	private Material iconInInventory;
	private Material iconInHotbar;
	
	private GadgetType(Class<? extends Gadget> gadget, String name, String description, int poundsPrice, int cooldown, Material iconInInventory, Material iconInHotbar) {
		this.gadget = gadget;
		this.name = name;
		this.poundsPrice = poundsPrice;
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

	public int getPrice() {
		return poundsPrice;
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

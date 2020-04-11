package games.game.kitpvp;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public enum Kit {

	AGILE("Agile", "Rush", 15, "Right click with your sword to get 3x speed.", Material.FEATHER, true),
	SNEAK("Sneak", "Flash", 5, "Right click with your sword to flash to the place you're looking at.", Material.INK_SAC, true),
	EXPLOSIVE("Explosive", "Explosive Arrow", 15, "Left click with your bow to charge Explosive Arrow.", Material.TNT, true),
	ICE("Ice", "Freeze", 10, "Right click with your sword on a player to freeze them for 3 seconds.", Material.PACKED_ICE, true),
	FAT("Fat", "Stomp", 15, "Double jump to stomp the ground and damage players around you.", Material.CAKE, true),
	PUMPKIN("Pumpkin", "Pumpkin Throw", 10, "Use your pumpkin to temporarily blind people.", Material.PUMPKIN, true),
	GYMLAD("Gym Lad", "I'm Hench", 15, "Right click your sword to have no knockback.", Material.BONE, true),
	CLINTON("Hillary Clinton", "Pneumonia", 12, "Right click your sword on someone to give them pneumonia", Material.RED_BED, true),
	CAMERON("David Cameron", "Resign", 3, "Right click your sword to resign and let them get on with Brexit!", Material.DARK_OAK_DOOR, true),
	FARAGE("Nigel Farage", "Shoot the Immigrants", 12, "Hold right click on your sword to shoot immigrants out of your country.", Material.FIRE_CHARGE, true),
	KEEMSTAR("Keemstar", "Popcorn Gun", 12, "Hold right click on your sword to spread the news.", Material.CHAINMAIL_HELMET, true),
	KHAN("Sadiq Khan", "Night Tube", 12, "Right click on someone to travel using the night tube.", Material.TNT_MINECART, true),
	CORBYN("Jeremy Corbyn", "I Will Never Leave", 15, "Right click on your sword to avoid death by Cameron.", Material.BARRIER, true),
	HUNT("Jeremy Hunt", "I'm Selling the NHS", 15, "Right click on someone to get rid of their healthcare.", Material.POTION, true),
	JARVIS("Jarvis", "Network Ban", 1, "Right click on someone to network ban them!", Material.CARROT, true),
	JOHNSON("Boris Johnson", "I'm a big friendly buffoon", 17, "Right click on your sword to bounce around like a big friendly buffoon.", Material.ANVIL, true),
	MAY("Theresa May", "Brexit means Brexit", 12, "Right click your sword to go invisible and pretend Brexit doesn't exist.", Material.MAP, true),
	TRUMP("Donald Trump", "Build a Wall", 15, "Right click your sword to build a wall", Material.COBBLESTONE_WALL, true);
	
	private final String name;
	private final String skillName;
	private final int cooldown;
	private final String description;
	private final Material material;
	private final boolean enabled;
	
	Kit(String name, String skillName, int cooldown, String description, Material material, boolean enabled){
		
		this.name = name;
		this.skillName = skillName;
		this.cooldown = cooldown;
		this.description = description;
		this.material = material;
		this.enabled = enabled;
		
	}
	
	public static HashMap<Player, Kit> kits = new HashMap<Player, Kit>();
	
	public String getName(){
		
		return name;
		
	}
	
	public String getSkillName(){
		
		return ChatColor.GRAY + "" + ChatColor.BOLD + skillName + ChatColor.RESET + "" + ChatColor.GRAY;
		
	}
	
	public int getCooldown(){
		
		return cooldown;
		
	}
	
	public String getDescription(){
		
		return description;
		
	}
	
	public Material getMaterial(){
		
		return material;
		
	}
	
	public boolean isEnabled(){
		
		return enabled;
		
	}
	
}

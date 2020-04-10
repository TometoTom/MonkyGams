package games.game.lobby;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import core.Main;
import net.md_5.bungee.api.ChatColor;
import utils.npc.NPC;

public class DrawingManager {

	public HashMap<String, Material> selectedColours = new HashMap<String, Material>();
	public NPC painter;
	
	private int taskID;
	
	public void setup() {
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isSneaking()) {
                        Block b = p.getTargetBlock(null, 9999);
                        if (b.getType().toString().endsWith("_WOOL")) {
                        	if (p.getInventory().getItemInMainHand().getType() == Material.SPECTRAL_ARROW)
                            if (!selectedColours.containsKey(p.getPlayer().getName())){
                            	b.setType(Material.BLACK_WOOL);
                            }
                            else {
                            	b.setType(selectedColours.get(p.getName()));
                            }
                        }
                    }
                }
            }
        }, 0, 1);
		
		/* Spawn painter NPC */
		Location painterLocation = new Location(LobbyGame.lobbyMap.getWorld(), 10.530017184350285, 84.0, 11.522053580922659, -56.96395f, 11.978119f);
		painter = new NPC(painterLocation, "6dd3d5038ec64bbcaa730ba613cf0407", ChatColor.GOLD + "" + ChatColor.BOLD + "Ross");
		painter.setClickEvent((p) -> {
			Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 10, 1));
			LobbyListeners.dmtArea.forEach((s) -> {
				try {
					Bukkit.getPlayer(s).sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Ross: " + ChatColor.RESET + "" + ChatColor.GRAY + "I've reset the canvas for you.");
				} catch (NullPointerException e) {}
			});
			reset(new Location(LobbyGame.lobbyMap.getWorld(), 9, 96, 23), new Location(LobbyGame.lobbyMap.getWorld(), 29, 83, 23));
		});
		painter.showForAll();
		painter.register();
		
	}
	
	public void stop() {
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
	public void reset(Location first, Location second) {
		
		int x1 = first.getBlockX();
		int y1 = first.getBlockY();
		int z1 = first.getBlockZ();

		int x2 = second.getBlockX();
		int y2 = second.getBlockY();
		int z2 = second.getBlockZ();

		//Then we create the following integers
		int xMin, yMin, zMin;
		int xMax, yMax, zMax;
		int x, y, z;

		//Now we need to make sure xMin is always lower then xMax
		if(x1 > x2){ //If x1 is a higher number then x2
			xMin = x2;
			xMax = x1;
		}else{
			xMin = x1;
			xMax = x2;
		}

		//Same with Y
		if(y1 > y2){
			yMin = y2;
			yMax = y1;
		}else{
			yMin = y1;
			yMax = y2;
		}

		//And Z
		if(z1 > z2){
			zMin = z2;
			zMax = z1;
		} else{
			zMin = z1;
			zMax = z2;
		}


		//Now it's time for the loop
		for(x = xMin; x <= xMax; x ++){
			for(y = yMin; y <= yMax; y ++){
				for(z = zMin; z <= zMax; z ++){
					first.getWorld().getBlockAt(x, y, z).setType(Material.WHITE_WOOL);
				}
			}
		}
		
	}
	
}

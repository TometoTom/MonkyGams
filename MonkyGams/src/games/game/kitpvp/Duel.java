package games.game.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import core.Main;

public class Duel {

	String s = ChatColor.GOLD + "" + ChatColor.BOLD + "DUEL: " + ChatColor.RESET + "" + ChatColor.GRAY;
	
	public Player p1 = null;
	public Player p2 = null;
	public DuelMap map = null;
	public Location locp1 = null;
	public Location locp2 = null;
	public Kit p1kit = Kit.kits.get(p1);
	public Kit p2kit = Kit.kits.get(p2);
	public static int i = 0;
	
	public Duel(Player player1, Player player2, DuelMap chosenMap){ 
		
		p1 = player1;
		p2 = player2;
		map = chosenMap;
		
	}
	
	public void setMap(DuelMap p){
		
		this.map = p;
		
	}
	
	public void startDuel(){
		
		this.p1kit = DuelData.kits.get(p1);
		this.p2kit = DuelData.kits.get(p2);
		
		if (this.map == null){
			p1.sendMessage("Invalid map");
			p2.sendMessage("Invalid map");
			return;
		}
		
		if (this.p1 == null){
			p2.sendMessage("Invalid player1");
			return;
		}
		
		if (this.p2 == null){
			p1.sendMessage("Invalid player2");
			return;
		}
		
		if (this.p1kit == null){
			p1.sendMessage("Invalid p1kit");
			p2.sendMessage("Invalid p2kit");
			return;
		}
		
		if (this.p2kit == null){
			p1.sendMessage("Invalid p2kit");
			p2.sendMessage("Invalid p2kit");
			return;
		}
		
		if (this.map == null){
			p1.sendMessage("Invalid map");
			p2.sendMessage("Invalid map");
			return;
		}
		
		locp1 = p1.getLocation();
		locp2 = p2.getLocation();
		
		DuelData.duels.add(this);
		DuelData.players.add(p1);
		DuelData.players.add(p2);
		
		Kit.kits.remove(p1);
		Kit.kits.remove(p2);

		p1.teleport(map.getSpawn1());
		p2.teleport(map.getSpawn2());
		
		while (i!=0){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
	            public void run() {
	            	p1.sendMessage(s + "Duel starts in " + i + " seconds");
	            }
	        }, i * 20);
			i--;
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	DuelData.cannotMove.remove(p1);
            	DuelData.cannotMove.remove(p2);
            }
        }, 100);
		
	}
	
	public void endDuel(){
		
		Duel d = this;
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			 
            public void run() {
                
				p1.teleport(locp1);
				p2.teleport(locp2);
				
				DuelData.duels.remove(d);
				DuelData.kits.remove(p1);
				DuelData.kits.remove(p2);
				DuelData.players.remove(p1);
				DuelData.players.remove(p2);
				
				Kit.kits.put(p1, p1kit);
				Kit.kits.put(p2, p2kit);
            	
            }
        }, 100);
		
	}
	
}

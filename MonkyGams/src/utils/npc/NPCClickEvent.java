package utils.npc;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface NPCClickEvent {

	public void onClick(Player p);
	
}

package games.game.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StackerManager {

	public HashMap<String, Stack<String>> stacks = new HashMap<>();
	
	public boolean throwPlayer(String player) {

		Player p = Bukkit.getPlayer(player);
		if (p == null) return false;

		Stack<String> stack = stacks.get(p.getName());
		if (stack == null) return false;
		if (stack.isEmpty()) return false;

		String playerOnTop = stack.pop();
		Player p2 = Bukkit.getPlayer(playerOnTop);
		if (p2 == null) return false;

		if (stack.isEmpty()) {
			p.removePassenger(p2);
		}
		else {
			p.removePassenger(Bukkit.getPlayer(stack.peek()));
		}
		p2.setVelocity(p.getLocation().getDirection().multiply(4));
		return true;

	}

	public void removePlayerFromStack(Player p) {

		for (Stack<String> stack : stacks.values()) {
			if (stack.contains(p.getName())) {
				int index = stack.indexOf(p.getName());

				ArrayList<String> toRemove = new ArrayList<>();
				stack.forEach(e -> {
					if (stack.indexOf(e) >= index) toRemove.add(e);
				});
				toRemove.forEach(e -> stack.remove(e));
				p.getPassengers().forEach(passenger -> p.removePassenger(passenger));
			}
		}

	}

	public void stackPlayer(Player stacker, Player stackee) {

		if (stacks.get(stacker.getName()) == null) {
			stacks.put(stacker.getName(), new Stack<String>());
		}

		Stack<String> stack = stacks.get(stacker.getName());
		
		if (stack.contains(stackee.getName())) {
			return;
		}
		
		removePlayerFromStack(stackee);
		
		if (stack.isEmpty()) stacker.addPassenger(stackee);
		else Bukkit.getPlayer(stack.peek()).addPassenger(stackee);
		
		stack.push(stackee.getName());
		

	}

}

package games.game.elytrabattle;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import core.data.PlayerStatisticsManager;
import games.meta.GameController;
import games.meta.statistics.ElytraBattleStatistics;
import net.minecraft.server.v1_15_R1.EntityMagmaCube;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_15_R1.World;
import utils.Utils;
import utils.game.GameUtils;

public class CompassEvents implements Listener {

	private static HashMap<EntityMagmaCube, Location> magmas = new HashMap<>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (!e.hasItem()) return;
		if (e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();
		ItemStack i = e.getItem();

		if (i.getType() != Material.COMPASS) return;

		ItemMeta meta = i.getItemMeta();

		if (meta.getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Xrayer's Compass")) {

			Location l = p.getLocation();
			
			int blocks = 0;
			
			if (l.getBlockY() - 5 > 15) {
				p.sendMessage(GameUtils.getFailureMessage("MINER'S COMPASS", "You won't find any diamonds at this level!"));
				return;
			}
			
			for (int x = l.getBlockX() - 10; x != l.getBlockX() + 10; x++) {
				for (int y = l.getBlockY() - 5; y != l.getBlockY() + 5; y++) {
					for (int z = l.getBlockZ() - 10; z != l.getBlockZ() + 10; z++) {
						if (l.getWorld().getBlockAt(x, y, z).getType() == Material.DIAMOND_ORE) {
							blocks++;
							glowBlock(p, new Location(l.getWorld(), x, y, z), 80);
						}
					}
				}
			}

			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).diamondsXrayed++;
			
			if (blocks == 0) {
				p.sendMessage(GameUtils.getSuccessMessage("XRAYER'S COMPASS", "Searched for diamonds nearby... but there are none around..."));
			}
			else {
				p.sendMessage(GameUtils.getSuccessMessage("XRAYER'S COMPASS", "Found " + blocks + " diamonds!"));
			}
			
			int uses = Integer.valueOf(ChatColor.stripColor(meta.getLore().get(2)).substring(0, 1));
			if (uses == 1) {
				p.getInventory().removeItem(i);
				p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);
			}
			else {
				uses--;
				List<String> lore = meta.getLore();
				lore.set(2, ChatColor.GRAY + "" + uses + " use" + Utils.plural(uses) + " left.");
				meta.setLore(lore);
				i.setItemMeta(meta);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 10, 1);
			}
			return;
			
		}
		
		if (meta.getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Chest Hunter's Compass")) {
			
			Location l = p.getLocation();
			
			if (l.getBlockY() < 50) {
				p.sendMessage(GameUtils.getFailureMessage("CHEST HUNTER'S COMPASS", "You won't find any chests this far underground!"));
				return;
			}
			
			int blocks = 0;
			ElytraBattleGame game = GameController.getCurrentGame(ElytraBattleGame.class);
			
			for (Chest c : game.chests.getChests()) {
				if (c.getLocation().distance(p.getLocation()) < 200) {
					glowBlock(p, c.getLocation(), 100);
					blocks++;
				}
			}

			PlayerStatisticsManager.getStatistics(e.getPlayer()).getStatistics(ElytraBattleStatistics.class).chestsXrayed++;
			
			if (blocks == 0) {
				p.sendMessage(GameUtils.getSuccessMessage("CHEST HUNTER'S COMPASS", "Searched for chests nearby... but there are none around..."));
			}
			else {
				p.sendMessage(GameUtils.getSuccessMessage("CHEST HUNTER'S COMPASS", "Found " + blocks + " chests!"));
			}
			
			int uses = Integer.valueOf(ChatColor.stripColor(meta.getLore().get(2)).substring(0, 1));
			if (uses == 1) {
				p.getInventory().removeItem(i);
				p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);
			}
			else {
				uses--;
				List<String> lore = meta.getLore();
				lore.set(2, ChatColor.GRAY + "" + uses + " use" + Utils.plural(uses) + " left.");
				meta.setLore(lore);
				i.setItemMeta(meta);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 10, 1);
			}
			return;
		}

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		
		for (Entry<EntityMagmaCube, Location> entry : magmas.entrySet()) {
			if (GameUtils.areLocationsEqual(e.getBlock().getLocation(), entry.getValue())) {
				PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entry.getKey().getId());
				Bukkit.getOnlinePlayers().forEach((p) -> {
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroy);
				});
				magmas.remove(entry.getKey());
			}
		}
		
	}
	
	/**
	 * <b>This could be replaced with ProtocolLib.</b>
	 * https://www.spigotmc.org/threads/spawn-entity-using-protocollib-wrappers.370803/
	 * @param p
	 * @param l
	 * @param ticksLength
	 */
	public static void glowBlock(Player p, Location l, int ticksLength) {

		World world = ((CraftWorld) l.getWorld()).getHandle();
		EntityMagmaCube entity = new EntityMagmaCube(EntityTypes.MAGMA_CUBE, world);
		entity.setPosition((double) l.getBlockX() + 0.5D, (double) l.getBlockY(), (double) l.getBlockZ() + 0.5D);
		entity.setInvisible(true);
		entity.setInvulnerable(true);
		entity.setNoAI(true);
		entity.setSilent(true);
		entity.setFlag(6, true); // Glow
		entity.setFlag(5, true); // Invisibility
		entity.setSize(2, false);
		magmas.put(entity, l);

		PacketPlayOutSpawnEntityLiving spawnEntityLiving = new PacketPlayOutSpawnEntityLiving(entity);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawnEntityLiving);
		PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entity.getId(),entity.getDataWatcher(),false);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(entityMetadata);

		GameUtils.delayTask(() -> {
			magmas.remove(entity);
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entity.getId());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroy);
		}, ticksLength);
	}

}

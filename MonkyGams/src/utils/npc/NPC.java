package utils.npc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import core.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;
import utils.game.GameUtils;

/**
 * Warning: Uses NMS. <b>Version dependent.</b>
 * Use Entity.hasMetadata(name) to detect.
 * @author Tom
 *
 */
public class NPC {

	public Location getLocation() {
		return l;
	}
	private EntityPlayer player;
	private String name;
	private String uuid;
	private GameProfile profile;
	private Location l;
	private Horse horse;
	private NPCClickEvent clickEvent;

	public EntityPlayer getPlayer() {
		return player;
	}
	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}
	public String getName() {
		return name;
	}

	public NPC(Location l, String skinUuid, String name) {

		name = ChatColor.translateAlternateColorCodes('&', name);
		
		this.name = name;
		this.l = l;
		this.uuid = skinUuid;
		
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) l.getWorld()).getHandle();

		profile = new GameProfile(UUID.randomUUID(), name);
		setSkin(skinUuid);
		
		player = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
		player.getBukkitEntity().setMetadata(name, new FixedMetadataValue(Main.getPlugin(), true));
		player.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		
		horse = (Horse) l.getWorld().spawnEntity(l, EntityType.HORSE);
		horse.setInvulnerable(true);
		PotionEffect pe = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false);
		horse.addPotionEffect(pe);
		horse.setSilent(true);
		horse.setAI(false);
		horse.setRemoveWhenFarAway(false);
		
	}
	
	public void show(Player p) {
		GameUtils.delayTask(() -> {
			PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
			con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player));
			con.sendPacket(new PacketPlayOutNamedEntitySpawn(player));
			con.sendPacket(new PacketPlayOutEntityHeadRotation(player, (byte) (l.getYaw() * 256 / 360)));
			GameUtils.delayTask(() -> con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, player)), 40);
		}, 1);
	}
	
	public void moveHead(float yaw, float pitch) {
		Bukkit.getOnlinePlayers().forEach((p) -> {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityLook(player.getId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(player, (byte) (yaw * 256 / 360)));
		});
	}

	public void showForAll() {
		Bukkit.getOnlinePlayers().forEach(this::show);
	}
	
	public void despawn() {
		
		Bukkit.getOnlinePlayers().forEach(p -> {
			PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
			con.sendPacket(new PacketPlayOutEntityDestroy(player.getId()));
		});
		horse.remove();
	}
	
	public void register() {
		NPCManager.getNpcs().add(this);
	}
	public void deregister() {
		NPCManager.getNpcs().remove(this);
	}

	/**
	 * <b>This method relies on a web API. It is therefore slow.</b>
	 * @param uuid
	 */
	public void setSkin(String uuid) {

		uuid = uuid.replaceAll("-", "");
		
		try {
			
			String value = "";
			String signature = "";
			
			if (NPCManager.getCache().containsKey(uuid)) {
				String textures = NPCManager.getCache().get(uuid);
				String[] split = textures.split(":");
				value = split[0];
				signature = split[1];
			}
			else {
				URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");

				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line = reader.readLine();
				con.disconnect();
				
				value = line.substring(line.indexOf("\"value\":\"") + "\"value\":\"".length());
				value = value.substring(0, value.indexOf("\","));
				
				signature = line.substring(line.indexOf("\"signature\":\"") + "\"signature\":\"".length());
				signature = signature.substring(0, signature.indexOf("\""));
				
				NPCManager.getCache().put(uuid, value + ":" + signature);
			}
			
			profile.getProperties().put("textures", new Property("textures", value, signature));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public String getUuid() {
		return uuid;
	}
	public NPCClickEvent getClickEvent() {
		return clickEvent;
	}
	public void setClickEvent(NPCClickEvent clickEvent) {
		this.clickEvent = clickEvent;
	}
	public Horse getHorse() {
		return horse;
	}
}

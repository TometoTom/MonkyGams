package games.game.elytrarun;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.Location;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;

import core.Main;

public enum MapPart {

	START(new File(Main.getPlugin().getDataFolder() + "/schematics/Start2.schem"), 0, 0F, 7, 8),
	MIDDLE(new File(Main.getPlugin().getDataFolder() + "/schematics/Middle2.schem"), 0, 0.8F, 7, 8),
	LEFT_TURN(new File(Main.getPlugin().getDataFolder() + "/schematics/Left_Turn.schem"), -90, 0.05F, 9, 8),
	RIGHT_TURN(new File(Main.getPlugin().getDataFolder() + "/schematics/Right_Turn.schem"), 90, 0.05F, 9, 8);
	
	private File schematic;
	private int endRotation;
	private float rarity;
	private int xSize;
	private int zSize;
	
	private MapPart(File schematic, int endRotation, float rarity, int xSize, int zSize) {
		
		this.schematic = schematic;
		this.endRotation = endRotation;
		this.rarity = rarity;
		this.xSize = xSize;
		this.zSize = zSize;
		
	}
	
	public File getSchematic() {
		return schematic;
	}

	public int getEndRotation() {
		return endRotation;
	}

	public float getRarity() {
		return rarity;
	}

	public int getxSize() {
		return xSize;
	}

	public int getzSize() {
		return zSize;
	}
	
	public void paste(Location l) {
		paste(l, 0);
	}
	
	public void paste(Location l, float rotation) {
		ClipboardFormat format = ClipboardFormats.findByFile(getSchematic());
		try (ClipboardReader reader = format.getReader(new FileInputStream(getSchematic()))) {
			Clipboard clipboard = reader.read();
			
			com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(l.getWorld());
			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
			ClipboardHolder ch = new ClipboardHolder(clipboard);
			if (rotation != 0F) {
				AffineTransform transform = new AffineTransform();
				transform = transform.rotateY(rotation);
				ch.setTransform(transform);
			}
			Operation operation = ch.createPaste(editSession)
		            .to(BlockVector3.at(l.getBlockX(), l.getBlockY(), l.getBlockZ()))
		            .ignoreAirBlocks(false)
		            .build();
			Operations.complete(operation);
			editSession.flushSession();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

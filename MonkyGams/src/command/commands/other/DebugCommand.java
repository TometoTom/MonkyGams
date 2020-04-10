package command.commands.other;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import command.meta.Args;
import command.meta.CommandCompatibility;
import command.meta.MonkyCommand;
import command.meta.PermissionLevel;
import games.game.elytrarun.MapPart;

public class DebugCommand extends MonkyCommand {

	@Override
	public void onCommand(Player p, Args args) {
		//		
		//		for (String uuid : RankManager.getRankManager().getOwners()) {
		//			s(p, "Testing " + uuid + " against " + p.getUniqueId().toString() + " - " + uuid.equalsIgnoreCase(p.getUniqueId().toString()) + ". " + RankManager.getRankManager().getOwners().contains(p.getUniqueId().toString()));
		//		}
		//		
		//		s(p, "Rank of " + p.getName() + ": " + RankManager.getRankManager().getRank(p));
		//		s(p, "Owners contents: " + RankManager.getRankManager().getOwners().get(0));
		//		s(p, "Has rank: " + RankManager.getRankManager().hasRank(p, PermissionLevel.OWNER));
		//		s(p, "Has rank: " + RankManager.getRankManager().hasRank(p, PermissionLevel.ADMIN));
		//		s(p, "Has rank: " + RankManager.getRankManager().hasRank(p, PermissionLevel.BUILDER));
		//		
		//		Map m = Map.getMap("two", GameType.ELYTRAPARKOUR);
		//		
		//		p.teleport(new Location(m.getSpawn().getWorld(), m.getSpawn().getX(), m.getSpawn().getY(), m.getSpawn().getZ(), m.getSpawn().getPitch(), m.getSpawn().getYaw()));
		////		p.teleport(m.getSpawn());
		//		s(p, "pitch: " + m.getSpawn().getPitch() + ", yaw: " + m.getSpawn().getYaw());
		//		
		//		String type = args.remove(0);
		//		
		//		if (type.equalsIgnoreCase("1")) {
		//			if (stage == 0) {
		//				initial = p.getTargetBlock(null, 100).getLocation();
		//				s(p, "First block set");
		//				stage++;
		//				return;
		//			}
		//			if (stage == 1) {
		//				second = p.getTargetBlock(null, 100).getLocation();
		//				s(p, "Second block set");
		//				stage++;
		//				return;
		//			}
		//			if (stage == 2) {
		//				
		//				if (args.isEmpty()) {
		//					f(p, "Name required.");
		//					return;
		//				}
		//				
		//				//Next we will name each coordinate
		//		        int x1 = initial.getBlockX();
		//		        int y1 = initial.getBlockY();
		//		        int z1 = initial.getBlockZ();
		//		 
		//		        int x2 = second.getBlockX();
		//		        int y2 = second.getBlockY();
		//		        int z2 = second.getBlockZ();
		//		 
		//		        //Then we create the following integers
		//		        int xMin, yMin, zMin;
		//		        int xMax, yMax, zMax;
		//		        int x, y, z;
		//		 
		//		        //Now we need to make sure xMin is always lower then xMax
		//		        if(x1 > x2){ //If x1 is a higher number then x2
		//		            xMin = x2;
		//		            xMax = x1;
		//		        }else{
		//		            xMin = x1;
		//		            xMax = x2;
		//		        }
		//		 
		//		        //Same with Y
		//		        if(y1 > y2){
		//		            yMin = y2;
		//		            yMax = y1;
		//		        }else{
		//		            yMin = y1;
		//		            yMax = y2;
		//		        }
		//		 
		//		        //And Z
		//		        if(z1 > z2){
		//		            zMin = z2;
		//		            zMax = z1;
		//		        }else{
		//		            zMin = z1;
		//		            zMax = z2;
		//		        }
		//		 
		//		        ArrayList<MapPartBlock> blocks = new ArrayList<MapPartBlock>();
		//		        
		//		        //Now it's time for the loop
		//		        for(x = xMin; x <= xMax; x ++){
		//		            for(y = yMin; y <= yMax; y ++){
		//		                for(z = zMin; z <= zMax; z ++){
		//		                    Block b = new Location(p.getWorld(), x, y, z).getBlock();
		//		                    blocks.add(new MapPartBlock(initial.getBlockX() - b.getX(), initial.getBlockY() - b.getY(), initial.getBlockZ() - b.getZ(), b.getType()));
		//		                }
		//		            }
		//		        }
		//		 
		//				s(p, blocks.size() + " blocks saved.");
		//				
		//				MapPart m = new MapPart(args.get(0), "", 0.2f, new SerialisableLocation(initial), new SerialisableLocation(second), blocks);
		//				MapPart.getMapParts().add(m);
		//				s(p, "Added " + m.getName() + ".");
		//				stage = 0;
		//				return;
		//			}
		//		}
		//		else if (type.equalsIgnoreCase("2")) {
		//			if (args.isEmpty()) {
		//				f(p, "Part name required");
		//				return;
		//			}
		//			
		//			String partName = args.get(0);
		//			
		//			MapPart mp = MapPart.getMapPart(partName);
		//			
		//			if (mp == null) {
		//				f(p, "Part not found");
		//				return;
		//			}
		//			
		//			for (MapPartBlock b : mp.getBlocks()) {
		//			
		//				p.getWorld().getBlockAt(
		//					p.getLocation().getBlockX() - b.getRelativeX(),
		//					p.getLocation().getBlockY() - b.getRelativeY(),
		//					p.getLocation().getBlockZ() - b.getRelativeZ())
		//				.setType(b.getType());
		//				
		//			}
		//		}

//		File schem = new File(Main.getPlugin().getDataFolder() + File.separator + "schematics" + File.separator + args.get(0) + ".schem");
//		ClipboardFormat format = ClipboardFormats.findByFile(schem);
//		s(p, schem +"");
//		try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
//			Clipboard clipboard = reader.read();
//			
//			com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(p.getWorld());
//			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
//			Operation operation = new ClipboardHolder(clipboard)
//		            .createPaste(editSession)
//		            .to(BlockVector3.at(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()))
//		            .ignoreAirBlocks(false)
//		            .build();
//			Operations.complete(operation);
//			editSession.flushSession();
//			s(p, "Done");
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
		
		MapPart[] parts = new MapPart[] {
				MapPart.START,
				MapPart.MIDDLE,
				MapPart.MIDDLE,
				MapPart.MIDDLE,
				MapPart.MIDDLE,
				MapPart.LEFT_TURN,
				MapPart.MIDDLE,
				MapPart.MIDDLE,
				MapPart.MIDDLE,
				MapPart.LEFT_TURN,
				MapPart.MIDDLE,
				MapPart.MIDDLE,
				MapPart.MIDDLE,
				MapPart.LEFT_TURN,
				MapPart.MIDDLE,
				MapPart.MIDDLE
		};
		
		int x = p.getLocation().getBlockX();
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ();
		float currentRotation = 0F;
		
		for (MapPart part : parts) {
			part.paste(new Location(p.getWorld(), x, y, z), currentRotation);
			
			float d = getDirection(currentRotation);
			
			Bukkit.broadcastMessage("Current direction is " + d);
			
			if (d == 180F) z += part.getzSize();
			if (d == -90F) x -= part.getxSize();
			if (d == 0F) z -= part.getzSize();
			if (d == 90F) x += part.getxSize();
			
			currentRotation += part.getEndRotation();
			
			if (part.getEndRotation() != 0) {
				d = getDirection(currentRotation + part.getEndRotation());
				if (d == 180F) z += part.getzSize();
				if (d == -90F) x -= part.getxSize();
				if (d == 0F) z -= part.getzSize();
				if (d == 90F) x += part.getxSize();
			}
			
		}
		
//		MapPart.START.paste(p.getLocation());
//		z -= MapPart.START.getzSize();
//		
//		MapPart.MIDDLE.paste(new Location(p.getWorld(), x, y, z));
//		z -= MapPart.MIDDLE.getzSize();
//		
//		MapPart.MIDDLE.paste(new Location(p.getWorld(), x, y, z));
//		z -= MapPart.MIDDLE.getzSize();
//		
//		MapPart.MIDDLE.paste(new Location(p.getWorld(), x, y, z));
//		z -= MapPart.MIDDLE.getzSize();
//		
//		MapPart.LEFT_TURN.paste(new Location(p.getWorld(), x, y, z));
//		z -= MapPart.LEFT_TURN.getzSize();
//		x -= MapPart.LEFT_TURN.getxSize();
//		currentRotation += MapPart.LEFT_TURN.getEndRotation();
//		
//		MapPart.MIDDLE.paste(new Location(p.getWorld(), x, y, z), currentRotation);
//		x -= MapPart.MIDDLE.getzSize();
//		
//		MapPart.MIDDLE.paste(new Location(p.getWorld(), x, y, z), currentRotation);
//		x -= MapPart.MIDDLE.getzSize();
//		
//		MapPart.MIDDLE.paste(new Location(p.getWorld(), x, y, z), currentRotation);
//		x -= MapPart.MIDDLE.getzSize();
		
	}

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String getDescription() {
		return "Used for debugging things";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] {};
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.OWNER;
	}
	
	@Override
	public CommandCompatibility getCommandCompatibility() {
		return CommandCompatibility.UNIVERSAL;
	}
	
	/* -180 to 180 */
	public float getDirection(float d) {
		
		while (!(d <= 180 && d >= -179.9)) {
			if (d > 180) d -= 180;
			else if (d < 179.9) d += 180;
		}
		
		return d;
		
	}


}

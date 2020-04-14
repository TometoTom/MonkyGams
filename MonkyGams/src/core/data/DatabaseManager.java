package core.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import command.meta.PermissionLevel;
import core.Main;
import games.meta.GameType;
import games.meta.statistics.GameStatistics;

public class DatabaseManager {

	private static Connection connection;
	
	public static String HOST = Main.getPlugin().getConfig().getString("host");
	public static String DATABASE = Main.getPlugin().getConfig().getString("databaseName");
	public static String USERNAME = Main.getPlugin().getConfig().getString("username");
	public static String PASSWORD = Main.getPlugin().getConfig().getString("password");
	public static int PORT = Main.getPlugin().getConfig().getInt("port");
	public static String TABLE = "player_data";
	
	public synchronized static @Nullable Connection getConnection() {
		
		if (HOST == null || DATABASE == null || USERNAME == null || PASSWORD == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Config is not set up correctly; there are no database fields. Could not connect to database.");
			return null;
		}
		
		try {
			if (connection == null || connection.isClosed()) {
				
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
				
			}
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not connect to database.");
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static PlayerStatistics getPlayerData(String uuid) {
		Connection c = getConnection();
		if (c == null) {
			return null;
		}
		
		try {
			PreparedStatement statement = c.prepareStatement("SELECT * FROM " + TABLE + " WHERE UUID=?");
			statement.setString(1, uuid);
			
			ResultSet results = statement.executeQuery();
			
			if (results.next()) {
				String response = results.getString(2);
				Bukkit.getLogger().log(Level.INFO, "Received information from the database: " + response);
				
				RuntimeTypeAdapterFactory<GameStatistics> fac = RuntimeTypeAdapterFactory.of(GameStatistics.class, "type");
				for (GameType gt : GameType.values()) {
					if (gt.getStatsClass() != null) {
						fac.registerSubtype(gt.getStatsClass(), gt.getStatsClass().getName());
					}
				}
				Gson gson = new GsonBuilder().registerTypeAdapterFactory(fac).create();
				
				return gson.fromJson(response, PlayerStatistics.class);
			}
			else {
				
				PlayerStatistics ps = new PlayerStatistics(uuid, PermissionLevel.NONE, 0, new HashMap<>(), 0, 0, 0, 1, "", new HashMap<>(), new ArrayList<>(), "", "");
				
				statement = c.prepareStatement("INSERT INTO " + TABLE + " (UUID,DATA) VALUE (?,?)");
				statement.setString(1, uuid);
				statement.setString(2, new Gson().toJson(ps));
				statement.executeUpdate();
				return ps;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} catch (Exception e) { }
		}
		return null;
	}
	
	public static void updatePlayerData(PlayerStatistics ps) {
		
		Connection c = getConnection();
		if (c == null) {
			return;
		}
		try {
			PreparedStatement statement = c.prepareStatement("UPDATE " + TABLE + " SET DATA=? WHERE UUID=?");
			statement.setString(1, new Gson().toJson(ps, PlayerStatistics.class));
			statement.setString(2, ps.getUuid());
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		finally {
			try {
				connection.close();
			} catch (Exception e) { }
		}
		
	}
	
}

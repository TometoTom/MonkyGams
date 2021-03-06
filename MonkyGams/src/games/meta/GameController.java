package games.meta;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import games.game.dropper.DropperGame;
import games.game.elytrabattle.ElytraBattleGame;
import games.game.elytraparkour.ElytraParkourGame;
import games.game.kitpvp.KitPVPGame;
import games.game.lobby.LobbyGame;
import games.game.monkykart.MonkyKartGame;
import games.game.parkourrace.ParkourRaceGame;
import games.meta.statistics.DataRecorder;
import games.meta.statistics.GameStatistics;
import games.meta.statistics.XPCalculation;
import hub.gadgets.meta.Gadget;

public class GameController {

	private static Game currentGame;
	private static HashMap<String, Kit> nextGameKits = new HashMap<>();

	public static Game getCurrentGame() {
		
		return currentGame;
		
	}
	
	public static <T> T getCurrentGame(Class<T> clazz){
		try {
			return clazz.cast(getCurrentGame());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static GameType getCurrentGameType() {
		
		return getCurrentGame() == null ? null : getCurrentGame().getType();
		
	}
	
	public static void endCurrentGame() {
		endCurrentGame(true, currentGame.getType());
	}
	
	public static void endCurrentGame(boolean startLobby, GameType queuedGame) {
		
		if (currentGame == null) return;
		
		Class<? extends GameStatistics> statsClass = currentGame.getType().getStatsClass();
		if (statsClass != null) {
			try {
				DataRecorder recorder = currentGame.getStats();
				for (Player p : Bukkit.getOnlinePlayers()) {
					PlayerStatistics ps = PlayerStatisticsManager.getStatistics(p);
					GameStatistics s = currentGame.getType().getStatsClass().cast(ps.getStatistics(currentGame.getType()));
					long joinTime = recorder.getJoinTimes().getOrDefault(p.getName(), 0L);
					long leaveTime = recorder.getLeaveTimes().getOrDefault(p.getName(), System.currentTimeMillis());
					if (joinTime != 0) {
						s.addTimePlayed(leaveTime - joinTime);
					}
					s.incrementGamesPlayed();
					
					XPCalculation xp = new XPCalculation(recorder);
					ps.setCurrency(ps.getCurrency() + xp.getTimePounds(p));
					ps.setTotalXP(ps.getTotalXP() + xp.getTimeXP(p));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Gadget.disableAllGadgets();
		
		currentGame.cancelCountdown();
		currentGame.end();
		
		if (startLobby) {
			currentGame = new LobbyGame(queuedGame);
			currentGame.start();
		}
		
	}
	
	public static boolean startNewGame(GameType g, Map m) {
		
		if (currentGame != null && !(currentGame instanceof LobbyGame)) {
			return false;
		}
		
		if (currentGame instanceof LobbyGame) {
			currentGame.end();
		}
		
		boolean gameStarted = false;
		if (g == GameType.ELYTRAPARKOUR) {
			currentGame = new ElytraParkourGame(m);
			currentGame.start();
			gameStarted = true;
		}
		else if (g == GameType.LOBBY) {
			currentGame = new LobbyGame(GameType.ELYTRAPARKOUR, m);
			currentGame.start();
			gameStarted = true;
		}
		else if (g == GameType.PARKOURRACE) {
			currentGame = new ParkourRaceGame(m);
			currentGame.start();
			gameStarted = true;
		}
		else if (g == GameType.DROPPER) {
			currentGame = new DropperGame(m);
			currentGame.start();
			gameStarted = true;
		}
		else if (g == GameType.MONKYKART) {
			currentGame = new MonkyKartGame(m);
			currentGame.start();
			gameStarted = true;
		}
		else if (g == GameType.ELYTRABATTLE) {
			currentGame = new ElytraBattleGame();
			currentGame.start();
			gameStarted = true;
		}
		else if (g == GameType.KITPVP) {
			currentGame = new KitPVPGame(m);
			currentGame.start();
			gameStarted = true;
		}
		
		HashMap<String, Kit> kitsCopied = new HashMap<String, Kit>();
		nextGameKits.entrySet().forEach(e -> {
			if (e.getValue().getGame() == getCurrentGameType()) {
				kitsCopied.put(e.getKey(), e.getValue());
			}
		});
		currentGame.getStats().setKits(kitsCopied);

		return gameStarted;
	}
	
	public static void clearKits() {
		nextGameKits.clear();
	}
	
	public static void putKit(Player p, Kit k) {
		nextGameKits.put(p.getName(), k);
	}
	
}

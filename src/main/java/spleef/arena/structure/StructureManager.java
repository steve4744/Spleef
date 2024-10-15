/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package spleef.arena.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import spleef.arena.Arena;
import spleef.utils.Utils;

public class StructureManager {

	private Arena arena;
	private GameZone gamezone;

	public StructureManager(Arena arena) {
		this.arena = arena;
		gamezone = new GameZone(arena);
	}

	private String world;
	private Vector p1 = null;
	private Vector p2 = null;
	private int gameleveldestroydelay = 8;
	private LoseLevel loselevel = new LoseLevel();
	private SpectatorSpawn spectatorspawn = new SpectatorSpawn();
	private PlayerSpawn playerspawn = new PlayerSpawn();
	private WaitingSpawn waitingspawn = new WaitingSpawn();
	private int minPlayers = 2;
	private int maxPlayers = 15;
	private double votesPercent = 0.75;
	private int timelimit = 300;
	private int countdown = 10;
	private int startVisibleCountdown = 10;
	private Rewards rewards = new Rewards();
	private TeleportDestination teleportDest = TeleportDestination.PREVIOUS;
	private DamageEnabled damageEnabled = DamageEnabled.NO;
	private boolean punchDamage = true;
	private boolean kitsEnabled = false;
	private boolean linkedRandom = true;
	private List<String> linkedKits = new ArrayList<>();
	private boolean testmode = false;
	private boolean excludeStats = false;
	private boolean statsEnabled = true;
	private int statsMinPlayers = 0;
	private boolean allowDoublejumps = true;
	private int regenerationdelay = 60;
	private String currency;
	private double fee = 0;
	private boolean finished = false;
	private List<Vector> additionalSpawnPoints = new ArrayList<>();
	private List<Vector> freeSpawnList = new ArrayList<>();
	private String commandOnStart;
	private String commandOnStop;
	private boolean shopEnabled = true;
	private int maxFinalPositions = 3;
	private boolean enableOnRestart = true;

	public String getWorldName() {
		return world;
	}

	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	public Vector getP1() {
		return p1;
	}

	public Vector getP2() {
		return p2;
	}

	public GameZone getGameZone() {
		return gamezone;
	}

	public int getGameLevelDestroyDelay() {
		return gameleveldestroydelay;
	}

	public LoseLevel getLoseLevel() {
		return loselevel;
	}

	public Vector getSpectatorSpawnVector() {
		return spectatorspawn.getVector();
	}

	public Location getSpectatorSpawn() {
		if (spectatorspawn.isConfigured()) {
			return new Location(getWorld(),
								spectatorspawn.getVector().getX(),
								spectatorspawn.getVector().getY(),
								spectatorspawn.getVector().getZ(),
								spectatorspawn.getYaw(),
								spectatorspawn.getPitch());
		}
		return null;
	}

	public Vector getWaitingSpawnVector() {
		return waitingspawn.getVector();
	}

	public Location getWaitingSpawn() {
		if (waitingspawn.isConfigured()) {
			return new Location(getWorld(),
								waitingspawn.getVector().getX(),
								waitingspawn.getVector().getY(),
								waitingspawn.getVector().getZ(),
								waitingspawn.getYaw(),
								waitingspawn.getPitch());
		}
		return null;
	}

	public Vector getSpawnPointVector() {
		return playerspawn.getVector();
	}

	public Location getSpawnPoint() {
		Vector v = playerspawn.getVector();
		if (hasAdditionalSpawnPoints()) {
			v = nextSpawnPoint();
		}
		return new Location(getWorld(),
							v.getX(),
							v.getY(),
							v.getZ(),
							playerspawn.getYaw(),
							playerspawn.getPitch());
	}

	public Location getPrimarySpawnPoint() {
		return new Location(getWorld(), playerspawn.getVector().getX(), playerspawn.getVector().getY(), playerspawn.getVector().getZ());
	}

	public List<Vector> getAdditionalSpawnPoints() {
		return additionalSpawnPoints;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getMinPlayers() {
		return minPlayers;
	}

	public double getVotePercent() {
		return votesPercent;
	}

	public int getTimeLimit() {
		return timelimit;
	}

	public int getCountdown() {
		return countdown;
	}

	public int getStartVisibleCountdown() {
		return startVisibleCountdown;
	}

	public Rewards getRewards() {
		return rewards;
	}

	public TeleportDestination getTeleportDestination() {
		return teleportDest;
	}

	public static enum TeleportDestination {
		PREVIOUS, LOBBY;
	}

	public DamageEnabled getDamageEnabled() {
		return damageEnabled;
	}

	public static enum DamageEnabled {
		YES, ZERO, NO
	}

	public boolean isKitsEnabled() {
		return kitsEnabled;
	}

	public boolean isPunchDamage() {
		return punchDamage;
	}

	public boolean isTestMode() {
		return testmode;
	}

	public boolean isArenaStatsEnabled() {
		return statsEnabled;
	}

	public int getStatsMinPlayers() {
		return statsMinPlayers;
	}

	public boolean isAllowDoublejumps() {
		return allowDoublejumps;
	}

	public boolean isShopEnabled() {
		return shopEnabled;
	}

	public int getRegenerationDelay() {
		return regenerationdelay;
	}

	public double getFee() {
		return fee;
	}

	public int getMaxFinalPositions() {
		return maxFinalPositions;
	}

	public Material getCurrency() {
		return Material.getMaterial(currency);
	}

	public boolean hasFee() {
		return fee > 0;
	}

	public boolean isCurrencyEnabled() {
		return Material.getMaterial(currency) != null && !Material.getMaterial(currency).isAir();
	}

	public String getArenaCost() {
		if (!isCurrencyEnabled()) {
			return Utils.getFormattedCurrency(String.valueOf(fee));
		}
		return new StringBuilder().append((int) fee).append(" x ").append(currency).toString();
	}

	public String getCommandOnStart() {
		return commandOnStart != null ? commandOnStart : "";
	}

	public String getCommandOnStop() {
		return commandOnStop != null ? commandOnStop : "";
	}

	public boolean hasCommandOnStart() {
		return commandOnStart != null && commandOnStart.length() > 0;
	}

	public boolean hasCommandOnStop() {
		return commandOnStop != null && commandOnStop.length() > 0;
	}

	public boolean isInArenaBounds(Location loc) {
		if (loc.toVector().isInAABB(getP1(), getP2())) {
			return true;
		}
		return false;
	}

	public boolean isArenaBoundsSet() {
		if (getP1() == null || getP2() == null || world == null) {
			return false;
		}
		return true;
	}

	public boolean isArenaConfigured() {
		return isArenaConfiguredString().equals("yes");
	}

	public String isArenaConfiguredString() {
		if (getP1() == null || getP2() == null || world == null) {
			return "Arena bounds not set";
		}
		if (!loselevel.isConfigured()) {
			return "Arena loselevel not set";
		}
		if (!playerspawn.isConfigured()) {
			return "Arena spawnpoint not set";
		}
		return "yes";
	}

	public boolean isArenaFinished() {
		return finished;
	}

	public boolean isSpawnpointSet() {
		return playerspawn.isConfigured();
	}

	public boolean isSpectatorSpawnSet() {
		return spectatorspawn.isConfigured();
	}

	public boolean isWaitingSpawnSet() {
		return waitingspawn.isConfigured();
	}

	public boolean isPvpEnabled() {
		return !getDamageEnabled().toString().equalsIgnoreCase("no");
	}

	public boolean isEnableOnRestart() {
		return enableOnRestart;
	}

	public void setArenaFinished(boolean finished) {
		this.finished = finished;
	}

	public void setArenaPoints(Location loc1, Location loc2) {
		world = loc1.getWorld().getName();
		p1 = loc1.toVector();
		p2 = loc2.toVector();
	}

	public void setGameLevelDestroyDelay(int delay) {
		gameleveldestroydelay = delay;
	}

	public boolean setLoseLevel(Location loc1) {
		if (isInArenaBounds(loc1)) {
			loselevel.setLoseLocation(loc1);
			return true;
		}
		return false;
	}

	public boolean setSpawnPoint(Location loc) {
		if (isInArenaBounds(loc)) {
			playerspawn.setPlayerSpawn(loc);
			return true;
		}
		return false;
	}

	public boolean setSpectatorsSpawn(Location loc) {
		if (isInArenaBounds(loc)) {
			spectatorspawn.setSpectatorSpawn(loc);
			return true;
		}
		return false;
	}

	public boolean setWaitingSpawn(Location loc) {
		if (isInArenaBounds(loc)) {
			waitingspawn.setWaitingSpawn(loc);
			return true;
		}
		return false;
	}

	/**
	 * Creates an additional spawn point from the supplied location.
	 * @param loc
	 * @return true if created successfully
	 */
	public boolean addSpawnPoint(Location loc) {
		if (isInArenaBounds(loc)) {
			additionalSpawnPoints.add(loc.toVector());
			return true;
		}
		return false;
	}

	public void removeSpectatorsSpawn() {
		spectatorspawn.remove();
	}

	public void removeWaitingSpawn() {
		waitingspawn.remove();
	}

	public void removeAdditionalSpawnPoints() {
		additionalSpawnPoints.clear();
	}

	public void setMaxPlayers(int maxplayers) {
		maxPlayers = maxplayers;
	}

	public void setMinPlayers(int minplayers) {
		minPlayers = minplayers;
	}

	public void setVotePercent(double votepercent) {
		votesPercent = votepercent;
	}

	public void setTimeLimit(int timelimit) {
		this.timelimit = timelimit;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public void setStartVisibleCountdown(int start) {
		this.startVisibleCountdown = start > 0 ? start : 0;
	}

	public void togglePunchDamage() {
		this.punchDamage = !punchDamage;
	}

	public void toggleTestMode() {
		this.testmode = !testmode;
	}

	public void toggleArenaStats() {
		this.statsEnabled = !statsEnabled;
	}

	public void toggleShopEnabled() {
		this.shopEnabled = !shopEnabled;
	}

	public void setStatsMinPlayers(int amount) {
		this.statsMinPlayers = amount > 0 ? amount : 0;
	}

	public void setTeleportDestination(TeleportDestination teleportDest) {
		this.teleportDest = teleportDest;
	}

	public void setDamageEnabled(DamageEnabled damageEnabled) {
		this.damageEnabled = damageEnabled;
	}
	
	public void enableKits(boolean kitsEnabled) {
		this.kitsEnabled = kitsEnabled;
	}

	public void linkKit(String kitName) {
		linkedKits.add(kitName);
	}

	public void unlinkKit(String kitName) {
		linkedKits.remove(kitName);
	}

	public List<String> getLinkedKits() {
		return linkedKits;
	}

	public boolean hasLinkedKits() {
		return linkedKits.size() > 0;
	}

	public boolean isRandomKit() {
		return linkedRandom;
	}

	public void setRegenerationDelay(int regendelay) {
		this.regenerationdelay = regendelay;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public void setMaxFinalPositions(int size) {
		this.maxFinalPositions = size > 0 ? size : 1;
	}

	public void setCurrency(Material currency) {
		if (currency.isAir()) {
			this.currency = null;
			return;
		}
		this.currency = currency.toString();
	}

	public boolean hasAdditionalSpawnPoints() {
		return additionalSpawnPoints != null && !additionalSpawnPoints.isEmpty();
	}

	private Vector nextSpawnPoint() {
		if (freeSpawnList.isEmpty()) {
			freeSpawnList.add(playerspawn.getVector());
			freeSpawnList.addAll(additionalSpawnPoints);
		}
		return freeSpawnList.remove(0);
	}

	public List<Vector> getFreeSpawnList() {
		return freeSpawnList;
	}

	public void saveToConfig() {
		FileConfiguration config = new YamlConfiguration();
		try {
			config.set("world", world);
			config.set("p1", p1);
			config.set("p2", p2);
		} catch (Exception e) {
		}
		try {
			loselevel.saveToConfig(config);
		} catch (Exception e) {
		}
		try {
			playerspawn.saveToConfig(config);
		} catch (Exception e) {
		}
		try {
			spectatorspawn.saveToConfig(config);
		} catch (Exception e) {
		}
		try {
			waitingspawn.saveToConfig(config);
		} catch (Exception e) {
		}
		config.set("gameleveldestroydelay", gameleveldestroydelay);
		config.set("maxPlayers", maxPlayers);
		config.set("minPlayers", minPlayers);
		config.set("votePercent", votesPercent);
		config.set("timelimit", timelimit);
		config.set("countdown", countdown);
		config.set("startVisibleCountdown", startVisibleCountdown);
		config.set("teleportto", teleportDest.toString());
		config.set("damageenabled", damageEnabled.toString());
		config.set("kits.enabled", kitsEnabled);
		config.set("kits.linked", linkedKits);
		config.set("kits.randomLinkedKit", linkedRandom);
		config.set("punchDamage", punchDamage);
		config.set("testmode", testmode);
		config.set("excludeStats", null);
		config.set("stats.enabled", statsEnabled);
		config.set("stats.minPlayers", statsMinPlayers);
		config.set("allowDoublejumps", allowDoublejumps);
		config.set("regenerationdelay", regenerationdelay);
		config.set("joinfee", fee);
		config.set("currency", currency);
		config.set("finished", finished);
		config.set("spawnpoints", additionalSpawnPoints);
		config.set("commandOnStart", getCommandOnStart());
		config.set("commandOnStop", getCommandOnStop());
		config.set("displayfinalpositions", maxFinalPositions);
		config.set("enableOnRestart", enableOnRestart);
		config.set("shop.enabled", shopEnabled);
		rewards.saveToConfig(config);
		try {
			config.save(arena.getArenaFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void loadFromConfig() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(arena.getArenaFile());
		world = config.getString("world", null);
		p1 = config.getVector("p1", null);
		p2 = config.getVector("p2", null);
		gameleveldestroydelay = config.getInt("gameleveldestroydelay", gameleveldestroydelay);
		loselevel.loadFromConfig(config);
		playerspawn.loadFromConfig(config);
		spectatorspawn.loadFromConfig(config);
		waitingspawn.loadFromConfig(config);
		maxPlayers = config.getInt("maxPlayers", maxPlayers);
		minPlayers = config.getInt("minPlayers", minPlayers);
		votesPercent = config.getDouble("votePercent", votesPercent);
		timelimit = config.getInt("timelimit", timelimit);
		countdown = config.getInt("countdown", countdown);
		startVisibleCountdown = config.getInt("startVisibleCountdown", startVisibleCountdown);
		teleportDest = TeleportDestination.valueOf(config.getString("teleportto", TeleportDestination.PREVIOUS.toString()));
		damageEnabled = DamageEnabled.valueOf(config.getString("damageenabled", DamageEnabled.NO.toString()));
		rewards.loadFromConfig(config);
		kitsEnabled = config.getBoolean("kits.enabled");
		linkedKits = config.getStringList("kits.linked");
		linkedRandom = config.getBoolean("kits.randomLinkedKit", true);
		punchDamage = config.getBoolean("punchDamage", true);
		testmode = config.getBoolean("testmode");
		statsEnabled = excludeStats ? false : config.getBoolean("stats.enabled", true);
		statsMinPlayers = config.getInt("stats.minPlayers", statsMinPlayers);
		allowDoublejumps = config.getBoolean("allowDoublejumps", true);
		regenerationdelay = config.getInt("regenerationdelay", regenerationdelay);
		fee = config.getDouble("joinfee", fee);
		currency = config.getString("currency", null);
		finished = config.getBoolean("finished");
		if (!finished && arena.getStructureManager().isArenaConfigured()) {
			finished = true;
		}
		additionalSpawnPoints = (List<Vector>) config.getList("spawnpoints", new ArrayList<>());
		commandOnStart = config.getString("commandOnStart", "");
		commandOnStop = config.getString("commandOnStop", "");
		maxFinalPositions = config.getInt("displayfinalpositions", maxFinalPositions);
		enableOnRestart = config.getBoolean("enableOnRestart", true);
		shopEnabled = config.getBoolean("shop.enabled", true);
	}

}

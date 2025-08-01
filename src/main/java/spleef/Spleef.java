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

package spleef;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import spleef.arena.Arena;
import spleef.arena.handlers.BungeeHandler;
import spleef.arena.handlers.SoundHandler;
import spleef.arena.handlers.VaultHandler;
import spleef.commands.AutoTabCompleter;
import spleef.commands.ConsoleCommands;
import spleef.commands.GameCommands;
import spleef.commands.setup.SetupCommandsHandler;
import spleef.commands.setup.SetupTabCompleter;
import spleef.datahandler.ArenasManager;
import spleef.datahandler.PlayerDataStore;
import spleef.datahandler.ScoreboardManager;
import spleef.eventhandler.HeadsPlusHandler;
import spleef.eventhandler.MenuHandler;
import spleef.eventhandler.PlayerLeaveArenaChecker;
import spleef.eventhandler.PlayerStatusHandler;
import spleef.eventhandler.RestrictionHandler;
import spleef.eventhandler.ShopHandler;
import spleef.kits.Kits;
import spleef.lobby.GlobalLobby;
import spleef.menu.Menus;
import spleef.messages.Language;
import spleef.messages.Messages;
import spleef.parties.Parties;
import spleef.signs.SignHandler;
import spleef.signs.editor.SignEditor;
import spleef.utils.Bars;
import spleef.utils.Shop;
import spleef.utils.Sounds;
import spleef.utils.Stats;
import spleef.utils.TitleMsg;
import spleef.utils.Utils;

public class Spleef extends JavaPlugin {

	private Logger log;
	private boolean mcMMO = false;
	private boolean headsplus = false;
	private boolean usestats = false;
	private boolean needupdate = false;
	private boolean placeholderapi = false;
	private boolean adpParties = false;
	private boolean file = false;
	private VaultHandler vaultHandler;
	private BungeeHandler bungeeHandler;
	private GlobalLobby globallobby;
	private Menus menus;
	private PlayerDataStore pdata;
	private SignEditor signEditor;
	private Kits kitmanager;
	private Sounds sound;
	private Language language;
	private Parties parties;
	private Stats stats;
	private MySQL mysql;
	private Shop shop;

	public ArenasManager amanager;
	private ScoreboardManager scoreboardManager;

	private static Spleef instance;
	private String version;
	private String latestRelease;
	private static final int SPIGOT_ID = 118673;
	private static final int BSTATS_PLUGIN_ID = 22271;
	private static final String SPIGOT_URL = "https://www.spigotmc.org/resources/Spleef_reloaded." + SPIGOT_ID + "/";

	@Override
	public void onEnable() {
		instance = this;
		log = getLogger();

		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();

		signEditor = new SignEditor(this);
		globallobby = new GlobalLobby(this);
		kitmanager = new Kits();
		language = new Language(this);
		Messages.loadMessages(this);
		Bars.loadBars(this);
		TitleMsg.loadTitles(this);
		pdata = new PlayerDataStore(this);
		amanager = new ArenasManager();
		menus = new Menus(this);
		parties = new Parties(this);

		version = getDescription().getVersion();
		setupPlugin();
		setupScoreboards();

		loadArenas();
		checkUpdate();
		sound = new SoundHandler(this);

		if (isBungeecord()) {
			log.info("Bungeecord is enabled");
			bungeeHandler = new BungeeHandler(this);
		}

		if (getConfig().getBoolean("special.Metrics", true)) {
			log.info("Attempting to start metrics (bStats)...");
			Metrics metrics = new Metrics(this, BSTATS_PLUGIN_ID);
			metrics.addCustomChart(new SimplePie("server_software", () -> {
				return getServer().getName() == "CraftBukkit" ? "Spigot" : getServer().getName();
			}));
		}

		setStorage();
		if (usestats) {
			stats = new Stats(this);
		}
	}

	public static Spleef getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		if (!file) {
			mysql.close();
		}
		saveArenas();
		globallobby.saveToConfig();
		globallobby = null;

		kitmanager.saveToConfig();
		kitmanager = null;

		signEditor.saveConfiguration();
		signEditor = null;

		amanager = null;
		pdata = null;
		stats = null;
		log = null;
	}

	private void saveArenas() {
		for (Arena arena : amanager.getArenas()) {
			arena.getStructureManager().getGameZone().regenNow();
			arena.getStatusManager().disableArena();
			arena.getStructureManager().saveToConfig();
			Bars.removeAll(arena.getArenaName());
		}
	}

	public void logSevere(String message) {
		log.severe(message);
	}

	public boolean isHeadsPlus() {
		return headsplus;
	}

	public boolean isMCMMO() {
		return mcMMO;
	}

	public boolean isPlaceholderAPI() {
		return placeholderapi;
	}

	public boolean isParties() {
		return getConfig().getBoolean("parties.enabled");
	}

	public boolean isAdpParties() {
		return adpParties && getConfig().getBoolean("parties.usePartiesPlugin") && !isParties();
	}

	public boolean useStats() {
		return usestats;
	}

	public void setUseStats(boolean usestats) {
		this.usestats = usestats;
	}

	public boolean needUpdate() {
		return needupdate;
	}

	public void setNeedUpdate(boolean needupdate) {
		this.needupdate = needupdate;
	}

	public boolean isFile() {
		return file;
	}

	public boolean useUuid() {
		return Bukkit.getOnlineMode() || (isBungeecord() && getConfig().getBoolean("bungeecord.useUUID"));
	}

	public boolean isBungeecord() {
		return getConfig().getBoolean("bungeecord.enabled");
	}

	private void checkUpdate() {
		if (!getConfig().getBoolean("special.CheckForNewVersion", true)) {
			return;
		}
		new VersionChecker(this, SPIGOT_ID).getVersion(latestVersion -> {
			latestRelease = latestVersion;
			if (version.equals(latestVersion)) {
				log.info("You are running the most recent version");
				setNeedUpdate(false);

			} else if (version.contains("beta") || version.toLowerCase().contains("snapshot")) {
				log.info("You are running dev build: " + version);
				log.info("Latest release: " + latestVersion);
				setNeedUpdate(false);

			} else if (Character.isDigit(latestVersion.charAt(0))) {
				log.info("Current version: " + version);
				log.info("Latest release: " + latestVersion);
				log.info("Latest release available from Spigot: " + SPIGOT_URL);
				setNeedUpdate(true);
				for (Player p : Bukkit.getOnlinePlayers()) {
					Utils.displayUpdate(p);
				}
			}
		});
	}

	private void connectToMySQL() {
		log.info("Connecting to MySQL database...");
		mysql = new MySQL(getConfig().getString("MySQL.host"),
				getConfig().getInt("MySQL.port"),
				getConfig().getString("MySQL.name"),
				getConfig().getString("MySQL.user"),
				getConfig().getString("MySQL.pass"),
				getConfig().getString("MySQL.useSSL"),
				getConfig().getString("MySQL.flags"),
				getConfig().getBoolean("MySQL.legacyDriver"),
				this);

		new BukkitRunnable() {
			@Override
			public void run() {

				mysql.query("CREATE TABLE IF NOT EXISTS `" + getConfig().getString("MySQL.table") + "` ( `username` varchar(50) NOT NULL, "
						+ "`streak` int(16) NOT NULL, `wins` int(16) NOT NULL, "
						+ "`played` int(16) NOT NULL, "
						+ "UNIQUE KEY `username` (`username`) ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

				mysql.query("ALTER TABLE `" + getConfig().getString("MySQL.table") + "` RENAME COLUMN `looses` TO `streak`");

				log.info("Connected to MySQL database!");
			}
		}.runTaskAsynchronously(this);
	}

	private void setupPlugin() {
		getCommand("spleef").setExecutor(new GameCommands(this));
		getCommand("spleefsetup").setExecutor(new SetupCommandsHandler(this));
		getCommand("spleefconsole").setExecutor(new ConsoleCommands(this));
		getCommand("spleef").setTabCompleter(new AutoTabCompleter());
		getCommand("spleefsetup").setTabCompleter(new SetupTabCompleter());

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerStatusHandler(this), this);
		pm.registerEvents(new RestrictionHandler(this), this);
		pm.registerEvents(new PlayerLeaveArenaChecker(this), this);
		pm.registerEvents(new SignHandler(this), this);
		pm.registerEvents(new MenuHandler(this), this);
		pm.registerEvents(new ShopHandler(this), this);

		setupShop();

		Plugin HeadsPlus = pm.getPlugin("HeadsPlus");
		if (HeadsPlus != null && HeadsPlus.isEnabled()) {
			pm.registerEvents(new HeadsPlusHandler(this), this);
			headsplus = true;
			log.info("Successfully linked with HeadsPlus, version " + HeadsPlus.getDescription().getVersion());
		}
		Plugin MCMMO = pm.getPlugin("mcMMO");
		if (MCMMO != null && MCMMO.isEnabled()) {
			mcMMO = true;
			log.info("Successfully linked with mcMMO, version " + MCMMO.getDescription().getVersion());
		}
		Plugin PlaceholderAPI = pm.getPlugin("PlaceholderAPI");
		if (PlaceholderAPI != null && PlaceholderAPI.isEnabled()) {
			placeholderapi = true;
			log.info("Successfully linked with PlaceholderAPI, version " + PlaceholderAPI.getDescription().getVersion());
			new SpleefPlaceholders(this).register();
		}
		Plugin Parties = getServer().getPluginManager().getPlugin("Parties");
		if (Parties != null && Parties.isEnabled()) {
			adpParties = true;
			log.info("Successfully linked with Parties, version " + Parties.getDescription().getVersion());
		}

		vaultHandler = new VaultHandler(this);
	}

	public void setupShop() {
		if (isGlobalShop()) {
			shop = new Shop(this);
		}
	}

	public boolean isGlobalShop() {
		return getConfig().getBoolean("shop.enabled");
	}

	public VaultHandler getVaultHandler() {
		return vaultHandler;
	}

	public BungeeHandler getBungeeHandler() {
		return bungeeHandler;
	}

	public Parties getParties() {
		return parties;
	}

	private void loadArenas() {
		final File arenasfolder = new File(getDataFolder() + File.separator + "arenas");
		arenasfolder.mkdirs();
		new BukkitRunnable() {

			@Override
			public void run() {
				globallobby.loadFromConfig();
				kitmanager.loadFromConfig();

				List<String> arenaList = Arrays.asList(arenasfolder.list());
				for (String file : arenaList) {
					Arena arena = new Arena(file.substring(0, file.length() - 4), instance);
					arena.getStructureManager().loadFromConfig();
					amanager.registerArena(arena);
					Bars.createBar(arena.getArenaName());
					if (arena.getStructureManager().isEnableOnRestart()) {
						arena.getStatusManager().enableArena();
					}
				}
				if (isBungeecord()) {
					amanager.setBungeeArena();
				}

				signEditor.loadConfiguration();
			}
		}.runTaskLater(this, 20L);
	}

	private void setStorage() {
		String storage = this.getConfig().getString("database");
		switch (storage) {
			case "file" -> {
				usestats = true;
				file = true;
			}
			case "sql", "mysql" -> {
				this.connectToMySQL();
				usestats = true;
				file = false;
			}
			default -> {
				log.info("The database " + storage + " is not supported, supported database types: sql, mysql, file");
				usestats = false;
				file = false;
				log.info("Disabling stats...");
			}
		}
	}

	public Menus getMenus() {
		return menus;
	}

	public PlayerDataStore getPData() {
		return pdata;
	}

	public Stats getStats() {
		return stats;
	}

	public Kits getKitManager() {
		return kitmanager;
	}

	public GlobalLobby getGlobalLobby() {
		return globallobby;
	}

	public Sounds getSound() {
		return sound;
	}

	public Language getLanguage() {
		return language;
	}

	public SignEditor getSignEditor() {
		return signEditor;
	}

	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	public MySQL getMysql() {
		return mysql;
	}

	public Shop getShop() {
		return shop;
	}

	public String getLatestRelease() {
		return latestRelease;
	}

	public String getSpigotURL() {
		return SPIGOT_URL;
	}

	public void setupScoreboards() {
		if (getConfig().getBoolean("special.UseScoreboard")) {
			scoreboardManager = new ScoreboardManager(this);
		}
	}
}

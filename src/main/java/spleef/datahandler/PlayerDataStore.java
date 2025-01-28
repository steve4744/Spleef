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

package spleef.datahandler;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import spleef.Spleef;

public class PlayerDataStore {

	private Map<String, ItemStack[]> plinv = new HashMap<>();
	private Map<String, ItemStack[]> plarmor = new HashMap<>();
	private Map<String, Collection<PotionEffect>> pleffects = new HashMap<>();
	private Map<String, Location> plloc = new HashMap<>();
	private Map<String, Integer> plhunger = new HashMap<>();
	private Map<String, GameMode> plgamemode = new HashMap<>();
	private Map<String, Integer> pllevel = new HashMap<>();
	private Map<String, Double> plhealth = new HashMap<>();
	private Map<String, Boolean> plflight = new HashMap<>();
	private File file;
	private final Spleef plugin;

	public PlayerDataStore(Spleef plugin) {
		this.plugin = plugin;
		file = new File(plugin.getDataFolder(), "players.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void storePlayerInventory(Player player) {
		PlayerInventory pinv = player.getInventory();
		plinv.put(player.getName(), pinv.getContents());
		pinv.clear();
	}

	public void storePlayerFlight(Player player) {
		plflight.put(player.getName(), player.getAllowFlight());
	}

	public void storePlayerArmor(Player player) {
		PlayerInventory pinv = player.getInventory();
		plarmor.put(player.getName(), pinv.getArmorContents());
		pinv.setArmorContents(null);
	}

	public void storePlayerPotionEffects(Player player) {
		Collection<PotionEffect> peff = player.getActivePotionEffects();
		pleffects.put(player.getName(), peff);
		for (PotionEffect peffect : peff) {
			player.removePotionEffect(peffect.getType());
		}
	}

	public void storePlayerLocation(Player player) {
		plloc.put(player.getName(), player.getLocation());
	}

	public void storePlayerHunger(Player player) {
		plhunger.put(player.getName(), player.getFoodLevel());
		if (plugin.getConfig().getBoolean("onjoin.fillhunger")) {
			player.setFoodLevel(20);
		}
	}

	public void storePlayerGameMode(Player player) {
		plgamemode.put(player.getName(), player.getGameMode());
		player.setGameMode(getGameMode());
	}

	public void storePlayerLevel(Player player) {
		pllevel.put(player.getName(), player.getLevel());
		player.setLevel(0);
	}

	public void storePlayerHealth(Player player) {
		LivingEntity le = (LivingEntity) player;
		plhealth.put(player.getName(), le.getHealth());
		if (plugin.getConfig().getBoolean("onjoin.fillhealth")) {
			le.setHealth(le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		}
	}

	public void restorePlayerInventory(Player player) {
		player.getInventory().setContents(plinv.remove(player.getName()));
	}

	public void restorePlayerArmor(Player player) {
		player.getInventory().setArmorContents(plarmor.remove(player.getName()));
	}

	public void restorePlayerPotionEffects(Player player) {
		player.addPotionEffects(pleffects.remove(player.getName()));
	}

	public void restorePlayerLocation(Player player) {
		player.teleport(plloc.remove(player.getName()));
	}
	
	public void restorePlayerFlight(Player player) {
		player.setAllowFlight(plflight.get(player.getName()));
	}

	public void clearPlayerLocation(Player player) {
		plloc.remove(player.getName());
	}

	public void restorePlayerHunger(Player player) {
		player.setFoodLevel(plhunger.remove(player.getName()));
	}

	public void restorePlayerGameMode(Player player) {
		player.setGameMode(plgamemode.remove(player.getName()));
	}

	public void restorePlayerLevel(Player player) {
		player.setLevel(pllevel.remove(player.getName()));
	}

	public void restorePlayerHealth(Player player) {
		LivingEntity le = (LivingEntity) player;
		le.setHealth(Math.min(le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), plhealth.remove(player.getName())));
	}

	public void saveDoubleJumpsToFile(OfflinePlayer player, int amount) {
		String uuid = plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
		saveConfigFile(uuid, ".doublejumps", amount);
	}

	public void setWinStreak(OfflinePlayer player, int amount) {
		String uuid = plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
		saveConfigFile(uuid, ".winstreak", amount);
	}

	private void saveConfigFile(String uuid, String path, int amount) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (amount == 0) {
			config.set(uuid + path, null);
			ConfigurationSection section = config.getConfigurationSection(uuid);
			if (section != null && section.getKeys(false).isEmpty()) {
				config.set(uuid, null);
			}

		} else {
			config.set(uuid + path, amount);
		}
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getDoubleJumpsFromFile(OfflinePlayer player) {
		String uuid = plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
		return getDoubleJumpsFromFile(uuid);
	}

	public int getDoubleJumpsFromFile(String name) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config.getInt(name + ".doublejumps", 0);
	}

	public int getWinStreak(OfflinePlayer player) {
		String uuid = plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
		return getWinStreakFromFile(uuid);
	}

	private int getWinStreakFromFile(String name) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config.getInt(name + ".winstreak", 0);
	}

	public boolean hasStoredDoubleJumps(Player player) {
		return getDoubleJumpsFromFile(player) > 0;
	}

	private GameMode getGameMode() {
		String gamemode = "SURVIVAL";
		/*String gamemode = plugin.getConfig().getString("gamemode", "SURVIVAL");
		if (!gamemode.equalsIgnoreCase("ADVENTURE")) {
			gamemode = "SURVIVAL";
		}*/
		return GameMode.valueOf(gamemode.toUpperCase());
	}
}

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

package spleef.arena;

import java.io.File;

import spleef.Spleef;
import spleef.arena.handlers.ArenaEconomy;
import spleef.arena.handlers.GameHandler;
import spleef.arena.handlers.PlayerHandler;
import spleef.arena.handlers.ScoreboardHandler;
import spleef.arena.status.PlayersManager;
import spleef.arena.status.StatusManager;
import spleef.arena.structure.StructureManager;
import spleef.eventhandler.SnowballHandler;

public class Arena {

	public Spleef plugin;
	private String arenaname;
	private ArenaEconomy arenaeco;
	private SnowballHandler snowballHandler;

	public Arena(String name, Spleef plugin) {
		arenaname = name;
		this.plugin = plugin;
		arenagh = new GameHandler(plugin, this);
		arenaph = new PlayerHandler(plugin, this);
		arenafile = new File(plugin.getDataFolder() + File.separator + "arenas" + File.separator + arenaname + ".yml");
		arenasb = new ScoreboardHandler(plugin, this);
		arenaeco = new ArenaEconomy(plugin, this);
		snowballHandler = new SnowballHandler(plugin, this);
	}

	public String getArenaName() {
		return arenaname;
	}

	private File arenafile;
	public File getArenaFile() {
		return arenafile;
	}

	private GameHandler arenagh;
	public GameHandler getGameHandler() {
		return arenagh;
	}

	private PlayerHandler arenaph;
	public PlayerHandler getPlayerHandler() {
		return arenaph;
	}

	private ScoreboardHandler arenasb;
	public ScoreboardHandler getScoreboardHandler() {
		return arenasb;
	}

	private StatusManager statusManager = new StatusManager(this);
	public StatusManager getStatusManager() {
		return statusManager;
	}

	private StructureManager structureManager = new StructureManager(this);
	public StructureManager getStructureManager() {
		return structureManager;
	}

	private PlayersManager playersManager = new PlayersManager();
	public PlayersManager getPlayersManager() {
		return playersManager;
	}

	public ArenaEconomy getArenaEconomy() {
		return arenaeco;
	}

	public SnowballHandler getSnowballHandler() {
		return snowballHandler;
	}
}

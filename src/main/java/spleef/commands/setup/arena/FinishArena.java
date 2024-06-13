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

package spleef.commands.setup.arena;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;
import spleef.utils.Bars;
import spleef.utils.Utils;

public class FinishArena implements CommandHandlerInterface {

	private Spleef plugin;
	public FinishArena(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena != null) {
			if (!arena.getStatusManager().isArenaEnabled()) {
				if (arena.getStructureManager().isArenaConfigured()) {
					arena.getStructureManager().setArenaFinished(true);
					arena.getStructureManager().saveToConfig();
					arena.getStatusManager().enableArena();
					Bars.createBar(args[0]);
					Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 saved and enabled");
					if (Utils.debug()) {
						plugin.getLogger().info("Arena " + args[0] + " finished successfully");
					}
				} else {
					Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c isn't configured. Reason: &6" + arena.getStructureManager().isArenaConfiguredString());
					if (Utils.debug()) {
						plugin.getLogger().info("Arena " + args[0] + " finish failed: " + arena.getStructureManager().isArenaConfiguredString());
					}
				}
			} else {
				Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
			}
		} else {
			Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
		}
		return true;
	}

	@Override
	public int getMinArgsLength() {
		return 1;
	}

}
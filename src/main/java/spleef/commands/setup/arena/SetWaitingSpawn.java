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

public class SetWaitingSpawn implements CommandHandlerInterface {

	private Spleef plugin;
	public SetWaitingSpawn(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena != null) {
			if (arena.getStatusManager().isArenaEnabled()) {
				Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
				return true;
			}
			if (!arena.getStructureManager().isArenaBoundsSet()) {
				Messages.sendMessage(player, Messages.arenanobounds);
				return true;
			}
			if (arena.getStructureManager().setWaitingSpawn(player.getLocation())) {
				Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 Waiting spawn set to &6X: &7" +
						Math.round(player.getLocation().getX()) + " &6Y: &7" +
						Math.round(player.getLocation().getY()) + " &6Z: &7" +
						Math.round(player.getLocation().getZ()));
			} else {
				Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c Waiting spawn must be inside arena bounds");
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

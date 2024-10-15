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
import spleef.utils.Utils;

public class SetSpawn implements CommandHandlerInterface {

	private Spleef plugin;

	public SetSpawn(Spleef plugin) {
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
			if (arena.getStructureManager().setSpawnPoint(player.getLocation())) {
				Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 spawn point set to &6X: &7" +
						Math.round(player.getLocation().getX()) + " &6Y: &7" +
						Math.round(player.getLocation().getY()) + " &6Z: &7" +
						Math.round(player.getLocation().getZ()));
			} else {
				Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c spawn point must be in arena bounds");
			}
			if (Utils.debug()) {
				plugin.getLogger().info("Arena " + arena.getArenaName() + " spawnpoint: " + player.getLocation().toVector().toString());
				plugin.getLogger().info("Min point: " + arena.getStructureManager().getP1().toString());
				plugin.getLogger().info("Max point: " + arena.getStructureManager().getP2().toString());
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
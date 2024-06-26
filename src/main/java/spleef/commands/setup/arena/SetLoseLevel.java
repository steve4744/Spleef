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

import java.text.DecimalFormat;
import java.util.StringJoiner;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;

public class SetLoseLevel implements CommandHandlerInterface {

	private Spleef plugin;

	public SetLoseLevel(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena == null) {
			Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
			return true;
		}
		if (arena.getStatusManager().isArenaEnabled()) {
			Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
			return true;
		}
		if (arena.getStructureManager().getWorldName() == null) {
			Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c bounds are wrong");
			return true;
		}

		Location loc = player.getLocation();
		if (arena.getStructureManager().setLoseLevel(loc)) {
			Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 loselevel set:\n&6Y = " + loc.getY());
		} else {
			Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c error: loselevel (&6" + displayLocation(loc) + "&c) is not within the bounds of the arena:\n" +
					"P1 = &6" + arena.getStructureManager().getP1() + "\n&cP2 = &6" + arena.getStructureManager().getP2());
		}
		return true;
	}

	private String displayLocation(Location loc) {
		StringJoiner msg = new StringJoiner(", ");
		DecimalFormat df = new DecimalFormat("#.#");
		msg.add(df.format(loc.getX())).add(df.format(loc.getY())).add(df.format(loc.getZ()));
		return msg.toString();
	}

	@Override
	public int getMinArgsLength() {
		return 1;
	}

}
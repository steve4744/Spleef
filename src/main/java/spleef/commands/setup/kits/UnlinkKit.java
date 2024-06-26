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

package spleef.commands.setup.kits;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;

public class UnlinkKit implements CommandHandlerInterface {

	private Spleef plugin;

	public UnlinkKit(Spleef plugin) {
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
		if (!plugin.getKitManager().kitExists(args[1]) && !arena.getStructureManager().getLinkedKits().contains(args[1])) {
			Messages.sendMessage(player, Messages.kitnotexists.replace("{KIT}", args[1]));
			return true;
		}
		arena.getStructureManager().unlinkKit(args[1]);
		Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 is no longer linked to kit &6" + args[1]);
		return true;
	}

	@Override
	public int getMinArgsLength() {
		return 2;
	}
}

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
package spleef.commands.setup.other;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;
import spleef.utils.Utils;

public class ResetCachedRank implements CommandHandlerInterface {

	private Spleef plugin;

	public ResetCachedRank(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		if (!plugin.getConfig().getBoolean("UseRankInChat.enabled") && plugin.getConfig().getBoolean("UseRankInChat.usegroup")) {
			Messages.sendMessage(player, "&7 Caching of ranks is not currently enabled");
			return false;
		}
		Utils.removeRankFromCache(args[0]);
		Messages.sendMessage(player, "&7 Cached rank cleared for player: &6" + args[0]);
		return true;
	}

	@Override
	public int getMinArgsLength() {
		return 1;
	}
}

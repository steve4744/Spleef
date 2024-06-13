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

package spleef.signs;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;

import spleef.Spleef;
import spleef.messages.Messages;
import spleef.signs.type.AutoJoinSign;
import spleef.signs.type.JoinSign;
import spleef.signs.type.LeaderboardSign;
import spleef.signs.type.LeaveSign;
import spleef.signs.type.LobbySign;
import spleef.signs.type.SignType;
import spleef.signs.type.VoteSign;
import spleef.utils.FormattingCodesParser;

public class SignHandler implements Listener {

	private HashMap<String, SignType> signs = new HashMap<>();

	private Spleef plugin;

	public SignHandler(Spleef plugin) {
		signs.put("[join]", new JoinSign(plugin));
		signs.put("[leave]", new LeaveSign(plugin));
		signs.put("[vote]", new VoteSign(plugin));
		signs.put("[lobby]", new LobbySign(plugin));
		signs.put("[autojoin]", new AutoJoinSign(plugin));
		signs.put("[leaderboard]", new LeaderboardSign(plugin));

		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSpleefSignCreate(SignChangeEvent e) {
		Player player = e.getPlayer();
		if (ChatColor.stripColor(e.getLine(0)).equalsIgnoreCase("[Spleef]")) {
			if (!player.hasPermission("spleef.setup")) {
				Messages.sendMessage(player, Messages.nopermission);
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				return;
			}	
			String line = e.getLine(1).toLowerCase();
			if (signs.containsKey(line)) {
				signs.get(line).handleCreation(e);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (!(e.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		Sign sign = (Sign) e.getClickedBlock().getState();
		if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase(
				FormattingCodesParser.parseFormattingCodes(plugin.getConfig().getString("signs.prefix")))) {

			String line = ChatColor.stripColor(sign.getSide(Side.FRONT).getLine(1).toLowerCase());
			if (line.equalsIgnoreCase(ChatColor.stripColor(
					FormattingCodesParser.parseFormattingCodes(plugin.getConfig().getString("signs.join"))))) {
				line = "[join]";
			}
			if (signs.containsKey(line)) {
				signs.get(line).handleClick(e);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignDestroy(BlockBreakEvent e) {
		if (!(e.getBlock().getState() instanceof Sign)) {
			return;
		}
		Player player = e.getPlayer();
		Sign sign = (Sign) e.getBlock().getState();
		if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase(
				FormattingCodesParser.parseFormattingCodes(plugin.getConfig().getString("signs.prefix")))) {

			if (!player.hasPermission("spleef.setup")) {
				Messages.sendMessage(player, Messages.nopermission);
				e.setCancelled(true);
				return;
			}
			String line = sign.getSide(Side.FRONT).getLine(1).toLowerCase();
			if (line.equalsIgnoreCase(FormattingCodesParser.parseFormattingCodes(plugin.getConfig().getString("signs.join")))) {
				line = "[join]";
			}
			if (signs.containsKey(line)) {
				signs.get(line).handleDestroy(e);
			} else {
				// at this point it must be a Spleef leaderboard sign
				signs.get("[leaderboard]").handleDestroy(e);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignEdit(PlayerSignOpenEvent e) {
		Sign sign = e.getSign();
		if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase(
				FormattingCodesParser.parseFormattingCodes(plugin.getConfig().getString("signs.prefix")))) {
			e.setCancelled(true);
		}
	}

}

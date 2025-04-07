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

package spleef.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import spleef.Spleef;

public class AutoTabCompleter implements TabCompleter {

	private static final List<String> COMMANDS = Arrays.asList(
			"help", "lobby", "list", "join", "leave", "vote", "info", "stats", "listkits", "autojoin", "leaderboard", "party");

	private static final List<String> PARTY_COMMANDS = Arrays.asList(
			"accept", "create", "decline", "info", "invite", "kick", "leave", "unkick");

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spleef") || cmd.getName().equalsIgnoreCase("sp")) {
			if (!(sender instanceof Player)) {
				return null;
			}

			List<String> list = new ArrayList<>();
			List<String> auto = new ArrayList<>();

			if (args.length == 1) {
				list.addAll(COMMANDS);

				if (sender.hasPermission("spleef.setup")) {
					list.add("cmds");
				}
				if (sender.hasPermission("spleef.start")) {
					list.add("start");
				}
				if (sender.hasPermission("spleef.spectate")) {
					list.add("spectate");
				}
				if (sender.hasPermission("spleef.listrewards")) {
					list.add("listrewards");
				}

			} else if (args.length == 2) {
				if (Stream.of("join", "list", "start", "spectate", "listrewards").anyMatch(s -> s.equalsIgnoreCase(args[0]))) {
					list.addAll(Spleef.getInstance().amanager.getArenasNames());

				} else if (args[0].equalsIgnoreCase("party")) {
					list.addAll(PARTY_COMMANDS);

				} else if (args[0].equalsIgnoreCase("listkits") || args[0].equalsIgnoreCase("listkit")) {
					list.addAll(Spleef.getInstance().getKitManager().getKits());

				} else if (args[0].equalsIgnoreCase("autojoin")) {
					list.add("nopvp");
					list.add("pvp");
				}

			} else if (args.length == 3 && args[0].equalsIgnoreCase("party")) {
				if (Stream.of("invite", "unkick", "accept", "decline").anyMatch(s -> s.equalsIgnoreCase(args[1]))) {
					list.addAll(getOnlinePlayerNames());

				} else if (args[1].equalsIgnoreCase("kick")) {
					list.addAll(Spleef.getInstance().getParties().getPartyMembers(sender.getName()));
				}
			}
			for (String s : list) {
				if (s.startsWith(args[args.length - 1])) {
					auto.add(s);
				}
			}
			return auto.isEmpty() ? list : auto;

		}
		return null;
	}

	private List<String> getOnlinePlayerNames() {
		return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
	}
}

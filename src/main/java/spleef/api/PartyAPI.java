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
package spleef.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import spleef.Spleef;

public class PartyAPI {

	private static final String[] CREATE = {"party", "create"};
	private static final String[] LEAVE = {"party", "leave"};

	public static void createParty(Player player) {
		Spleef.getInstance().getParties().handleCommand(player, CREATE);
	}

	public static void leaveParty(Player player) {
		Spleef.getInstance().getParties().handleCommand(player, LEAVE);
	}

	public static void joinParty(Player player, String target) {
		List<String> join = new ArrayList<>(List.of("party", "accept", target));
		Spleef.getInstance().getParties().handleCommand(player, join.toArray(String[]::new));
	}

	public static void inviteToParty(Player player, String target) {
		List<String> invite = new ArrayList<>(List.of("party", "invite", target));
		Spleef.getInstance().getParties().handleCommand(player, invite.toArray(String[]::new));
	}
}

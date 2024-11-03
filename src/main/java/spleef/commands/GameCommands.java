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

import java.util.List;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.messages.Messages;
import spleef.utils.Utils;

public class GameCommands implements CommandExecutor {

	private Spleef plugin;

	public GameCommands(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Messages.sendMessage(sender, "&c You must be a player");
			return false;
		}
		Player player = (Player) sender;
		if (args.length < 1) {
			Messages.sendMessage(player, "&7============" + Messages.spprefix + "============", false);
			Messages.sendMessage(player, "&c Please use &6/sp help");
			return false;
		}
		// help command
		if (args[0].equalsIgnoreCase("help")) {
			Messages.sendMessage(player, "&7============" + Messages.spprefix + "============", false);
			player.spigot().sendMessage(Utils.getTextComponent("/sp lobby", true), Utils.getTextComponent(Messages.helplobby));
			player.spigot().sendMessage(Utils.getTextComponent("/sp list [arena]", true), Utils.getTextComponent(Messages.helplist));
			player.spigot().sendMessage(Utils.getTextComponent("/sp join [arena]", true), Utils.getTextComponent(Messages.helpjoin));
			player.spigot().sendMessage(Utils.getTextComponent("/sp spectate {arena}", true), Utils.getTextComponent(Messages.helpspectate));
			player.spigot().sendMessage(Utils.getTextComponent("/sp autojoin [pvp|nopvp]", true), Utils.getTextComponent(Messages.helpautojoin));
			player.spigot().sendMessage(Utils.getTextComponent("/sp leave", true), Utils.getTextComponent(Messages.helpleave));
			player.spigot().sendMessage(Utils.getTextComponent("/sp vote", true), Utils.getTextComponent(Messages.helpvote));
			player.spigot().sendMessage(Utils.getTextComponent("/sp info", true), Utils.getTextComponent(Messages.helpinfo));
			player.spigot().sendMessage(Utils.getTextComponent("/sp stats", true), Utils.getTextComponent(Messages.helpstats));
			player.spigot().sendMessage(Utils.getTextComponent("/sp leaderboard [size]", true), Utils.getTextComponent(Messages.helplb));
			player.spigot().sendMessage(Utils.getTextComponent("/sp listkit [kit]", true), Utils.getTextComponent(Messages.helplistkit));
			player.spigot().sendMessage(Utils.getTextComponent("/sp listrewards {arena}", true), Utils.getTextComponent(Messages.helplistrewards));
			player.spigot().sendMessage(Utils.getTextComponent("/sp start {arena}", true), Utils.getTextComponent(Messages.helpstart));
			player.spigot().sendMessage(Utils.getTextComponent("/sp cmds", true), Utils.getTextComponent(Messages.helpcmds));

		} else if (args[0].equalsIgnoreCase("lobby")) {
			plugin.getGlobalLobby().joinLobby(player);
		}

		// list arenas
		else if (args[0].equalsIgnoreCase("list")) {
			if (args.length >= 2) {
				Arena arena = plugin.amanager.getArenaByName(args[1]);
				if (arena == null) {
					Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
					return false;
				}
				//list arena details
				Messages.sendMessage(player, "&7============" + Messages.spprefix + "============", false);
				Messages.sendMessage(player, "&7Arena Details: &a" + arena.getArenaName(), false);

				String arenaStatus = arena.getStatusManager().isArenaEnabled() ? "Enabled" : "Disabled";
				String bigspace = ChatColor.BOLD + " ";
				player.sendMessage(ChatColor.GOLD + "Status " + ChatColor.DARK_GRAY + "............................ " + ChatColor.RED + arenaStatus);
				player.sendMessage(ChatColor.GOLD + "Min Players " + ChatColor.DARK_GRAY + "..............." + bigspace + ChatColor.RED + arena.getStructureManager().getMinPlayers());
				player.sendMessage(ChatColor.GOLD + "Max Players " + ChatColor.DARK_GRAY + "............." + bigspace + ChatColor.RED + arena.getStructureManager().getMaxPlayers());
				player.sendMessage(ChatColor.GOLD + "Time Limit " + ChatColor.DARK_GRAY + "...................... " + ChatColor.RED + arena.getStructureManager().getTimeLimit() + " seconds");
				player.sendMessage(ChatColor.GOLD + "Countdown " + ChatColor.DARK_GRAY + ".................. " + ChatColor.RED + arena.getStructureManager().getCountdown() + " seconds");
				player.sendMessage(ChatColor.GOLD + "Teleport to " + ChatColor.DARK_GRAY + "................ " + ChatColor.RED + Utils.getTitleCase(arena.getStructureManager().getTeleportDestination().toString()));
				player.sendMessage(ChatColor.GOLD + "Player Count " + ChatColor.DARK_GRAY + "..........." + bigspace + ChatColor.RED + arena.getPlayersManager().getPlayersCount());
				player.sendMessage(ChatColor.GOLD + "Vote Percent " + ChatColor.DARK_GRAY + "........... " + ChatColor.RED + arena.getStructureManager().getVotePercent());
				player.sendMessage(ChatColor.GOLD + "PVP Damage " + ChatColor.DARK_GRAY + "............... " + ChatColor.RED + Utils.getTitleCase(arena.getStructureManager().getDamageEnabled().toString()));

				String result = arena.getStructureManager().isKitsEnabled() ? "Yes" : "No";
				player.sendMessage(ChatColor.GOLD + "Kits Enabled " + ChatColor.DARK_GRAY + "............." + bigspace + ChatColor.RED + result);

				List<String> kitnames = arena.getStructureManager().getLinkedKits();
				if (kitnames.size() > 0) {
					player.sendMessage(ChatColor.GOLD + "Linked Kits " + ChatColor.DARK_GRAY + "................." + bigspace + ChatColor.RED + String.join(", ", kitnames));
				}

				player.sendMessage(ChatColor.GOLD + "Rewards " + ChatColor.DARK_GRAY + "....................... " + ChatColor.RED + "Use command '/sp listrewards {arena}'");

				if (arena.getStructureManager().getFee() > 0) {
					player.sendMessage(ChatColor.GOLD + "Join Fee " + ChatColor.DARK_GRAY + "....................... " + ChatColor.RED + arena.getStructureManager().getArenaCost());
				}

				if (arena.getStructureManager().isTestMode()) {
					player.sendMessage(ChatColor.GOLD + "Test Mode " + ChatColor.DARK_GRAY + "..................." + bigspace + ChatColor.RED + "Enabled");
				}
				if (arena.getStructureManager().isArenaStatsEnabled()) {
					player.sendMessage(ChatColor.GOLD + "Arena Stats " + ChatColor.DARK_GRAY + ".............." + bigspace + ChatColor.RED + "Enabled");
				}
				return true;
			}

			int arenacount = plugin.amanager.getArenas().size();
			Messages.sendMessage(player, Messages.availablearenas.replace("{COUNT}", String.valueOf(arenacount)));
			if (arenacount == 0) {
				return false;
			}
			StringJoiner message = new StringJoiner(" : ");
			plugin.amanager.getArenasNames().stream().sorted().forEach(arenaname -> {
				if (plugin.amanager.getArenaByName(arenaname).getStatusManager().isArenaEnabled()) {
					message.add("&a" + arenaname);
				} else {
					message.add("&c" + arenaname + "&a");
				}
			});
			Messages.sendMessage(player, message.toString(), false);
		}

		// join arena
		else if (args[0].equalsIgnoreCase("join")) {
			if (args.length == 1 && player.hasPermission("spleef.joinmenu")) {
				plugin.getMenus().buildJoinMenu(player);
				return true;
			}
			if (args.length != 2) {
				Messages.sendMessage(player, "&c Invalid number of arguments supplied");
				return false;
			}
			Arena arena = plugin.amanager.getArenaByName(args[1]);
			if (arena == null) {
				Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
				return false;
			}
			if (arena.getPlayerHandler().checkJoin(player)) {
				arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
			}
		}

		// join or spectate called from invitation message
		else if (args[0].equalsIgnoreCase("joinorspectate")) {
			if (args.length != 2) {
				return false;
			}
			Arena arena = plugin.amanager.getArenaByName(args[1]);
			if (arena == null) {
				return false;
			}
			if (!arena.getStatusManager().isArenaRunning()) {
				if (arena.getPlayerHandler().checkJoin(player)) {
					arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
				}
			} else {
				if (!plugin.getConfig().getBoolean("invitationmessage.allowspectate")) {
					Messages.sendMessage(player, arena.getStatusManager().getFormattedMessage(Messages.arenarunning));
					return false;
				}
				if (!arena.getPlayerHandler().canSpectate(player)) {
					return false;
				}
				arena.getPlayerHandler().spectatePlayer(player, Messages.playerjoinedasspectator, "");
				if (Utils.debug()) {
					plugin.getLogger().info("Player " + player.getName() + " joined arena " + arena.getArenaName() + " as a spectator");
				}
			}
		}

		// spectate arena
		else if (args[0].equalsIgnoreCase("spectate")) {
			if (args.length != 2) {
				Messages.sendMessage(player, "&c Invalid number of arguments supplied");
				return false;
			}
			Arena arena = plugin.amanager.getArenaByName(args[1]);
			if (arena == null) {
				Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
				return false;
			}
			if (!arena.getPlayerHandler().canSpectate(player)) {
				return false;
			}
			arena.getPlayerHandler().spectatePlayer(player, Messages.playerjoinedasspectator, "");
			if (Utils.debug()) {
				plugin.getLogger().info("Player " + player.getName() + " joined arena " + arena.getArenaName() + " as a spectator");
			}
		}

		// autojoin
		else if (args[0].equalsIgnoreCase("autojoin")) {
			if (args.length >= 2) {
				if (!args[1].equalsIgnoreCase("pvp") && !args[1].equalsIgnoreCase("nopvp")) {
					Messages.sendMessage(player, "&c Invalid argument supplied");
					return false;
				}
			}
			String arenatype = (args.length >= 2) ? args[1] : "";
			plugin.getMenus().autoJoin(player, arenatype);
		}

		// spleef_reloaded info
		else if (args[0].equalsIgnoreCase("info")) {
			Utils.displayInfo(player);
		}

		// player stats
		else if (args[0].equalsIgnoreCase("stats")) {
			if (!plugin.useStats()) {
				Messages.sendMessage(player, Messages.statsdisabled);
				return false;
			}
			String uuid = plugin.getStats().getPlayerUUID(player);
			Messages.sendMessage(player, Messages.statshead, false);
			Messages.sendMessage(player, Messages.gamesplayed + plugin.getStats().getPlayedGames(uuid), false);
			Messages.sendMessage(player, Messages.gameswon + plugin.getStats().getWins(uuid), false);
			Messages.sendMessage(player, Messages.gameslost + plugin.getStats().getLosses(uuid), false);
		}

		// leaderboard
		else if (args[0].equalsIgnoreCase("leaderboard")) {
			if (!plugin.useStats()) {
				Messages.sendMessage(player, Messages.statsdisabled);
				return false;
			}
			int entries = plugin.getConfig().getInt("leaderboard.maxentries", 10);
			if (args.length > 1) {
				if (Utils.isNumber(args[1]) && Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) <= entries) {
					entries = Integer.parseInt(args[1]);
				}
			}
			Messages.sendMessage(player, Messages.leaderhead, false);
			plugin.getStats().getLeaderboard(player, entries);
		}

		// leave arena
		else if (args[0].equalsIgnoreCase("leave")) {
			Arena arena = plugin.amanager.getPlayerArena(player.getName());
			if (arena != null) {
				arena.getPlayerHandler().leavePlayer(player, Messages.playerlefttoplayer, Messages.playerlefttoothers);
			} else {
				Messages.sendMessage(player, Messages.playernotinarena);
				return false;
			}
		}

		// all commands
		else if (args[0].equalsIgnoreCase("cmds")) {
			Messages.sendMessage(player, "&7============" + Messages.spprefix + "============", false);
			Utils.displayHelp(player);
			Messages.sendMessage(player, "&7============[&6Other commands&7]============", false);
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup deletespectate {arena}", true), Utils.getTextComponent(Messages.setupdelspectate));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setgameleveldestroydelay {arena} {ticks}", true), Utils.getTextComponent(Messages.setupdelay));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setregenerationdelay {arena} {ticks}", true), Utils.getTextComponent(Messages.setupregendelay));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setmaxplayers {arena} {players}", true), Utils.getTextComponent(Messages.setupmax));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setminplayers {arena} {players}", true), Utils.getTextComponent(Messages.setupmin));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setvotepercent {arena} {0<votepercent<1}", true), Utils.getTextComponent(Messages.setupvote));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup settimelimit {arena} {seconds}", true), Utils.getTextComponent(Messages.setuptimelimit));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setcountdown {arena} {seconds}", true), Utils.getTextComponent(Messages.setupcountdown));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setmoneyreward {arena} {amount}", true), Utils.getTextComponent(Messages.setupmoney));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setteleport {arena} {previous/lobby}", true), Utils.getTextComponent(Messages.setupteleport));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setdamage {arena} {yes/no/zero}", true), Utils.getTextComponent(Messages.setupdamage));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup addkit {kitname}", true), Utils.getTextComponent(Messages.setupaddkit));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup deletekit {kitname}", true), Utils.getTextComponent(Messages.setupdelkit));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup enablekits {arena}", true), Utils.getTextComponent(Messages.setupenablekits));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup disablekits {arena}", true), Utils.getTextComponent(Messages.setupdisablekits));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setbarcolor", true), Utils.getTextComponent(Messages.setupbarcolor));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setP1", true), Utils.getTextComponent(Messages.setupp1));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setP2", true), Utils.getTextComponent(Messages.setupp2));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup clear", true), Utils.getTextComponent(Messages.setupclear));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup configure {arena}", true), Utils.getTextComponent(Messages.setupconfigure));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup reloadbars", true), Utils.getTextComponent(Messages.setupreloadbars));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup reloadtitles", true), Utils.getTextComponent(Messages.setupreloadtitles));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup reloadmsg", true), Utils.getTextComponent(Messages.setupreloadmsg));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup reloadconfig", true), Utils.getTextComponent(Messages.setupreloadconfig));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup enable {arena}", true), Utils.getTextComponent(Messages.setupenable));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup disable {arena}", true), Utils.getTextComponent(Messages.setupdisable));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup delete {arena}", true), Utils.getTextComponent(Messages.setupdelete));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setreward {arena}", true), Utils.getTextComponent(Messages.setupreward));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setfee {arena} {amount}", true), Utils.getTextComponent(Messages.setupfee));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setcurrency {arena} {item}", true), Utils.getTextComponent(Messages.setupcurrency));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup setlobby", true), Utils.getTextComponent(Messages.setuplobby));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup deletelobby", true), Utils.getTextComponent(Messages.setupdellobby));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup addspawn", true), Utils.getTextComponent(Messages.setupaddspawn));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup deletespawnpoints", true), Utils.getTextComponent(Messages.setupdelspawns));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup deletewaitingspawn", true), Utils.getTextComponent(Messages.setupdelwaiting));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup addtowhitelist", true), Utils.getTextComponent(Messages.setupwhitelist));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup resetstats {player}", true), Utils.getTextComponent(Messages.setupresetstats));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup resetcachedrank {player}", true), Utils.getTextComponent(Messages.setupresetrank));
			player.spigot().sendMessage(Utils.getTextComponent("/spsetup help", true), Utils.getTextComponent(Messages.setuphelp));
		}

		// vote
		else if (args[0].equalsIgnoreCase("vote")) {
			Arena arena = plugin.amanager.getPlayerArena(player.getName());
			if (arena == null) {
				Messages.sendMessage(player, Messages.playernotinarena);
				return false;
			}
			if (!arena.getPlayersManager().getPlayers().contains(player)) {
				Messages.sendMessage(player, Messages.playercannotvote);
				return false;
			}
			if (arena.getPlayerHandler().vote(player)) {
				Messages.sendMessage(player, Messages.playervotedforstart);
			} else {
				Messages.sendMessage(player, Messages.playeralreadyvotedforstart);
				return false;
			}
		}

		// listkits
		else if (args[0].equalsIgnoreCase("listkit") || args[0].equalsIgnoreCase("listkits")) {
			if (args.length >= 2) {
				plugin.getKitManager().listKit(args[1], player);
				return true;
			}

			int kitcount = plugin.getKitManager().getKits().size();
			Messages.sendMessage(player, Messages.availablekits.replace("{COUNT}", String.valueOf(kitcount)));
			if (kitcount == 0) {
				return false;
			}
			StringJoiner message = new StringJoiner(" : ");
			plugin.getKitManager().getKits().stream().sorted().forEach(kit -> {
				message.add("&a" + kit);
			});
			Messages.sendMessage(player, message.toString(), false);
		}

		// listrewards
		else if (args[0].equalsIgnoreCase("listrewards")) {
			if (!player.hasPermission("spleef.listrewards")) {
				Messages.sendMessage(player, Messages.nopermission);
				return false;
			}
			if (args.length != 2) {
				Messages.sendMessage(player, "&c Invalid number of arguments supplied");
				return false;
			}
			Arena arena = plugin.amanager.getArenaByName(args[1]);
			if (arena == null) {
				Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
				return false;
			}
			arena.getStructureManager().getRewards().listRewards(player, args[1]);
		}

		// start
		else if (args[0].equalsIgnoreCase("start")) {
			if (!player.hasPermission("spleef.start")) {
				Messages.sendMessage(player, Messages.nopermission);
				return false;
			}
			if (args.length != 2) {
				Messages.sendMessage(player, "&c Invalid number of arguments supplied");
				return false;
			}
			Arena arena = plugin.amanager.getArenaByName(args[1]);
			if (arena == null) {
				Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[1]));
				return false;
			}
			if (arena.getPlayersManager().getPlayersCount() <= 1) {
				Messages.sendMessage(player, Messages.playersrequiredtostart);
				return false;
			}
			if (!arena.getStatusManager().isArenaStarting()) {
				plugin.getServer().getConsoleSender().sendMessage("[Spleef_reloaded] Arena " + ChatColor.GOLD + arena.getArenaName() + ChatColor.WHITE + " force-started by " + ChatColor.AQUA + player.getName());
				arena.getGameHandler().forceStartByCommand();
			} else {
				Messages.sendMessage(player, arena.getStatusManager().getFormattedMessage(Messages.arenastarting));
				return false;
			}
		}

		// party
		else if(args[0].equalsIgnoreCase("party")) {
			if (!player.hasPermission("spleef.party")) {
				Messages.sendMessage(player, Messages.nopermission);
				return false;
			}
			if (!plugin.isParties()) {
				Messages.sendMessage(player, Messages.partynotenabled);
				return false;
			}
			if (!validatePartyArgs(args)) {
				Messages.sendMessage(player, "&c Invalid number of arguments supplied");
				return false;
			}
			plugin.getParties().handleCommand(player, args);
		}

		else {
			Messages.sendMessage(player, "&c Invalid argument supplied, please use &6/sp help");
			return false;
		}	
		return true;
	}

	private boolean validatePartyArgs(String[] args) {
		if (args.length == 1) {
			return false;
		}

		switch(args[1].toLowerCase()) {
			case "invite":
			case "unkick":
			case "kick":
			case "accept":
			case "decline":
				return args.length == 3;

			case "create":
			case "leave":
			case "info":
				return args.length == 2;

			default:
		}
		return false;
	}
}

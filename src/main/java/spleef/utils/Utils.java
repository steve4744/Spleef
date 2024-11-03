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

package spleef.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import spleef.Spleef;
import spleef.arena.Arena;
import spleef.messages.Messages;

public class Utils {

	private static Map<String, String> ranks = new HashMap<>();
	private static Map<String, String> colours = new HashMap<>();
	private static final Logger log = Spleef.getInstance().getLogger();

	public static boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {}
        return false;
    }

	public static boolean isDouble(String text) {
		try {
			Double.parseDouble(text);
			return true;
		} catch (NumberFormatException e) {}
		return false;
	}

	public static String getFormattedTime(int totalSeconds) {
		int hours = totalSeconds / 3600;
		int minutes = (totalSeconds % 3600) / 60;
		int seconds = totalSeconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public static int playerCount() {
		int pCount = 0;
		for (Arena arena : Spleef.getInstance().amanager.getArenas()) {
			pCount += arena.getPlayersManager().getPlayersCount();
		}
		return pCount;
	}

	public static int spectatorCount() {
		int sCount = 0;
		for (Arena arena : Spleef.getInstance().amanager.getArenas()) {
			sCount += arena.getPlayersManager().getSpectatorsCount();
		}
		return sCount;
	}

	public static int pvpPlayerCount() {
		int pCount = 0;
		for (Arena arena : Spleef.getInstance().amanager.getPvpArenas()) {
			pCount += arena.getPlayersManager().getPlayersCount();
		}
		return pCount;
	}

	public static int nonPvpPlayerCount() {
		int pCount = 0;
		for (Arena arena : Spleef.getInstance().amanager.getNonPvpArenas()) {
			pCount += arena.getPlayersManager().getPlayersCount();
		}
		return pCount;
	}

	public static List<String> getSpleefPlayers() {
		List<String> names = new ArrayList<>();
		Spleef.getInstance().amanager.getArenas().stream().forEach(arena -> {
			arena.getPlayersManager().getAllParticipantsCopy().stream().forEach(player -> {
				names.add(player.getName());
			});
		});
		return names;
	}

	public static void displayInfo(CommandSender sender) {
		Messages.sendMessage(sender, "&7============" + Messages.spprefix + "============", false);
		Messages.sendMessage(sender, "&bPlugin Version: &f" + Spleef.getInstance().getDescription().getVersion(), false);
		Messages.sendMessage(sender, "&bWeb: &f" + Spleef.getInstance().getSpigotURL(), false);
		Messages.sendMessage(sender, "&bSpleef_reloaded Author: &fsteve4744", false);
	}

	public static void displayUpdate(Player player) {
		if (player.hasPermission("spleef.version.check")) {
			final String newmsg = " A new version is available!";
			final String download = " Click here to download";
			final String url = Spleef.getInstance().getSpigotURL();

			TextComponent tc = getTextComponentPrefix();
			TextComponent message = new TextComponent(newmsg);
			message.setColor(ChatColor.WHITE);
			tc.addExtra(message);

			Content content = new Text(getUpdateMessage().create());
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, content));

			TextComponent link = buildComponent(download, url, url, "", "OPEN_URL");
			link.setColor(ChatColor.AQUA);

			player.spigot().sendMessage(tc, link);
		}
	}

	private static TextComponent getTextComponentPrefix() {
		TextComponent tc = new TextComponent("[");
		tc.setColor(ChatColor.GRAY);
		TextComponent tc2 = new TextComponent("Spleef");
		tc2.setColor(ChatColor.GOLD);
		TextComponent tc3 = new TextComponent("]");
		tc3.setColor(ChatColor.GRAY);
		tc.addExtra(tc2);
		tc.addExtra(tc3);
		return tc;
	}

	private static ComponentBuilder getUpdateMessage() {
		ComponentBuilder cb = new ComponentBuilder("Current version : ").color(ChatColor.AQUA).append(Spleef.getInstance().getDescription().getVersion()).color(ChatColor.GOLD);
		cb.append("\nLatest version : ").color(ChatColor.AQUA).append(Spleef.getInstance().getLatestRelease()).color(ChatColor.GOLD);
		return cb;
	}

	public static void displayHelp(Player player) {
		player.spigot().sendMessage(getTextComponent("/spsetup setlobby", true), getTextComponent(Messages.setuplobby));
		player.spigot().sendMessage(getTextComponent("/spsetup create {arena}", true), getTextComponent(Messages.setupcreate));
		player.spigot().sendMessage(getTextComponent("/spsetup setarena {arena}", true), getTextComponent(Messages.setupbounds));
		player.spigot().sendMessage(getTextComponent("/spsetup setloselevel {arena}", true), getTextComponent(Messages.setuploselevel));
		player.spigot().sendMessage(getTextComponent("/spsetup setspawn {arena}", true), getTextComponent(Messages.setupspawn));
		player.spigot().sendMessage(getTextComponent("/spsetup setspectate {arena}", true), getTextComponent(Messages.setupspectate));
		player.spigot().sendMessage(getTextComponent("/spsetup setwatingspawn {arena}", true), getTextComponent(Messages.setupwaiting));
		player.spigot().sendMessage(getTextComponent("/spsetup finish {arena}", true), getTextComponent(Messages.setupfinish));
	}

	/**
	 * Display a clickable invitation message when the first player joins an arena.
	 *
	 * @param player first player that joins an arena
	 * @param arenaname
	 * @param joinMessage
	 */
	public static void displayJoinMessage(Player player, String arenaname, String joinMessage) {
		final String command = "/spleef joinorspectate ";
		final String border = FormattingCodesParser.parseFormattingCodes(Messages.playerborderinvite);
		final String clickAction = "RUN_COMMAND";
		TextComponent jointc = new TextComponent(TextComponent.fromLegacy(border + "\n"));
		jointc.addExtra(buildComponent(joinMessage, Messages.playerclickinvite.replace("{ARENA}", arenaname), arenaname, command, clickAction));
		jointc.addExtra(new TextComponent(TextComponent.fromLegacy("\n" + border)));
		player.spigot().sendMessage(jointc);
	}

	public static String getTitleCase(String input) {
		return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
	}

	public static TextComponent getTextComponent(String text) {
		return getTextComponent(text, false);
	}

	public static TextComponent getTextComponent(String text, Boolean click) {
		if (!click) {
			TextComponent tc = new TextComponent(text);
			tc.setColor(ChatColor.RED);
			return tc;
		}
		final String clickAction = "SUGGEST_COMMAND";
		String splitter = text.contains("{") ? "{" : "[";

		TextComponent tc = buildComponent(text, Messages.helpclick, StringUtils.substringBefore(text, splitter), "", clickAction);
		tc.setColor(ChatColor.GOLD);
		tc.addExtra(getTextComponentDelimiter(" - "));
		return tc;
	}

	private static TextComponent getTextComponentDelimiter(String delim) {
		TextComponent tc = new TextComponent(delim);
		tc.setColor(ChatColor.WHITE);
		return tc;
	}

	public static boolean debug() {
		return Spleef.getInstance().getConfig().getBoolean("debug", false);
	}

	public static String getDecimalFormat(String amount) {
		String formattedAmount = amount;
		if (!isNumber(amount)) {
			DecimalFormat df = new DecimalFormat("0.00");
			formattedAmount = (amount.endsWith(".00") || amount.endsWith(".0")) ? amount.split("\\.")[0] : df.format(Double.valueOf(amount));
		}
		return formattedAmount;
	}

	public static String getFormattedCurrency(String amount) {
		return Spleef.getInstance().getConfig().getString("currency.prefix") + getDecimalFormat(amount) +
				Spleef.getInstance().getConfig().getString("currency.suffix");
	}

	/**
	 * Cache the player's primary group or prefix on joining an arena. If the player's
	 * rank has changed then update the cache.
	 *
	 * @param player
	 */
	public static void cachePlayerGroupData(OfflinePlayer player) {
		String rank = Spleef.getInstance().getVaultHandler().getPermissions().getPrimaryGroup(null, player);
		if (rank == null) {
			return;
		}
		if (!rank.equalsIgnoreCase(ranks.get(player.getName()))) {
			cacheRank(player);
		}
	}

	/**
	 * Cache the player's rank or prefix.
	 * If the rank is not cached, retrieve it and cache it.
	 * If the player is offline retrieve it asynchronously and cache it.
	 *
	 * @param OfflinePlayer player
	 */
	private static void cacheRank(OfflinePlayer player) {
		FileConfiguration config = Spleef.getInstance().getConfig();
		String rank = "";
		String cgmeta = "";
		if (Spleef.getInstance().getVaultHandler().isPermissions() && config.getBoolean("UseRankInChat.usegroup")) {
			if (player.isOnline()) {
				rank = Spleef.getInstance().getVaultHandler().getPermissions().getPrimaryGroup(null, player);
				if (config.getBoolean("UseRankInChat.groupcolormeta")) {
					cgmeta = Spleef.getInstance().getVaultHandler().getChat().getGroupInfoString("", rank, "spleef-color", "");
				}
				if (Utils.debug()) {
					log.info("[Spleef_reloaded] Cached rank " + rank + " for online player " + player.getName());
					log.info("[Spleef_reloaded] Cached colour " + cgmeta + " for online player " + player.getName());
				}
			} else {
				final String pn = player.getName();
				new BukkitRunnable() {
					@Override
					public void run() {
						String cgmeta = "";
						String rank = Spleef.getInstance().getVaultHandler().getPermissions().getPrimaryGroup(null, player);
						ranks.put(pn, rank != null ? rank : "");

						if (config.getBoolean("UseRankInChat.groupcolormeta")) {
							cgmeta = Spleef.getInstance().getVaultHandler().getChat().getGroupInfoString("", rank, "spleef-color", "");
							colours.put(pn, cgmeta != null ? cgmeta : "");
						}

						if (Utils.debug()) {
							log.info("[Spleef_reloaded] Cached rank " + rank + " for offline player " + pn);
							log.info("[Spleef_reloaded] Cached colour " + cgmeta + " for offline player " + pn);
						}
					}
				}.runTaskAsynchronously(Spleef.getInstance());
			}

		} else if (Spleef.getInstance().getVaultHandler().isChat() && config.getBoolean("UseRankInChat.useprefix")) {
			if (player.isOnline()) {
				rank = Spleef.getInstance().getVaultHandler().getChat().getPlayerPrefix(null, player);
				if (Utils.debug()) {
					log.info("[Spleef_reloaded] Cached prefix " + rank + " for online player " + player.getName());
				}
			} else {
				final String pn = player.getName();
				new BukkitRunnable() {
					@Override
					public void run() {
						String rank = Spleef.getInstance().getVaultHandler().getChat().getPlayerPrefix(null, player);
						ranks.put(pn, rank != null ? rank : "");

						if (Utils.debug()) {
							log.info("[Spleef_reloaded] Cached prefix " + rank + "for offline player " + pn);
						}
					}
				}.runTaskAsynchronously(Spleef.getInstance());
			}
		}
		ranks.put(player.getName(), rank != null ? rank : "");
		colours.put(player.getName(), cgmeta != null ? cgmeta : "");
	}

	/**
	 * Attempt to get a player's cached rank. This can be either the player's prefix or primary group.
	 *
	 * @param player
	 * @return player's rank.
	 */
	public static String getRank(OfflinePlayer player) {
		if (Spleef.getInstance().getConfig().getBoolean("UseRankInChat.enabled")) {
			if (ranks.containsKey(player.getName())) {
				return ranks.get(player.getName());
			}
			cacheRank(player);
		}
		return "";
	}

	/**
	 * Get a player's cached rank. This can be either the player's prefix or primary group.
	 *
	 * @param player name
	 * @return player's rank.
	 */
	public static String getRank(String playerName) {
		if (!Spleef.getInstance().getConfig().getBoolean("UseRankInChat.enabled")) {
			return "";
		}
		return ranks.getOrDefault(playerName, "");
	}

	/**
	 * Attempt to get a player's cached colour meta. The colour is applied to players of the same rank/group.
	 * If the rank is not cached, retrieve it asynchronously and cache it.
	 *
	 * @param player
	 * @return player's rank.
	 */
	public static String getColourMeta(OfflinePlayer player) {
		FileConfiguration config = Spleef.getInstance().getConfig();
		if (config.getBoolean("UseRankInChat.enabled") && config.getBoolean("UseRankInChat.groupcolormeta")) {
			if (colours.containsKey(player.getName())) {
				return colours.get(player.getName());
			}
		}
		return "";
	}

	/**
	 * Get a player's cached colour meta. The colour is applied to players of the same rank/group.
	 *
	 * @param player name
	 * @return player's rank.
	 */
	public static String getColourMeta(String playerName) {
		FileConfiguration config = Spleef.getInstance().getConfig();
		if (config.getBoolean("UseRankInChat.enabled") && config.getBoolean("UseRankInChat.groupcolormeta")) {
			return colours.get(playerName) != null ? colours.get(playerName) : "";
		}
		return "";
	}

	public static void removeRankFromCache(String playerName) {
		ranks.remove(playerName);
		colours.remove(playerName);
	}

	/**
	 * The maximum number of double jumps the player is allowed. If permissions are used,
	 * return the lower number of the maximum and number allowed by the permission node.
	 * This applies to free and purchased double jumps.
	 *
	 * @param player
	 * @param max allowed double jumps
	 * @return integer representing the number of double jumps to give player
	 */
	public static int getAllowedDoubleJumps(Player player, int max) {
		if (!Spleef.getInstance().getConfig().getBoolean("special.UseDoubleJumpPermissions") || max <= 0) {
			return max;
		}
		String permissionPrefix = "spleef.doublejumps.";
		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getPermission().startsWith(permissionPrefix) && attachmentInfo.getValue()) {
				String permission = attachmentInfo.getPermission();
				if (!isNumber(permission.substring(permission.lastIndexOf(".") + 1))) {
					return 0;
				}
				return Math.min(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)), max);
			}
		}
		return max;
	}

	/**
	 * Display a clickable invitation to join a Spleef party.
	 *
	 * @param player party leader
	 * @param target player being invited
	 */
	public static void displayPartyInvite(Player player, String target) {
		final String command1 = "/spleef party accept ";
		final String command2 = "/spleef party decline ";
		final String clickAction = "RUN_COMMAND";

		TextComponent accept = buildComponent(Messages.partyclickaccept, Messages.partyaccepttext, player.getName(), command1, clickAction);
		TextComponent decline = buildComponent(Messages.partyclickdecline, Messages.partydeclinetext, player.getName(), command2, clickAction);
		accept.addExtra(" | ");

		Bukkit.getPlayer(target).spigot().sendMessage(accept, decline);
	}

	/**
	 * Add the click event and hover text to the text component.
	 *
	 * @param text
	 * @param hoverMessage
	 * @param target target of command, e.g. party leader or arena
	 * @param command
	 * @param action the click event action
	 * @return
	 */
	private static TextComponent buildComponent(String text, String hoverMessage, String target, String command, String clickAction) {
		Content content = new Text(ChatColor.translateAlternateColorCodes('&', hoverMessage));
		TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));

		component.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(clickAction), command + target));
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, content));
		return component;
	}
}

package spleef.commands.setup.arena;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;
import spleef.utils.Utils;

public class SetFee implements CommandHandlerInterface {

	private final Spleef plugin;

	public SetFee(Spleef plugin) {
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
			if (!Utils.isDouble(args[1]) || Double.parseDouble(args[1]) < 0) {
				Messages.sendMessage(player, "&c The fee to join must be a positive");
				return true;
			}
			arena.getStructureManager().setFee(Double.parseDouble(args[1]));
			Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 Join fee set to &6" + args[1]);
		} else {
			Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
		}
		return true;
	}

	@Override
	public int getMinArgsLength() {
		return 2;
	}

}

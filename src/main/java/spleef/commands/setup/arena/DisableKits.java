package spleef.commands.setup.arena;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;

public class DisableKits implements CommandHandlerInterface {
	
	private Spleef plugin;
	public DisableKits(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena != null) {
			if (!arena.getStructureManager().isKitsEnabled()) {
				Messages.sendMessage(player, "&c Kits are already disabled for arena &6" + args[0]);
				return true;
			}
			if (arena.getStatusManager().isArenaEnabled()) {
				Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
				return true;
			}
			arena.getStructureManager().enableKits(false);
			Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 Kits have been &6disabled");
			
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

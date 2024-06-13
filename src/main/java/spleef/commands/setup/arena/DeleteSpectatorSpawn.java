package spleef.commands.setup.arena;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;

public class DeleteSpectatorSpawn implements CommandHandlerInterface {

	private Spleef plugin;
	public DeleteSpectatorSpawn(Spleef plugin) {
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
			arena.getStructureManager().removeSpectatorsSpawn();
			Messages.sendMessage(player, "&7 Spectator spawn for arena &6" + args[0] + "&7 deleted");
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

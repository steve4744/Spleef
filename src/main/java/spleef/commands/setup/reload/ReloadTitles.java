package spleef.commands.setup.reload;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.utils.TitleMsg;

public class ReloadTitles  implements CommandHandlerInterface {

	private Spleef plugin;
	
	public ReloadTitles(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		TitleMsg.loadTitles(plugin);
		player.sendMessage("Titles reloaded");
		return true;
	}

	@Override
	public int getMinArgsLength() {
		return 0;
	}

}

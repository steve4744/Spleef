package spleef.commands.setup.other;

import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.commands.setup.CommandHandlerInterface;
import spleef.messages.Messages;

public class SetLanguage implements CommandHandlerInterface {

	private Spleef plugin;

	public SetLanguage(Spleef plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		if (!plugin.getLanguage().getTranslatedLanguages().contains(args[0])) {
			Messages.sendMessage(player, "&7 Language not currently supported: &c" + args[0]);
			return true;
		}
		plugin.getLanguage().setLang(args[0]);
		plugin.reloadConfig();
		Messages.loadMessages(plugin);
		Messages.sendMessage(player, "&7 Language set to &6" + args[0]);
		return true;
	}

	@Override
	public int getMinArgsLength() {
		return 1;
	}
}

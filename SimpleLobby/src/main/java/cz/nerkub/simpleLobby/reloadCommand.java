package cz.nerkub.simpleLobby;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class reloadCommand implements CommandExecutor {
	private final SimpleLobby plugin;

	public reloadCommand(SimpleLobby plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender.hasPermission("simplelobby.admin")) {
			plugin.reloadConfig();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig was reloaded!"));
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, but you do not have permission!"));
		}

		return true;
	}
}

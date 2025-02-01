package cz.nerkub.simpleLobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class lobbyCommand implements CommandExecutor {

	private final SimpleLobby plugin;

	public lobbyCommand(SimpleLobby plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player player) {
			if (player.hasPermission("simplelobby.admin")) {
				if (command.getName().equalsIgnoreCase("setlobby")) {
					Location spawn = player.getLocation();
					FileConfiguration config = plugin.getConfig();

					config.set("lobby.world", spawn.getWorld().getName());
					config.set("lobby.x", spawn.getX());
					config.set("lobby.y", spawn.getY());
					config.set("lobby.z", spawn.getZ());
					config.set("lobby.pithc", spawn.getPitch());
					config.set("lobby.yaw", spawn.getYaw());

					plugin.saveConfig();

					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aLobby successfully set!"));
				}
			}

			if (command.getName().equalsIgnoreCase("lobby")) {

				double x = plugin.getConfig().getDouble("lobby.x");
				double y = plugin.getConfig().getDouble("lobby.y");
				double z = plugin.getConfig().getDouble("lobby.z");
				String world = plugin.getConfig().getString("lobby.world");
				float yaw = (float) plugin.getConfig().getDouble("lobby.yaw");
				float pitch = (float) plugin.getConfig().getDouble("lobby.pitch");

				Location spawn = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

				player.teleport(spawn);
			}
		}


		return true;
	}
}

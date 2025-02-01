package cz.nerkub.simpleLobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class onJoinListener implements Listener {

	private final SimpleLobby plugin;

	public onJoinListener(SimpleLobby plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		FileConfiguration config = plugin.getConfig();
		String prefix = config.getString("prefix");

		// Načtení lokace spawnu
		double x = config.getDouble("lobby.x");
		double y = config.getDouble("lobby.y");
		double z = config.getDouble("lobby.z");
		String world = config.getString("lobby.world");
		float yaw = (float) config.getDouble("lobby.yaw");
		float pitch = (float) config.getDouble("lobby.pitch");

		Location spawn = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

		player.setAllowFlight(false);
		player.setFlying(false);

		if (player.getLocation().getWorld().getName().equals(config.getString("lobby.world"))) {
			// Nastavení GameMode
			String gmString = config.getString("on-join.gamemode");
			player.setGameMode(GameMode.valueOf(gmString.toUpperCase()));
		}

		if (config.getBoolean("custom-join-leave-messages.enabled")) {
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("custom-join-leave-messages.join")
					.replace("%player%", player.getName())));
		}

		if (config.getBoolean("on-join.teleport-to-lobby")) {
			// Teleportace hráče na spawn
			player.teleport(spawn);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		FileConfiguration config = plugin.getConfig();
		String prefix = config.getString("prefix");
		Player player = event.getPlayer();

		if (config.getBoolean("custom-join-leave-messages.enabled")) {
			event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("custom-join-leave-messages.leave")
					.replace("%player%", player.getName())));
		}
	}
}

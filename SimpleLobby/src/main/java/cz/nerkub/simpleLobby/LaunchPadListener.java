package cz.nerkub.simpleLobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class LaunchPadListener implements Listener {

	private final SimpleLobby plugin;

	private Material bottomBlock;
	private Material topBlock;
	private double launchPower;
	private double verticalBoost;

	public LaunchPadListener(SimpleLobby plugin) {
		this.plugin = plugin;
	}


	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		FileConfiguration config = plugin.getConfig();
		bottomBlock = Material.matchMaterial(config.getString("launchpad.bottomBlock"));
		topBlock = Material.matchMaterial(config.getString("launchpad.topBlock"));

		launchPower = config.getDouble("launchpad.launchPower", 1.5);
		verticalBoost = config.getDouble("launchpad.verticalBoost", 1.0);

		Player player = event.getPlayer();
		Location location = player.getLocation();

		Location bottomBlockLoc = location.clone().subtract(0, 1, 0);
		Location topBlockLoc = location.clone();
		if (player.getLocation().getWorld().getName().equals(plugin.getConfig().getString("lobby.world"))) {

			if (bottomBlockLoc.getBlock().getType() == bottomBlock && topBlockLoc.getBlock().getType() == topBlock) {
				Vector direction = player.getLocation().getDirection().setY(0).normalize();

				// Přidáme vertikální sílu
				direction.multiply(launchPower).setY(verticalBoost);

				// Aplikujeme hráči odraz
				player.setVelocity(direction);

				// Zvukový efekt při odrazu
				player.getWorld().playSound(player.getLocation(), "minecraft:entity.player.levelup", 1.0f, 1.5f);

				// Partikl efekt (volitelné)
				player.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation(), 10);
			}
		}
	}
}


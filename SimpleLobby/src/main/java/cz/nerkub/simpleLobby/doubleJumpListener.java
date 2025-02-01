package cz.nerkub.simpleLobby;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class doubleJumpListener implements Listener {

	private final SimpleLobby plugin;

	public doubleJumpListener(SimpleLobby plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (plugin.getConfig().getBoolean("double-jump")) {
			if (player.getGameMode() != GameMode.CREATIVE && player.isOnGround()) {
				player.setAllowFlight(true);
			}
		} else {
			if (player.getGameMode() != GameMode.CREATIVE) {
				player.setAllowFlight(false);
			}
		}
	}

	@EventHandler
	public void onPlayerFlight(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();

		if (plugin.getConfig().getBoolean("double-jump")) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);

				player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(1.0));

				// Vizuální efekt
				player.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation(), 10);

				// Zvuk efektu double jumpu
				player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.0f);

				player.setAllowFlight(false);
				player.setFlying(false);
			}
		}

		if (player.getGameMode() != GameMode.CREATIVE) {
			player.setFlying(false);
		}
	}

}

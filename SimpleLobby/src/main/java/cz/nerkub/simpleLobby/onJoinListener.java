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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

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

		// Nastavení GameMode
		String gmString = config.getString("on-join.gamemode");
		if (gmString != null) {
			try {
				GameMode gm = GameMode.valueOf(gmString.toUpperCase());
				if (!player.getGameMode().equals(gm)) {
					player.setGameMode(gm);
				}
			} catch (IllegalArgumentException e) {
				Bukkit.getLogger().warning("Invalid GameMode in config: " + gmString);
			}
		}

		// Odebrání všech aktivních efektů
		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

		// Aplikace efektů, pokud je v configu zapnuto
		if (config.getBoolean("on-join.effect")) {
			List<Map<?, ?>> effectsList = config.getMapList("effects");

			for (Map<?, ?> effectMap : effectsList) {
				try {
					// Načtení efektu z configu
					String effectType = (String) effectMap.get("type");
					PotionEffectType potionEffect = PotionEffectType.getByName(effectType.toUpperCase());

					if (potionEffect == null) {
						Bukkit.getLogger().warning("Invalid potion effect type: " + effectType);
						continue;
					}

					// Načtení úrovně a délky efektu
					int level = (int) effectMap.get("level");
					int timer = (int) effectMap.get("timer");

					// Převod timeru (-1 = nekonečno)
					int duration = (timer == -1) ? Integer.MAX_VALUE : timer * 20; // Sekundy na ticky

					// Přidání efektu hráči
					player.addPotionEffect(new PotionEffect(potionEffect, duration, level - 1, true, false));

				} catch (Exception e) {
					Bukkit.getLogger().warning("Error loading effect from config: " + e.getMessage());
				}
			}
		}

		if (config.getBoolean("custom-join-leave-messages.enabled")) {
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("custom-join-leave-messages.join")
					.replace("%player%", player.getName())));
		}

		if (config.getBoolean("on-join.teleport-to-lobby")){
			// Teleportace hráče na spawn
			player.teleport(spawn);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		FileConfiguration config = plugin.getConfig();
		String prefix = config.getString("prefix");
		Player player = event.getPlayer();

		// Odebrání všech aktivních efektů
		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

		if (config.getBoolean("custom-join-leave-messages.enabled")) {
			event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("custom-join-leave-messages.leave")
					.replace("%player%", player.getName())));
		}
	}
}

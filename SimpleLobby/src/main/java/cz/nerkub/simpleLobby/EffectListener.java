package cz.nerkub.simpleLobby;

import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public class EffectListener implements Listener {

	private final SimpleLobby plugin;

	public EffectListener(SimpleLobby plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		String lobbyWorld = plugin.getConfig().getString("lobby.world");
		FileConfiguration config = plugin.getConfig();

		// Pokud hráč opustil lobby svět
		if (!player.getWorld().getName().equals(lobbyWorld)) {
			// Odebereme všechny efekty při opuštění lobby světa
			player.getActivePotionEffects().forEach(effect -> {
				if (effect.getDuration() == -1) {
					player.removePotionEffect(effect.getType());
				}
			});
		}

		// Pokud hráč přišel do lobby svět
		if (player.getWorld().getName().equals(lobbyWorld)) {
			// Pokud máme zapnuté efekty při vstupu do lobby světa
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

						// Přidání efektu hráči
						player.addPotionEffect(new PotionEffect(potionEffect, timer, level - 1, true, false));

					} catch (Exception e) {
						Bukkit.getLogger().warning("Error loading effect from config: " + e.getMessage());
					}
				}
			}
		}
	}
}
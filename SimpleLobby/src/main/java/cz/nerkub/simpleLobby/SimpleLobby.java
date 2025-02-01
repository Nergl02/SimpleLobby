package cz.nerkub.simpleLobby;

import org.bukkit.plugin.java.JavaPlugin;


public final class SimpleLobby extends JavaPlugin {

	private static SimpleLobby plugin;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		// Plugin startup logic
		plugin = this;

		this.getCommand("setlobby").setExecutor(new lobbyCommand(this));
		this.getCommand("lobby").setExecutor(new lobbyCommand(this));

		this.getServer().getPluginManager().registerEvents(new onJoinListener(this), this);
		this.getServer().getPluginManager().registerEvents(new LaunchPadListener(this), this);
		this.getServer().getPluginManager().registerEvents(new doubleJumpListener(this), this);
		this.getServer().getPluginManager().registerEvents(new EffectListener(this), this);

		this.getCommand("slreload").setExecutor(new reloadCommand(this));

		Metrics metrics = new Metrics(this, 24596);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}

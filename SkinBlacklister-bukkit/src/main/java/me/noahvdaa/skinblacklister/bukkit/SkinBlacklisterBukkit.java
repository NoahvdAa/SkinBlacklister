package me.noahvdaa.skinblacklister.bukkit;

import me.noahvdaa.skinblacklister.bukkit.listeners.PlayerEventsListener;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SkinBlacklisterBukkit extends JavaPlugin implements SkinBlacklister {

	@Override
	public void onEnable() {
		try {
			getConfigLoader().loadConfig(this.getDataFolder().toPath());
			getConfigLoader().saveConfig();
		} catch (IOException e) {
			getLogger().warning("Failed to load config:");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		getServer().getPluginManager().registerEvents(new PlayerEventsListener(this), this);

		new Metrics(this, 13227);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

}

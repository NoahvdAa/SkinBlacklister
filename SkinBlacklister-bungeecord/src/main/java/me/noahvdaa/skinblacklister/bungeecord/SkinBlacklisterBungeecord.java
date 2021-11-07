package me.noahvdaa.skinblacklister.bungeecord;

import me.noahvdaa.skinblacklister.bungeecord.listeners.PlayerEventsListener;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.io.IOException;

public final class SkinBlacklisterBungeecord extends Plugin implements SkinBlacklister {

	@Override
	public void onEnable() {
		try {
			getConfigLoader().loadConfig(this.getDataFolder().toPath());
			getConfigLoader().saveConfig();
		} catch (IOException e) {
			getLogger().warning("Failed to load config:");
			e.printStackTrace();
			return;
		}

		getProxy().getPluginManager().registerListener(this, new PlayerEventsListener(this));

		new Metrics(this, 13228);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

}

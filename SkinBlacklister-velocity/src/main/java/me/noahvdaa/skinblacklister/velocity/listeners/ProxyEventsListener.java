package me.noahvdaa.skinblacklister.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import me.noahvdaa.skinblacklister.common.config.ConfigLoader;
import me.noahvdaa.skinblacklister.velocity.SkinBlacklisterVelocity;
import org.slf4j.Logger;

import java.io.IOException;

public class ProxyEventsListener {

	private final SkinBlacklisterVelocity plugin;

	public ProxyEventsListener(SkinBlacklisterVelocity plugin) {
		this.plugin = plugin;
	}

	@Subscribe
	public void onProxyReload(ProxyReloadEvent event) {
		ConfigLoader configLoader = plugin.getConfigLoader();
		Logger logger = plugin.getLogger();

		logger.info("Reloading SkinBlacklister config...");

		try {
			configLoader.loadConfig(plugin.getDataDirectory());
			configLoader.saveConfig();
		} catch (IOException e) {
			logger.warn("Failed to load config:");
			e.printStackTrace();
			return;
		}

		logger.info("Reloaded SkinBlacklister config!");
	}

}

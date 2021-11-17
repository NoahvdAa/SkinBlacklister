package me.noahvdaa.skinblacklister.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import me.noahvdaa.skinblacklister.velocity.listeners.PlayerEventsListener;
import me.noahvdaa.skinblacklister.velocity.listeners.ProxyEventsListener;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(
		id = "skinblacklister",
		name = "SkinBlacklister",
		version = BuildConstants.VERSION,
		description = "Blacklist skins you don't want on your server!",
		url = "https://github.com/NoahvdAa/SkinBlacklister",
		authors = {"NoahvdAa"}
)
public class SkinBlacklisterVelocity implements SkinBlacklister {

	private final ProxyServer server;
	private final Metrics.Factory metricsFactory;
	private final Logger logger;
	private final Path dataDirectory;

	@Inject
	public SkinBlacklisterVelocity(ProxyServer server, Metrics.Factory metricsFactory, Logger logger, @DataDirectory Path dataDirectory) {
		this.server = server;
		this.metricsFactory = metricsFactory;
		this.logger = logger;
		this.dataDirectory = dataDirectory;
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		try {
			getConfigLoader().loadConfig(dataDirectory);
			getConfigLoader().saveConfig();
		} catch (IOException e) {
			logger.warn("Failed to load config:");
			e.printStackTrace();
			return;
		}

		server.getEventManager().register(this, new PlayerEventsListener(this));
		server.getEventManager().register(this, new ProxyEventsListener(this));

		metricsFactory.make(this, 13229);
	}

	public Logger getLogger() {
		return this.logger;
	}

	public Path getDataDirectory() {
		return this.dataDirectory;
	}

}

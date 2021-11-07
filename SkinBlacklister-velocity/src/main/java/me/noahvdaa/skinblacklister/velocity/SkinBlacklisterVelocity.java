package me.noahvdaa.skinblacklister.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import me.noahvdaa.skinblacklister.velocity.listeners.PlayerEventsListener;
import org.bstats.velocity.Metrics;

import javax.inject.Inject;

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

	@Inject
	public SkinBlacklisterVelocity(ProxyServer server, Metrics.Factory metricsFactory) {
		this.server = server;
		this.metricsFactory = metricsFactory;
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		server.getEventManager().register(this, new PlayerEventsListener(this));

		metricsFactory.make(this, 13229);
	}

}

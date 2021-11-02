package me.noahvdaa.skinblacklister.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import me.noahvdaa.skinblacklister.velocity.listener.PlayerEventsListener;

import javax.inject.Inject;

@Plugin(
		id = "skinblacklister",
		name = "SkinBlacklister",
		version = BuildConstants.VERSION,
		description = "Blacklist skins you don't want on your Minecraft server! ",
		url = "https://github.com/NoahvdAa/SkinBlacklister",
		authors = {"NoahvdAa"}
)
public class SkinBlacklisterVelocity implements SkinBlacklister {

	private final ProxyServer server;

	@Inject
	public SkinBlacklisterVelocity(ProxyServer server) {
		this.server = server;
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		server.getEventManager().register(this, new PlayerEventsListener(this));
	}

}

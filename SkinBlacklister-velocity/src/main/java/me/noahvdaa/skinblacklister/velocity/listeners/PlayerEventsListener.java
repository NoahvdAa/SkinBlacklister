package me.noahvdaa.skinblacklister.velocity.listeners;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.noahvdaa.skinblacklister.velocity.SkinBlacklisterVelocity;

import java.io.IOException;

public class PlayerEventsListener {

	private final SkinBlacklisterVelocity plugin;

	public PlayerEventsListener(SkinBlacklisterVelocity plugin) {
		this.plugin = plugin;
	}

	@Subscribe
	public void onPostLogin(PostLoginEvent event) {
		// Don't hold up the login process.
		EventTask.async(() -> {
			Player player = event.getPlayer();

			if (player.hasPermission("skinblacklister.bypass")) return;

			byte[] skin;
			try {
				skin = plugin.getSkinDownloader().getSkin(player.getUniqueId());
			} catch (IOException e) {
				// TODO
				return;
			}
		});
	}
}

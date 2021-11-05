package me.noahvdaa.skinblacklister.bungeecord.listeners;

import me.noahvdaa.skinblacklister.bungeecord.SkinBlacklisterBungeecord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

public class PlayerEventsListener implements Listener {

	private final SkinBlacklisterBungeecord plugin;

	public PlayerEventsListener(SkinBlacklisterBungeecord plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		// Don't hold up the login process.
		plugin.getProxy().getScheduler().runAsync(plugin, () -> {
			ProxiedPlayer player = event.getPlayer();

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

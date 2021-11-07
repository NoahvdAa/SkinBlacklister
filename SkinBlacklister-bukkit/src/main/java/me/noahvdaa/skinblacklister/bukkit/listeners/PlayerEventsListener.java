package me.noahvdaa.skinblacklister.bukkit.listeners;

import me.noahvdaa.skinblacklister.bukkit.SkinBlacklisterBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class PlayerEventsListener implements Listener {

	private final SkinBlacklisterBukkit plugin;

	public PlayerEventsListener(SkinBlacklisterBukkit plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// We definitely don't want to make HTTP requests on the main thread.
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			Player player = event.getPlayer();

			if (player.hasPermission("skinblacklister.bypass")) return;

			byte[] skin = null;
			try {
				skin = plugin.getSkinDownloader().getSkin(player.getUniqueId());
			} catch (IOException e) {
				// Swallowed, handled below.
			}
			if (skin == null) {
				// TODO
				return;
			}
		});
	}

}

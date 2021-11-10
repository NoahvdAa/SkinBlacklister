package me.noahvdaa.skinblacklister.bukkit.listeners;

import me.noahvdaa.skinblacklister.bukkit.SkinBlacklisterBukkit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

			// They already left!
			if (!player.isOnline()) return;

			if (skin == null) {
				if (plugin.getConfigLoader().getConfig().node("Checking").node("KickOnSkinLoadFailure").getBoolean()) {
					String kickMessageUnparsed = plugin.getConfigLoader().getConfig().node("Checking").node("KickOnSkinLoadFailureReason").getString();
					Component kickMessage = MiniMessage.get().parse(kickMessageUnparsed);
					// TODO: Don't use leg*cy serializer.
					String serializedKickMessage = LegacyComponentSerializer.legacySection().serialize(kickMessage);
					// We have to kick synchronously!
					plugin.getServer().getScheduler().runTask(plugin, () -> player.kickPlayer(serializedKickMessage));
					return;
				}
			}
		});
	}

}

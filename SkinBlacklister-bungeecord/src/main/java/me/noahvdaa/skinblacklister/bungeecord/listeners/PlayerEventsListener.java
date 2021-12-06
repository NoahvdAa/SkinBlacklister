package me.noahvdaa.skinblacklister.bungeecord.listeners;

import me.noahvdaa.skinblacklister.bungeecord.SkinBlacklisterBungeecord;
import me.noahvdaa.skinblacklister.common.net.SkinDownloader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
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

			byte[] skin = null;
			try {
				skin = SkinDownloader.getSkin(player.getUniqueId());
			} catch (IOException e) {
				// Swallowed, handled below.
			}

			// They already left!
			if (!player.isConnected()) return;

			if (skin == null) {
				if (plugin.configLoader().config().node("Checking").node("KickOnSkinLoadFailure").getBoolean()) {
					String kickMessageUnparsed = plugin.configLoader().config().node("Checking").node("KickOnSkinLoadFailureReason").getString("");
					Component kickMessage = MiniMessage.miniMessage().parse(kickMessageUnparsed);
					// TODO: Maybe don't use BungeeCord components here?
					BaseComponent[] kickMessageSerialized = BungeeComponentSerializer.get().serialize(kickMessage);

					player.disconnect(kickMessageSerialized);
				}
				return;
			}
		});
	}

}

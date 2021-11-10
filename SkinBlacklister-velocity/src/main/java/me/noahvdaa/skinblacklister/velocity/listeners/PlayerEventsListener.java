package me.noahvdaa.skinblacklister.velocity.listeners;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.noahvdaa.skinblacklister.velocity.SkinBlacklisterVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

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

			byte[] skin = null;
			try {
				skin = plugin.getSkinDownloader().getSkin(player.getUniqueId());
			} catch (IOException e) {
				// Swallowed, handled below.
			}

			// They already left!
			if (!player.isActive()) return;

			if (skin == null) {
				if (plugin.getConfigLoader().getConfig().node("Checking").node("KickOnSkinLoadFailure").getBoolean()) {
					String kickMessageUnparsed = plugin.getConfigLoader().getConfig().node("Checking").node("KickOnSkinLoadFailureReason").getString();
					Component kickMessage = MiniMessage.get().parse(kickMessageUnparsed);

					player.disconnect(kickMessage);
					return;
				}
			}
		});
	}
}

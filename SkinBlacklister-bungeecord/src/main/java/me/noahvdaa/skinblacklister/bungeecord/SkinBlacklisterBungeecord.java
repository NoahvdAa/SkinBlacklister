package me.noahvdaa.skinblacklister.bungeecord;

import me.noahvdaa.skinblacklister.bungeecord.listeners.PlayerEventsListener;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;

public final class SkinBlacklisterBungeecord extends Plugin implements SkinBlacklister {

	private BungeeAudiences adventure;

	@Override
	public void onEnable() {
		try {
			configLoader().loadConfig(this.getDataFolder().toPath());
			configLoader().saveConfig();
		} catch (IOException e) {
			getLogger().warning("Failed to load config:");
			e.printStackTrace();
			return;
		}

		skinMatcher().loadSkins(new File(this.getDataFolder(), "skins"), getLogger());

		adventure = BungeeAudiences.create(this);

		getProxy().getPluginManager().registerListener(this, new PlayerEventsListener(this));

		new Metrics(this, 13228);
	}

	@Override
	public void onDisable() {
		adventure.close();
	}


	public BungeeAudiences adventure() {
		return this.adventure;
	}

}

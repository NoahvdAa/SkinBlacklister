package me.noahvdaa.skinblacklister.bukkit;

import me.noahvdaa.skinblacklister.bukkit.listeners.PlayerEventsListener;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SkinBlacklisterBukkit extends JavaPlugin implements SkinBlacklister {

	private BukkitAudiences adventure;

	@Override
	public void onEnable() {
		try {
			configLoader().loadConfig(this.getDataFolder().toPath());
			configLoader().saveConfig();
		} catch (IOException e) {
			getLogger().warning("Failed to load config:");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		skinMatcher().loadSkins(new File(this.getDataFolder(), "skins"), getLogger());

		adventure = BukkitAudiences.create(this);

		getServer().getPluginManager().registerEvents(new PlayerEventsListener(this), this);

		new Metrics(this, 13227);
	}

	@Override
	public void onDisable() {
		adventure.close();
	}

	public BukkitAudiences adventure() {
		return this.adventure;
	}

}

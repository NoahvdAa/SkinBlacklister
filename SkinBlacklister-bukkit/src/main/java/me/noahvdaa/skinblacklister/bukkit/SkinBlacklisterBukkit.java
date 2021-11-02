package me.noahvdaa.skinblacklister.bukkit;

import me.noahvdaa.skinblacklister.bukkit.listener.PlayerEventsListener;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkinBlacklisterBukkit extends JavaPlugin implements SkinBlacklister {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerEventsListener(this), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

}

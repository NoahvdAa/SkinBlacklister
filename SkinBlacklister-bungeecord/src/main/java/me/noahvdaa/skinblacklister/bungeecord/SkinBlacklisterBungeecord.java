package me.noahvdaa.skinblacklister.bungeecord;

import me.noahvdaa.skinblacklister.bungeecord.listener.PlayerEventsListener;
import me.noahvdaa.skinblacklister.common.SkinBlacklister;
import net.md_5.bungee.api.plugin.Plugin;

public final class SkinBlacklisterBungeecord extends Plugin implements SkinBlacklister {

	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, new PlayerEventsListener(this));
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

}

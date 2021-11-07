package me.noahvdaa.skinblacklister.common;

import me.noahvdaa.skinblacklister.common.config.ConfigLoader;
import me.noahvdaa.skinblacklister.common.net.SkinDownloader;

public interface SkinBlacklister {

	ConfigLoader configLoader = new ConfigLoader();
	SkinDownloader skinDownloader = new SkinDownloader();

	default ConfigLoader getConfigLoader() {
		return this.configLoader;
	}

	default SkinDownloader getSkinDownloader() {
		return this.skinDownloader;
	}

}

package me.noahvdaa.skinblacklister.common;

import me.noahvdaa.skinblacklister.common.net.SkinDownloader;

public interface SkinBlacklister {

	SkinDownloader skinDownloader = new SkinDownloader();

	default SkinDownloader getSkinDownloader() {
		return this.skinDownloader;
	}

}

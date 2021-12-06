package me.noahvdaa.skinblacklister.common;

import me.noahvdaa.skinblacklister.common.config.ConfigLoader;
import me.noahvdaa.skinblacklister.common.matching.SkinMatcher;

public interface SkinBlacklister {

	ConfigLoader configLoader = new ConfigLoader();
	SkinMatcher skinMatcher = new SkinMatcher();

	default ConfigLoader configLoader() {
		return this.configLoader;
	}

	default SkinMatcher skinMatcher() {
		return this.skinMatcher;
	}

}

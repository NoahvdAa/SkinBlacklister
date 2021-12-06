package me.noahvdaa.skinblacklister.common.matching;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SkinMatcher {

	private List<LoadedSkin> skins = new ArrayList<>();

	public void loadSkins(File skinsFolder, Logger logger) {
		if (!skinsFolder.exists()) skinsFolder.mkdirs();

		this.skins = new ArrayList<>();
		File[] skinFiles = skinsFolder.listFiles();
		if (skinFiles == null) return;
		for (File skin : skinFiles) {
			// Ignore files that aren't PNGs.
			if (!skin.getName().toLowerCase().endsWith(".png")) continue;

			BufferedImage skinImage;
			try {
				skinImage = ImageIO.read(skin);
			} catch (IOException e) {
				logger.warning("Failed to load skin file " + skin.getName() + "!");
				e.printStackTrace();
				continue;
			}

			if (skinImage.getHeight() != 64 || skinImage.getWidth() != 64) {
				logger.warning("Invalid skin file " + skin.getName() + "! Skin image must be 64x64 pixels.");
				continue;
			}
			LoadedSkin loadedSkin = new LoadedSkin(skin.getName(), skinImage);
			skins.add(loadedSkin);
		}
	}

	public List<LoadedSkin> skins() {
		return this.skins;
	}

}

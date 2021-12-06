package me.noahvdaa.skinblacklister.common.matching;

import java.awt.image.BufferedImage;

// TODO: This class would be a perfect candidate for a record. Unfortunately, we're stuck with Java 8 for now. (stupid legacy users 😡)
public class LoadedSkin {

	private String name;
	private BufferedImage image;

	public LoadedSkin(String name, BufferedImage image) {
		this.name = name;
		this.image = image;
	}

	public String name() {
		return this.name;
	}

	public BufferedImage image() {
		return this.image;
	}

}

package me.noahvdaa.skinblacklister.common.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigLoader {

	private HoconConfigurationLoader loader;
	private CommentedConfigurationNode config;
	private final String header = "This is the main configuration file for SkinBlacklister.\n" +
			"SkinBlacklister uses MiniMessage for messages, for information on how to use the MiniMessage format,\n" +
			"check out: https://docs.adventure.kyori.net/minimessage#format";

	public void loadConfig(Path dataDirectory) throws IOException {
		this.loader = HoconConfigurationLoader.builder()
				.path(dataDirectory.resolve("config.conf"))
				.headerMode(HeaderMode.PRESERVE)
				.build();

		this.config = this.loader.load();
		this.setDefaults();
	}

	public void saveConfig() throws IOException {
		this.loader.save(this.config);
	}

	private void setDefaults() {
		this.config.node("Checking").act(node -> {
			// TODO: We shouldn't assume we can safely put the config header here.
			node.commentIfAbsent(header);

			node.node("KickOnSkinLoadFailure").act(childNode -> {
				childNode.getBoolean(true);
				childNode.commentIfAbsent("When set to true, players will be kicked if their skin can't be fetched, for the reason specified in KickOnSkinLoadFailureReason.");
			});
			node.node("KickOnSkinLoadFailureReason").act(childNode -> {
				childNode.getString("<red>Your skin can't be fetched. Please try again later!");
				childNode.commentIfAbsent("The reason to kick players for if their skin can't be fetched.");
			});
		});
	}

	public CommentedConfigurationNode config() {
		return this.config;
	}

}

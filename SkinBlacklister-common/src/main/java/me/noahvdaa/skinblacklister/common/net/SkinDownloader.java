package me.noahvdaa.skinblacklister.common.net;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class SkinDownloader {

	public byte[] getSkin(UUID player) throws IOException {
		URL skinURL = getSkinURL(player);
		if (skinURL == null) return null;
		HttpURLConnection con = (HttpURLConnection) skinURL.openConnection();
		con.setRequestMethod("GET");

		byte[] buffer = new byte[con.getContentLength()];
		readToBuffer(con.getInputStream(), buffer);

		return buffer;
	}

	public URL getSkinURL(UUID player) throws IOException {
		URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + player.toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder mojangResponse = new StringBuilder();
		String inputLine;
		// Read all lines of the response.
		while ((inputLine = in.readLine()) != null) {
			mojangResponse.append(inputLine);
		}

		JSONObject parsedResponse = new JSONObject(mojangResponse.toString());
		JSONArray properties = parsedResponse.getJSONArray("properties");
		String texturesValue = properties.getJSONObject(0).getString("value");
		// Decode from the base74 response mojang returns.
		String texturesDecoded = new String(Base64.getDecoder().decode(texturesValue));
		JSONObject textures = new JSONObject(texturesDecoded).getJSONObject("textures");

		// Using default skin.
		if (!textures.has("SKIN")) return null;

		JSONObject skin = textures.getJSONObject("SKIN");

		return new URL(skin.getString("url"));
	}

	private static int readToBuffer(final InputStream input, final byte[] buffer) throws IOException {
		int remaining = buffer.length;
		while (remaining > 0) {
			final int location = buffer.length - remaining;
			final int count = input.read(buffer, location, remaining);
			if (count == -1) { // EOF
				break;
			}
			remaining -= count;
		}
		return buffer.length - remaining;
	}

}

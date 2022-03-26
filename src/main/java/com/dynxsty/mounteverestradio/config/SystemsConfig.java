package com.dynxsty.mounteverestradio.config;

import lombok.Data;

/**
 * Contains configuration settings for various systems which the bot uses, such
 * as databases or dependencies that have runtime properties.
 */
@Data
public class SystemsConfig {
	/**
	 * The token used to create the JDA Discord bot instance.
	 */
	private String jdaBotToken = "";

	/**
	 * The token used to create the YouTube API instance.
	 */
	private String youtubeToken = "";

	/**
	 * The number of threads to allocate to the bot's general purpose async
	 * thread pool.
	 */

	private int asyncPoolSize = 4;

	private SpotifyConfig spotify = new SpotifyConfig();

	@Data
	public static class SpotifyConfig {
		private String clientId = "";
		private String clientSecret = "";
	}
}

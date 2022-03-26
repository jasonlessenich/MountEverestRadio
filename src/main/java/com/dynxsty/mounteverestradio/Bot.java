package com.dynxsty.mounteverestradio;

import com.dynxsty.dih4jda.DIH4JDA;
import com.dynxsty.dih4jda.DIH4JDABuilder;
import com.dynxsty.mounteverestradio.config.BotConfig;
import com.dynxsty.mounteverestradio.listener.ShutdownListener;
import com.dynxsty.mounteverestradio.listener.UserLeftListener;
import com.dynxsty.mounteverestradio.systems.music.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import youTubeAPI.YouTubeAPI;

import java.nio.file.Path;
import java.time.ZoneOffset;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Bot {

	/**
	 * The set of configuration properties that this bot uses.
	 */
	public static BotConfig config;

	/**
	 * A reference to the Spotify API client.
	 */
//	public static SpotifyApi spotifyApi;

	/**
	 * A reference to the YouTube API client.
	 */
	public static YouTubeAPI youtubeApi;

	/**
	 * The Bot's Music Manager.
	 */
	public static MusicManager musicManager;

	/**
	 * A reference to the {@link DIH4JDA} object.
	 */
	public static DIH4JDA dih4jda;

	/**
	 * A general-purpose thread pool that can be used by the bot to execute
	 * tasks outside the main event processing thread.
	 */
	public static ScheduledExecutorService asyncPool;

	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
		config = new BotConfig(Path.of("config"));
		musicManager = new MusicManager();
		asyncPool = Executors.newScheduledThreadPool(config.getSystems().getAsyncPoolSize());
		JDA jda = JDABuilder.createDefault(config.getSystems().getJdaBotToken())
				.setStatus(OnlineStatus.DO_NOT_DISTURB)
				.addEventListeners(PresenceUpdater.standardActivities())
				.build();
		dih4jda = DIH4JDABuilder
				.setJDA(jda)
				.setCommandsPackage("com.dynxsty.mounteverestradio.systems")
				.build();
		addEventListeners(jda);
//		authorizeSpotify(config.getSystems().getSpotify());
		youtubeApi = new YouTubeAPI(config.getSystems().getYoutubeToken());
	}

//	private static void authorizeSpotify(SystemsConfig.SpotifyConfig config) {
//		spotifyApi = new SpotifyApi.Builder()
//				.setClientId(config.getClientId())
//				.setClientSecret(config.getClientSecret())
//				.build();
//		ClientCredentialsRequest accessRequest = spotifyApi.clientCredentials().build();
//		accessRequest.executeAsync().thenAccept(credentials -> spotifyApi.setAccessToken(credentials.getAccessToken()));
//		AuthorizationCodeRefreshRequest refreshRequest = spotifyApi.authorizationCodeRefresh().build();
//		refreshRequest.executeAsync().thenAccept(credentials -> spotifyApi.setRefreshToken(credentials.getRefreshToken()));
//	}

	private static void addEventListeners(JDA jda) {
		jda.addEventListener(
				new ShutdownListener(),
				new UserLeftListener()
		);
	}
}

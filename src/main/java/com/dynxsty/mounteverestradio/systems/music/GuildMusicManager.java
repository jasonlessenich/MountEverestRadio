package com.dynxsty.mounteverestradio.systems.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
	/**
	 * Audio player for the guild.
	 */
	public final AudioPlayer player;
	/**
	 * Track scheduler for the player.
	 */
	public final TrackScheduler scheduler;

	/**
	 * Creates a player and a track scheduler.
	 * @param manager Audio player manager to use for creating the player.
	 */
	public GuildMusicManager(AudioPlayerManager manager) {
		this.player = manager.createPlayer();
		this.scheduler = new TrackScheduler(player);
		this.player.addListener(scheduler);
	}

	/**
	 * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
	 */
	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(player);
	}
}

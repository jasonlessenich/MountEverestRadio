package com.dynxsty.mounteverestradio.systems.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {

	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;

	public MusicManager() {
		this.playerManager = new DefaultAudioPlayerManager();
		this.musicManagers = new HashMap<>();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	public GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = this.musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			this.musicManagers.put(guildId, musicManager);
		}
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		return musicManager;
	}

	public void play(GuildMusicManager manager, AudioTrack track) {
		manager.scheduler.queue(track);
	}

	public void play(Member member, GuildMusicManager manager, AudioTrack track) {
		AudioManager audio = member.getGuild().getAudioManager();
		audio.setSelfDeafened(true);
		if (!audio.isConnected()) {
			audio.openAudioConnection(member.getVoiceState().getChannel());
		}
		this.play(manager, track);
	}

	public AudioPlayerManager getPlayerManager() {
		return playerManager;
	}
}

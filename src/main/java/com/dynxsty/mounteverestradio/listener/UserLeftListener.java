package com.dynxsty.mounteverestradio.listener;

import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class UserLeftListener extends ListenerAdapter {
	@Override
	public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
		if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
			if (event.getChannelLeft().getMembers().size() == 1) {
				GuildMusicManager musicManager = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
				musicManager.scheduler.getQueue().clear();
				musicManager.player.stopTrack();
				event.getGuild().getAudioManager().closeAudioConnection();
			}
		}
	}
}

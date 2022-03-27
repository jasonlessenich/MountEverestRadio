package com.dynxsty.mounteverestradio.systems.music.commands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.MusicUtils;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class TrackLoopCommand extends GuildSlashCommand implements ISlashCommand {
	public TrackLoopCommand() {
		this.setCommandData(Commands.slash("track-loop", "Toggles looping for the current playing song."));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		GuildMusicManager manager = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		AudioTrack current = manager.player.getPlayingTrack();
		if (current == null) {
			Responses.respond(event.getHook(), "There is nothing playing right now").queue();
			return;
		}
		manager.scheduler.setTrackLoop(!manager.scheduler.isTrackLooping());
		String text = "Enabled";
		if (!manager.scheduler.isTrackLooping()) text = "Disabled";
		Responses.respond(event.getHook(), String.format("%s Track Loop", text), MusicUtils.formatPlayingTrack(current)).queue();
	}
}

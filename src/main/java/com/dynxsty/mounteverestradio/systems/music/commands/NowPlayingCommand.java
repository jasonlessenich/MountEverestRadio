package com.dynxsty.mounteverestradio.systems.music.commands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.MusicUtils;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import youTubeAPI.core.entities.search.Item;
import youTubeAPI.core.entities.videos.VideosItem;
import youTubeAPI.core.error.YouTubeException;

import java.time.Instant;

public class NowPlayingCommand extends GuildSlashCommand implements ISlashCommand {
	public NowPlayingCommand() {
		this.setCommandData(Commands.slash("now-playing", "Check whats currently playing."));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		GuildMusicManager guildManager = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		AudioTrack currentlyPlaying = guildManager.player.getPlayingTrack();
		if (currentlyPlaying == null) {
			Responses.respond(event.getHook(), "There's nothing playing right now").queue();
			return;
		}
		try {
			VideosItem result = Bot.youtubeApi.getVideo(currentlyPlaying.getInfo().identifier).getItems().get(0);
			event.getHook().sendMessageEmbeds(MusicUtils.convertToEmbed(event.getUser(), "Now Playing", currentlyPlaying, result)).queue();
		} catch (YouTubeException e) {
			Responses.respond(event.getHook(), e.getMessage()).queue();
		}

	}
}

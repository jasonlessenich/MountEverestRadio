package com.dynxsty.mounteverestradio.systems.music.commands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.MusicUtils;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import youTubeAPI.core.entities.search.Item;
import youTubeAPI.core.error.YouTubeException;

public class PlayCommand extends GuildSlashCommand implements ISlashCommand {
	public PlayCommand() {
		this.setCommandData(Commands.slash("play", "Play some tunes.")
				.addOption(OptionType.STRING, "query", "The song to search for", true)
		);
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		OptionMapping queryOption = event.getOption("query");
		if (queryOption == null) {
			Responses.respond(event.getHook(), "Missing required arguments.").queue();
			return;
		}
		try {
			this.addToQueue(event.getHook(), event.getMember(), queryOption.getAsString(), true);
		} catch (YouTubeException e) {
			Responses.respond(event.getHook(), e.getMessage()).queue();
		}
	}

	private void addToQueue(InteractionHook hook, Member member, String query, boolean urlSearch) throws YouTubeException {
		GuildMusicManager musicManager = Bot.musicManager.getGuildAudioPlayer(member.getGuild());
		// Get the first result
		if (!urlSearch) {
			query = Bot.youtubeApi.searchVideo(query).getItems().get(0).getId().videoId;
		}
		final String usedQuery = query;
		Bot.musicManager.getPlayerManager().loadItemOrdered(musicManager, query, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				try {
					Item result = Bot.youtubeApi.searchVideo(track.getInfo().identifier).getItems().get(0);
					hook.sendMessageEmbeds(MusicUtils.convertToEmbed(member.getUser(), "Adding to Queue", track, result)).queue();
					Bot.musicManager.play(member, musicManager, track);
				} catch (YouTubeException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}
				hook.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
				Bot.musicManager.play(member, musicManager, firstTrack);
			}

			@Override
			public void noMatches() {
				if (urlSearch) {
					try {
						addToQueue(hook, member, usedQuery, false);
					} catch (YouTubeException e) {
						Responses.respond(hook, e.getMessage()).queue();
					}
				} else {
					Responses.respond(hook, String.format("No songs found for `%s`", usedQuery)).queue();
				}
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				Responses.respond(hook, "Could not play: " + exception.getMessage()).queue();
			}
		});
	}
}

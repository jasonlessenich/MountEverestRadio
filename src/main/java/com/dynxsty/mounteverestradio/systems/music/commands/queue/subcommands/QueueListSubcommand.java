package com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.Subcommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.systems.music.TrackScheduler;
import com.dynxsty.mounteverestradio.utils.MusicUtils;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class QueueListSubcommand extends Subcommand implements ISlashCommand {
	public QueueListSubcommand() {
		this.setSubcommandData(new SubcommandData("list", "Shows the current queue."));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		GuildMusicManager player = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		BlockingQueue<AudioTrack> queue = player.scheduler.getQueue();
		event.replyEmbeds(this.buildQueueEmbed(event.getUser(), queue, player)).queue();
	}

	private MessageEmbed buildQueueEmbed(User author, BlockingQueue<AudioTrack> queue, GuildMusicManager musicManager) {
		TrackScheduler scheduler = musicManager.scheduler;
		AudioPlayer player = musicManager.player;
		EmbedBuilder builder = new EmbedBuilder()
				.setAuthor(author.getAsTag(), null, author.getEffectiveAvatarUrl())
				.setTitle(String.format("%sThere are %s songs queued", (scheduler.isQueueLooping() ? "\uD83D\uDD01 " : ""), queue.size()))
				.setColor(Responses.Type.DEFAULT.getColor())
				.setTimestamp(Instant.now());
		AudioTrack track = musicManager.player.getPlayingTrack();
		if (track != null) {
			builder.addField(
					String.format("%s%sCurrently Playing",
							(scheduler.isTrackLooping() ? "\uD83D\uDD02 " : ""),
							(player.isPaused() ? "⏸️ " : "")),
					MusicUtils.formatPlayingTrack(track), false);
			if (!scheduler.isTrackLooping() && !scheduler.isQueueLooping()) {
				builder.setFooter(String.format("Total left: %smin", MusicUtils.convertToMinutes(queue.stream().mapToLong(AudioTrack::getDuration).sum() + (track.getDuration() - track.getPosition()))));
			}
		}
		if (queue.size() > 0) {
			builder.addField("Next Up", queue.stream().map(MusicUtils::formatQueuedTrack).collect(Collectors.joining("\n\n")), false);
		}
		return builder.build();
	}
}

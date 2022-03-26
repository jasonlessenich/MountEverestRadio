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
import net.dv8tion.jda.internal.utils.tuple.Pair;
import youTubeAPI.core.entities.videos.VideosItem;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class QueueCommand extends GuildSlashCommand implements ISlashCommand {
	public QueueCommand() {
		this.setCommandData(Commands.slash("queue", "Shows the current queue."));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		GuildMusicManager player = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		BlockingQueue<AudioTrack> queue = player.scheduler.getQueue();
		event.replyEmbeds(this.buildQueueEmbed(event.getUser(), queue, player)).queue();
	}

	private MessageEmbed buildQueueEmbed(User author, BlockingQueue<AudioTrack> queue, GuildMusicManager player) {
		 EmbedBuilder builder = new EmbedBuilder()
				 .setAuthor(author.getAsTag(), null, author.getEffectiveAvatarUrl())
				 .setTitle(String.format("There are %s songs queued", queue.size()))
				 .setDescription(queue.stream().map(MusicUtils::formatQueuedTrack).collect(Collectors.joining("\n\n")))
				 .setColor(Responses.Type.DEFAULT.getColor())
				 .setTimestamp(Instant.now());
		 AudioTrack track = player.player.getPlayingTrack();
		 if (track != null) {
			 builder.addField(String.format("%sCurrently Playing", player.scheduler.isLoop() ? "\uD83D\uDD02 " : ""), MusicUtils.formatPlayingTrack(track), false);
			 if (!player.scheduler.isLoop()) {
				 builder.setFooter(String.format("Total left: %smin", MusicUtils.convertToMinutes(queue.stream().mapToLong(AudioTrack::getDuration).sum() + (track.getDuration() - track.getPosition()))));
			 }
		 }
		 return builder.build();
	}
}

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

import java.time.Instant;

public class SkipCommand extends GuildSlashCommand implements ISlashCommand {
	public SkipCommand() {
		this.setCommandData(Commands.slash("skip", "Skip the current track."));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		GuildMusicManager guildManager = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		guildManager.scheduler.setTrackLoop(false);
		AudioTrack currentlyPlaying = guildManager.player.getPlayingTrack();
		AudioTrack nextTrack = guildManager.scheduler.getQueue().peek();
		if (currentlyPlaying == null) {
			Responses.respond(event.getHook(), "There is nothing to skip").queue();
			return;
		}
		guildManager.scheduler.nextTrack();
		guildManager.player.setPaused(false);
		event.getHook().sendMessageEmbeds(this.buildSkipEmbed(event.getUser(), currentlyPlaying, nextTrack)).queue();
	}

	private MessageEmbed buildSkipEmbed(User skippedBy, AudioTrack currentlyPlaying, AudioTrack nextTrack) {
		EmbedBuilder builder = new EmbedBuilder()
				.setAuthor(skippedBy.getAsTag(), null, skippedBy.getEffectiveAvatarUrl())
				.setColor(Responses.Type.DEFAULT.getColor())
				.setTimestamp(Instant.now());
		if (currentlyPlaying != null) {
			String title = String.format("Skipped \"%s\"", currentlyPlaying.getInfo().title);
			builder.setTitle(title.substring(0, Math.min(title.length(), MessageEmbed.TITLE_MAX_LENGTH)));
		}
		if (nextTrack != null) {
			builder.addField("Next Up", MusicUtils.formatQueuedTrack(nextTrack), false);
		}
		return builder.build();
	}
}

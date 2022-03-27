package com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.Subcommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.MusicUtils;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class QueueLoopSubcommand extends Subcommand implements ISlashCommand {
	public QueueLoopSubcommand() {
		this.setSubcommandData(new SubcommandData("loop", "Toggles looping for the current queue."));
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
		manager.scheduler.setQueueLoop(!manager.scheduler.isQueueLooping());
		String text = "Enabled";
		if (!manager.scheduler.isQueueLooping()) text = "Disabled";
		Responses.respond(event.getHook(), String.format("%s Queue Loop", text), manager.scheduler.isQueueLooping() ?
				String.format("Looping `%s` songs.%n%n%s", manager.scheduler.getQueue().size() + 1, MusicUtils.formatPlayingTrack(current)) : null).queue();
	}
}

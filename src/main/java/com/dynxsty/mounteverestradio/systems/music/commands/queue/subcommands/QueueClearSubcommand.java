package com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.Subcommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.concurrent.BlockingQueue;

public class QueueClearSubcommand extends Subcommand implements ISlashCommand {
	public QueueClearSubcommand() {
		this.setSubcommandData(new SubcommandData("clear", "Clears the current queue."));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		GuildMusicManager player = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		BlockingQueue<AudioTrack> queue = player.scheduler.getQueue();
		int clearCount = queue.size();
		if (player.player.getPlayingTrack() != null) {
			player.player.stopTrack();
			clearCount++;
		}
		queue.clear();
		Responses.respond(event.getHook(), String.format("Cleared %s songs", clearCount)).queue();

	}
}

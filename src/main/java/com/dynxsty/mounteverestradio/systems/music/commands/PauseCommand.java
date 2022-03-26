package com.dynxsty.mounteverestradio.systems.music.commands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PauseCommand extends GuildSlashCommand implements ISlashCommand {
	public PauseCommand() {
		this.setCommandData(Commands.slash("pause", "Pause/Unpause the current Track"));
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
		manager.player.setPaused(!manager.player.isPaused());
		String text = "Paused";
		if (!manager.player.isPaused()) text = "Unpaused";
		Responses.respond(event.getHook(), String.format("%s current Track", text)).queue();
	}
}

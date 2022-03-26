package com.dynxsty.mounteverestradio.systems.music.commands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.Responses;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class DisconnectCommand extends GuildSlashCommand implements ISlashCommand {
	public DisconnectCommand() {
		this.setCommandData(Commands.slash("disconnect", "Let the Bot leave its current Voice Channel"));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		AudioChannel channel = event.getGuild().getSelfMember().getVoiceState().getChannel();
		if (channel == null) {
			Responses.respond(event.getHook(), "I'm not in a Voice channel").queue();
			return;
		}
		event.getGuild().getAudioManager().closeAudioConnection();
		GuildMusicManager musicManager = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		musicManager.scheduler.getQueue().clear();
		if (musicManager.player.getPlayingTrack() != null) musicManager.player.stopTrack();
		Responses.respond(event.getHook(), "Successfully disconnected from " + channel.getName()).queue();
	}
}

package com.dynxsty.mounteverestradio.systems.music.commands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.utils.Responses;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends GuildSlashCommand implements ISlashCommand {
	public JoinCommand() {
		this.setCommandData(Commands.slash("join", "Let the Bot join your Voice Channel"));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		AudioChannel channel = event.getMember().getVoiceState().getChannel();
		if (channel == null) {
			Responses.respond(event.getHook(), "You're not in a Voice channel").queue();
			return;
		}
		AudioManager audioManager = event.getGuild().getAudioManager();
		audioManager.openAudioConnection(channel);
		audioManager.setSelfDeafened(true);
		Responses.respond(event.getHook(), "I've joined your Voice Channel!").queue();
	}
}

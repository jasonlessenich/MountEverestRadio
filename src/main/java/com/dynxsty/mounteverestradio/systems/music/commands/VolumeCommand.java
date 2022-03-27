package com.dynxsty.mounteverestradio.systems.music.commands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VolumeCommand extends GuildSlashCommand implements ISlashCommand {
	public VolumeCommand() {
		this.setCommandData(Commands.slash("volume", "Control the Bot's Volume")
				.addOptions(new OptionData(OptionType.INTEGER, "volume", "The number the volume should be set to.", false)
						.setMaxValue(200)
				));
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		OptionMapping volumeOption = event.getOption("volume");
		int volume = 100;
		if (volumeOption != null) {
			volume = volumeOption.getAsInt();
		}
		Bot.musicManager.getGuildAudioPlayer(event.getGuild()).player.setVolume(volume);
		String message = String.format("The Volume has been set to %s", volume + "%");
		if (volume > 100) message = message.toUpperCase();
		if (volume < 50) message = String.format("\\*%s\\*", message.toLowerCase());
		Responses.respond(event.getHook(), message).queue();
	}
}

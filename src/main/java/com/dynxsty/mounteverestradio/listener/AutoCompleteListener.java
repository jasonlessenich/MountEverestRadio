package com.dynxsty.mounteverestradio.listener;

import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteListener extends ListenerAdapter {
	@Override
	public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
		switch (event.getName()) {
			case "queue-remove" -> handleQueue(event, event.getGuild());
		}
	}

	private void handleQueue(CommandAutoCompleteInteractionEvent event, Guild guild) {
		GuildMusicManager musicManager = Bot.musicManager.getGuildAudioPlayer(guild);
		List<Command.Choice> choices = new ArrayList<>();
		for (AudioTrack track : musicManager.scheduler.getQueue()) {
			choices.add(new Command.Choice(track.getInfo().title, track.getIdentifier()));
		}
		event.replyChoices(choices).queue();
	}
}

package com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.Subcommand;
import com.dynxsty.mounteverestradio.Bot;
import com.dynxsty.mounteverestradio.systems.music.GuildMusicManager;
import com.dynxsty.mounteverestradio.utils.Responses;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class QueueRemoveSubcommand extends Subcommand implements ISlashCommand {
	public QueueRemoveSubcommand() {
		this.setSubcommandData(new SubcommandData("remove", "Removes a single song from the queue.")
				.addOption(OptionType.STRING, "item", "The song you want to remove.", true, true)
		);
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		OptionMapping itemOption = event.getOption("item");
		if (itemOption == null) {
			Responses.respond(event.getHook(), "Missing required arguments.", null, Responses.Type.ERROR).queue();
			return;
		}
		String item = itemOption.getAsString();
		GuildMusicManager player = Bot.musicManager.getGuildAudioPlayer(event.getGuild());
		BlockingQueue<AudioTrack> queue = player.scheduler.getQueue();
		if (queue.size() == 0) {
			Responses.respond(event.getHook(), "The queue is empty.", null, Responses.Type.ERROR).queue();
			return;
		}
		if (queue.stream().noneMatch(t -> t.getIdentifier().equals(item))) {
			Responses.respond(event.getHook(), "Unknown Track with Identifier: ", item).queue();
			return;
		}
		Optional<AudioTrack> optionalTrack = queue.stream().filter(t -> t.getIdentifier().equals(item)).findFirst();
		if (optionalTrack.isEmpty()) {
			Responses.respond(event.getHook(), "Unknown Track with Identifier: ", item).queue();
			return;
		}
		queue.remove(optionalTrack.get());
		Responses.respond(event.getHook(), String.format("Removed \"%s\"", optionalTrack.get().getInfo().title)).queue();
	}
}

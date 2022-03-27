package com.dynxsty.mounteverestradio.systems.music.commands.queue;

import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands.QueueClearSubcommand;
import com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands.QueueListSubcommand;
import com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands.QueueLoopSubcommand;
import com.dynxsty.mounteverestradio.systems.music.commands.queue.subcommands.QueueRemoveSubcommand;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class QueueCommand extends GuildSlashCommand {
	public QueueCommand() {
		this.setCommandData(Commands.slash("queue", "Commands for managing the current queue."));
		this.setSubcommands(QueueClearSubcommand.class, QueueListSubcommand.class, QueueLoopSubcommand.class, QueueRemoveSubcommand.class);
	}
}

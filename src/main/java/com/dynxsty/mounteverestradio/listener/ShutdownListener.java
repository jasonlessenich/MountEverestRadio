package com.dynxsty.mounteverestradio.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ShutdownListener extends ListenerAdapter {
	@Override
	public void onShutdown(@NotNull ShutdownEvent event) {
		for (Guild guild : event.getJDA().getGuilds()) {
			guild.getAudioManager().closeAudioConnection();
		}
	}
}

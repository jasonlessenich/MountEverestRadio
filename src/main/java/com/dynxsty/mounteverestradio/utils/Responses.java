package com.dynxsty.mounteverestradio.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import javax.annotation.Nullable;
import java.awt.*;

public class Responses {

	public enum Type {
		DEFAULT(Color.decode("#2F3136")),
		INFO(Color.decode("#34A2EB")),
		WARN(Color.decode("#EBA434")),
		ERROR(Color.decode("#EB3434"));

		private final Color color;
		Type(Color color) {
			this.color = color;
		}
		public Color getColor() {
			return this.color;
		}
	}

	public static ReplyCallbackAction respond(SlashCommandInteractionEvent event, String title) {
		return respond(event, title, null, Type.DEFAULT);
	}

	public static ReplyCallbackAction respond(SlashCommandInteractionEvent event, String title, @Nullable String description) {
		return respond(event, title, description, Type.DEFAULT);
	}

	public static ReplyCallbackAction respond(SlashCommandInteractionEvent event, String title, @Nullable String description, Type type) {
		return event.replyEmbeds(buildRespondEmbed(event.getUser(), title, description, type.getColor()));
	}

	public static WebhookMessageAction<Message> respond(InteractionHook hook, String title) {
		return respond(hook, title, null, Type.DEFAULT);
	}

	public static WebhookMessageAction<Message> respond(InteractionHook hook, String title, @Nullable String description) {
		return respond(hook, title, description, Type.DEFAULT);
	}

	public static WebhookMessageAction<Message> respond(InteractionHook hook, String title, @Nullable String description, Type type) {
		return hook.sendMessageEmbeds(buildRespondEmbed(hook.getInteraction().getUser(), title, description, type.getColor()));
	}

	private static MessageEmbed buildRespondEmbed(User author, String title, @Nullable String description, Color color) {
		return new EmbedBuilder()
				.setAuthor(author.getAsTag(), null, author.getEffectiveAvatarUrl())
				.setColor(color)
				.setTitle(title)
				.setDescription(description)
				.build();
	}
}

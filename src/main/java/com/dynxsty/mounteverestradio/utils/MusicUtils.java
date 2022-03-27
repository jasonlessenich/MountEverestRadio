package com.dynxsty.mounteverestradio.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import youTubeAPI.core.entities.Thumbnails;
import youTubeAPI.core.entities.search.Item;
import youTubeAPI.core.entities.videos.VideosItem;

public class MusicUtils {

	public static String formatPlayingTrack(AudioTrack track) {
		return String.format("**[%s](%s)**%nby `%s` `(%s/%s)`",
				track.getInfo().title,
				track.getInfo().uri,
				track.getInfo().author,
				MusicUtils.convertToMinutes(track.getPosition()),
				MusicUtils.convertToMinutes(track.getDuration()));
	}

	public static String formatQueuedTrack(AudioTrack track) {
		return String.format("**[%s](%s)**%nby `%s` `(%s)`",
				track.getInfo().title,
				track.getInfo().uri,
				track.getInfo().author,
				MusicUtils.convertToMinutes(track.getDuration()));
	}

	public static MessageEmbed convertToEmbed(User requestedBy, String title, AudioTrack track, Thumbnails thumbnails, String channelId) {
		return new EmbedBuilder()
				.setAuthor(requestedBy.getAsTag(), null, requestedBy.getEffectiveAvatarUrl())
				.setTitle(title)
				.setImage(thumbnails == null ? null : getHighestResolutionThumbnail(thumbnails))
				.setColor(Responses.Type.DEFAULT.getColor())
				.setDescription(String.format("[%s](%s)", track.getInfo().title, track.getInfo().uri))
				.addField("Author", String.format("[%s](https://youtube.com/channel/%s)", track.getInfo().author, channelId), true)
				.addField("Length", MusicUtils.convertToMinutes(track.getDuration()) + " min", true)
				.build();
	}

	public static MessageEmbed convertToEmbed(User requestedBy, String title, AudioTrack track, Item result) {
		return convertToEmbed(requestedBy, title, track, result.getInfo().getThumbnails(), result.getInfo().channelId);
	}

	public static MessageEmbed convertToEmbed(User requestedBy, String title, AudioTrack track, VideosItem result) {
		return convertToEmbed(requestedBy, title, track, result.getInfo().getThumbnails(), result.getInfo().channelId);
	}

	public static String convertToMinutes(long milliseconds) {
		long minutes = (milliseconds / 1000) / 60;
		long seconds = (milliseconds / 1000) % 60;
		String secondsStr = Long.toString(seconds);
		String secs;
		if (secondsStr.length() >= 2) {
			secs = secondsStr.substring(0, 2);
		} else secs = "0" + secondsStr;
		return minutes + ":" + secs;
	}

	public static String getHighestResolutionThumbnail(Thumbnails thumbnails) {
		if (thumbnails == null) return null;
		if (thumbnails.getMaxres() != null) {
			return thumbnails.getMaxres().url;
		} else if (thumbnails.getHigh() != null) {
			return thumbnails.getHigh().url;
		} else if (thumbnails.getMedium() != null) {
			return thumbnails.getMedium().url;
		} else if (thumbnails.getStandard() != null) {
			return thumbnails.getStandard().url;
		} else if (thumbnails.getDefault() != null) {
			return thumbnails.getDefault().url;
		}
		return null;
	}
}

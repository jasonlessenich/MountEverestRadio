package com.dynxsty.mounteverestradio.systems.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;

	private boolean loop = false;

	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	public void queue(AudioTrack audioTrack) {
		if (!player.startTrack(audioTrack, true)) {
			queue.offer(audioTrack);
		}
	}

	public void nextTrack() {
		player.startTrack(queue.poll(), false);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (loop && track != null) {
			this.player.startTrack(track.makeClone(), false);
		} else if (endReason.mayStartNext) {
			this.nextTrack();
		}
	}

	public BlockingQueue<AudioTrack> getQueue() {
		return this.queue;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public boolean isLoop() {
		return this.loop;
	}
}

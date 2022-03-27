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

	private boolean trackLoop = false;
	private boolean queueLoop = false;

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
		if (queueLoop && track != null) {
			queue.offer(track.makeClone());
		}
		if (trackLoop && track != null) {
			this.player.startTrack(track.makeClone(), false);
		} else if (endReason.mayStartNext) {
			this.nextTrack();
		}
	}

	public BlockingQueue<AudioTrack> getQueue() {
		return this.queue;
	}

	public void setTrackLoop(boolean loop) {
		if (loop) this.setQueueLoop(false);
		this.trackLoop = loop;
	}

	public boolean isTrackLooping() {
		return this.trackLoop;
	}

	public void setQueueLoop(boolean loop) {
		if (loop) this.setTrackLoop(false);
		this.queueLoop = loop;
	}

	public boolean isQueueLooping() {
		return this.queueLoop;
	}
}

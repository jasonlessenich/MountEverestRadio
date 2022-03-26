package com.dynxsty.mounteverestradio.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The base container class for all the Bot's configuration.
 */
@Slf4j
public class BotConfig {
	private static final String SYSTEMS_FILE = "systems.json";

	/**
	 * Global configuration settings for the bot which are not guild-specific.
	 */
	private final SystemsConfig systemsConfig;

	/**
	 * The path from which the config was loaded.
	 */
	private final Path dir;

	/**
	 * Constructs a new empty configuration.
	 *
	 * @param dir The path to the directory containing the guild configuration
	 *            files.
	 */
	public BotConfig(Path dir) {
		this.dir = dir;
		if (!(Files.exists(dir) && Files.isDirectory(dir))) {
			if (!Files.exists(dir)) {
				try {
					Files.createDirectories(dir);
				} catch (IOException e) {
					log.error("Could not create config directory " + dir, e);
				}
			} else {
				log.error("File exists at config directory path {}", dir);
			}
		}
		Gson gson = new Gson();
		Path systemsFile = dir.resolve(SYSTEMS_FILE);
		if (Files.exists(systemsFile)) {
			try (var reader = Files.newBufferedReader(systemsFile)) {
				this.systemsConfig = gson.fromJson(reader, SystemsConfig.class);
				log.info("Loaded systems config from {}", systemsFile);
			} catch (JsonSyntaxException e) {
				log.error("Invalid JSON found! Please fix or remove config file " + systemsFile + " and restart.", e);
				throw e;
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		} else {
			log.info("No systems config file found. Creating an empty one at {}", systemsFile);
			this.systemsConfig = new SystemsConfig();
			this.flush();
		}
	}

	public SystemsConfig getSystems() {
		return this.systemsConfig;
	}

	/**
	 * Flushes all configuration to the disk.
	 */
	public void flush() {
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		Path systemsFile = this.dir.resolve(SYSTEMS_FILE);
		try (var writer = Files.newBufferedWriter(systemsFile)) {
			gson.toJson(this.systemsConfig, writer);
			writer.flush();
		} catch (IOException e) {
			log.error("Could not save systems config.", e);
		}
	}
}

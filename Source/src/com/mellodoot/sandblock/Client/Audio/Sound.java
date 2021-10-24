package com.mellodoot.sandblock.Client.Audio;

// import java.io.File;
import java.io.IOException;
import java.net.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	// private File file;
	private Clip clip;
	private AudioInputStream audio;
	
	public Sound(String path) {
		//sound = Applet.newAudioClip(Sound.class.getResource(name));
		try {
			clip = AudioSystem.getClip();
			audio = AudioSystem.getAudioInputStream(new URL(path));
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	public void play(boolean loop) {
		try {
			if (!clip.isOpen()) clip.open(audio);
			clip.start();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		if (loop) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	public void stop() {
		if (clip.isOpen()) {
			clip.stop();
			clip.close();
		}
	}
	
	public boolean isPlaying() {
		return clip.isRunning();
	}
	
}

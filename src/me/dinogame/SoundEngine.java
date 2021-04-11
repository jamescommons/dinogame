package me.dinogame;

import javax.sound.sampled.*;
import java.io.IOException;

public class SoundEngine {
	private Clip line;
	private AudioInputStream audioInputStream;
	
	public void playSound(String path) {
		
		// Set audioInputStream
		try {
			audioInputStream = AudioSystem.getAudioInputStream(
					getClass().getResource(path));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		DataLine.Info info = new DataLine.Info(Clip.class,
				audioInputStream.getFormat()); // format is an AudioFormat object
		if (!AudioSystem.isLineSupported(info)) {
			System.err.println("Type of line required is unsupported");
		}
		
		// Obtain and open the line.
		try {
			line = (Clip)AudioSystem.getLine(info);
			line.open(audioInputStream);
		} catch (LineUnavailableException | IOException e) {
			System.err.println("Line is unavailable.");
		}
		
		line.start();
	}
}

package me.dinogame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Extends JPanel, set layout manager to null
public class GraphicsPanel extends JPanel {
	private final BufferedImage ground1img, ground2img, ground3img;
	
	// Used by drawGround()
	private BufferedImage imgFirst, imgMiddle, imgLast;
	private final BufferedImage imgLastBuffer;
	private final BufferedImage endScreen;
	private final BufferedImage highScore;
	
	// A field used to track the position of the ground (middle image position)
	private int groundXPos;
	
	// No args constructor
	public GraphicsPanel() {
		super();
		setDoubleBuffered(true);
		ground1img = getImageFromJar("/images/Ground1.png");
		ground2img = getImageFromJar("/images/Ground2.png");
		ground3img = getImageFromJar("/images/Ground3.png");
		endScreen = getImageFromJar("/images/EndScreen.png");
		highScore = getImageFromJar("/images/HighScore.png");
		imgFirst = ground1img;
		imgMiddle = ground2img;
		imgLast = ground3img;
		imgLastBuffer = ground1img;
		groundXPos = (getWidth() - imgMiddle.getWidth()) / 2;
	}
	
	// Returns the ground level relative to JPanel
	public int getGroundLevel() {
		return (getHeight() / 2) + 24;
	}
	
	// Get images from JAR
	public BufferedImage getImageFromJar(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Draw ground
		drawGround(g);
		
		// Draw dino
		DinoGame.getPlayer().draw(g);
		
		// Draw obstacles
		GameEngine.drawObstacles(g);
		
		// Draw score
		g.drawImage(highScore, getWidth() - 150, 25,
				highScore.getWidth() / 2, highScore.getHeight() / 2,
				(img, infoflags, x, y, width, height) -> false);
		Font font = new Font("Arial", Font.BOLD, 18);
		String str = String.format("%05d", DinoGame.getHighScore());
		g.setColor(Color.getColor("Grey", 0x909191));
		g.setFont(font);
		g.drawString(str, getWidth() - 120, 36);
		str = String.format("%05d", DinoGame.getScore());
		g.drawString(str, getWidth() - 60, 36);
		
		// Draw end screen
		if (DinoGame.getGamesPlayed() > 0 && !DinoGame.getGameRunning()) {
			drawEndScreen(g);
		}
	}
	
	public void drawGround(Graphics g) {
		
		// If the middle image is beyond the panel
		if (groundXPos < -imgMiddle.getWidth() + 20) {
			
			// Switch images around
			imgMiddle = imgLast;
			imgFirst = imgMiddle;
			switch ((int)(Math.random() * 3)) {
				case 0 -> imgLast = ground1img;
				case 1 -> imgLast = ground2img;
				case 2 -> imgLast = ground3img;
			}
			
			groundXPos = 0;
		}
		
		// Get groundVelocity
		int groundVelocity = DinoGame.getGroundObstacleVelocity();
		
		// Start position
		int yPos = getGroundLevel();
		
		groundXPos -= groundVelocity;
		
		// Draw images
		g.drawImage(imgMiddle, groundXPos, yPos,
				(img, infoflags, x, y, width, height) -> false);
		g.drawImage(imgFirst, groundXPos - imgFirst.getWidth(), yPos,
				(img, infoflags, x, y, width, height) -> false);
		g.drawImage(imgLast, groundXPos + imgMiddle.getWidth(), yPos,
				(img, infoflags, x, y, width, height) -> false);
		g.drawImage(imgLastBuffer, groundXPos + (imgMiddle.getWidth() * 2),
				yPos, (img, infoflags, x, y, width, height) -> false);
	}
	
	public void drawEndScreen(Graphics g) {
		g.drawImage(endScreen, (getWidth() / 2) - (endScreen.getWidth() / 4),
				((getHeight() / 2) - (endScreen.getHeight() / 4)) - 50,
				endScreen.getWidth() / 2, endScreen.getHeight() / 2,
				(img, infoflags, x, y, width, height) -> false);
	}
}

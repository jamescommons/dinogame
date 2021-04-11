package me.dinogame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Obstacle {
	protected int xPos, yPos;
	
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
	
	public abstract void draw(Graphics g);
	public abstract Rectangle getRect();
	
	// Velocity is actually speed
	public void move() {
		xPos -= DinoGame.getGroundObstacleVelocity();
	}
}

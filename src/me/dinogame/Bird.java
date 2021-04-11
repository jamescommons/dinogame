package me.dinogame;

import java.awt.*;
import java.awt.image.BufferedImage;

// extends Obstacle
public class Bird extends Obstacle {
	private final BufferedImage flying1, flying2;
	private int whichImg = 1; // -1 = 2
	private int relativeYPos;
	
	public Bird(int x, int y) {
		flying1 = getImageFromJar("/images/Bird1.png");
		flying2 = getImageFromJar("/images/Bird2.png");
		xPos = x;
		switch (y) {
			case 0 -> relativeYPos = 10;
			case 1 -> relativeYPos = 40;
			case 2 -> relativeYPos = 70;
		}
	}
	
	// Used for collision detection
	@Override
	public Rectangle getRect() {
		return new Rectangle(xPos - 30, yPos - 10, (flying1.getWidth() / 2) + 30,
				(flying1.getHeight() / 2) + 10);
	}
	
	@Override
	public void draw(Graphics g) {
		if (DinoGame.getScore() % 4 == 0) {
			whichImg *= -1;
		}
		
		BufferedImage image;
		if (whichImg == 1) {
			image = flying1;
		} else {
			image = flying2;
		}
		
		yPos = (DinoGame.getPanel().getGroundLevel() -
				(image.getHeight() / 2) + 17) - relativeYPos;
		
		g.drawImage(image, xPos, yPos,
				image.getWidth() / 2, image.getHeight() / 2,
				(img, infoflags, x, y, width, height) -> false);
	}
}

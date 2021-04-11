package me.dinogame;

import java.awt.*;
import java.awt.image.BufferedImage;

// extends Obstacle
public class Cactus extends Obstacle {
	private final BufferedImage image;
	
	// refer to cactus by image name
	public Cactus(String whichCactus, int x) {
		image = getImageFromJar(whichCactus);
		xPos = x;
	}
	
	// Used for collision detection
	@Override
	public Rectangle getRect() {
		if (image.getWidth() < 40) {
			return new Rectangle(xPos - 20, yPos - 20,
					(image.getWidth() / 2) + 20,
					(image.getHeight() / 2) + 20);
		}
		
		return new Rectangle(xPos, yPos, image.getWidth() / 2,
				image.getHeight() / 2);
	}
	
	@Override
	public void draw(Graphics g) {
		
		// Get yPos
		yPos = (DinoGame.getPanel().getGroundLevel() -
				(image.getHeight() / 2) + 17);
		
		g.drawImage(image, xPos, yPos,
				image.getWidth() / 2, image.getHeight() / 2,
				(img, infoflags, x, y, width, height) -> false);
	}
}

package me.dinogame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Dinosaur {
	private final int xPos;
	private int relativeYPos;
	private boolean isDucking;
	private final BufferedImage dinoPic, dinoRunning1,
			dinoRunning2, dinoDucking1, dinoDucking2;
	private BufferedImage currentImg;
	private int whichImg = 1; // -1 = 2
	private int yVelocity, yAcceleration;
	
	public Dinosaur(int x) {
		xPos = x;
		relativeYPos = 0;
		isDucking = false;
		dinoPic = getImageFromJar("/images/Dino.png");
		dinoRunning1 = getImageFromJar("/images/DinoRunning1.png");
		dinoRunning2 = getImageFromJar("/images/DinoRunning2.png");
		dinoDucking1 = getImageFromJar("/images/DinoDucking1.png");
		dinoDucking2 = getImageFromJar("/images/DinoDucking2.png");
	}
	
	// Used for collision detection
	public Rectangle getRect() {
		return new Rectangle(xPos, (DinoGame.getPanel().getGroundLevel() -
				(currentImg.getHeight() / 2) + 17) - relativeYPos,
				currentImg.getWidth() / 2, currentImg.getHeight() / 2);
	}
	
	public int getRelativeYPos() {
		return relativeYPos;
	}
	
	public void setRelativeYPos(int y) {
		relativeYPos = y;
	}
	
	public void setDucking(boolean isDucking) {
		this.isDucking = isDucking;
	}
	
	public void switchImage() {
		whichImg *= -1;
	}
	
	public void resetImage() {
		currentImg = dinoPic;
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
	
	public void draw(Graphics g) {
		
		// The current image of the dino
		// Changes depending on running or not, etc.
		currentImg = dinoPic;
		if (DinoGame.getGameRunning()) {
			if (isDucking) {
				if (whichImg == 1) {
					currentImg = dinoDucking1;
				} else {
					currentImg = dinoDucking2;
				}
			} else {
				if (whichImg == 1) {
					currentImg = dinoRunning1;
				} else {
					currentImg = dinoRunning2;
				}
			}
		}
		
		int yPos = (DinoGame.getPanel().getGroundLevel() -
				(currentImg.getHeight() / 2) + 17) - relativeYPos;
		
		g.drawImage(currentImg, xPos, yPos, currentImg.getWidth() / 2,
				currentImg.getHeight() / 2,
				(img, infoflags, x, y, width, height) -> false);
	}
	
	public void jump() {
		yAcceleration = 0;
		yVelocity = 20;
		relativeYPos += yVelocity;
	}
	
	public void fall() {
		yAcceleration = -3;
		yVelocity += yAcceleration;
		relativeYPos += yVelocity;
	}
}

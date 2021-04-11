package me.dinogame;

import java.awt.*;

public class GameEngine {
	private static boolean requestDinoDuck;
	private static boolean requestDinoJump;
	private static boolean dinoMovingDown;
	private static boolean dinoMovingUp;
	private static boolean shouldPlayJumpSound;
	private static int timer;
	private static Obstacle obstacle1, obstacle2, obstacle3;
	
	public static void requestingDinoDuck(boolean bool) {
		requestDinoDuck = bool;
	}
	
	public static void requestingDinoJump(boolean bool) {
		requestDinoJump = bool;
		if (!bool) {
			dinoMovingDown = true;
		}
	}
	
	public static void moveDino() {
		
		// If dino is on the ground
		if (DinoGame.getPlayer().getRelativeYPos() == 0) {
			DinoGame.getPlayer().setDucking(requestDinoDuck);
		} else {
			DinoGame.getPlayer().setDucking(false);
		}
		
		// If dino is on ground and not jumping
		if (DinoGame.getPlayer().getRelativeYPos() <= 20 &&
				!dinoMovingUp) {
			dinoMovingDown = false;
			DinoGame.getPlayer().setRelativeYPos(0);
			shouldPlayJumpSound = true;
		}
		
		// If user wants to jump
		if (requestDinoJump) {
			if (!dinoMovingDown) {
				if (DinoGame.getPlayer().getRelativeYPos() < 60) {
					DinoGame.getPlayer().jump();
					dinoMovingUp = true;
					if (shouldPlayJumpSound) {
						DinoGame.getSoundEngine().playSound(
								"/soundeffects/Jump.wav");
						shouldPlayJumpSound = false;
					}
				} else {
					dinoMovingDown = true;
				}
			}
		}
		
		// If dino is moving down
		if (dinoMovingDown) {
			DinoGame.getPlayer().fall();
			dinoMovingUp = false;
		}
	}
	
	public static void initializeObstacles() {
		obstacle1 = null;
		obstacle2 = null;
		obstacle3 = null;
	}
	
	public static void addObstacle() {
		if (obstacle3 == null) {
			if (DinoGame.getScore() < 200) {
				switch ((int)(Math.random() * 4)) {
					case 0 -> obstacle3 = new Cactus(
							"/images/1CactusLarge.png", 2400);
					case 1 ->  obstacle3 = new Cactus(
							"/images/1CactusSmall.png", 2400);
					case 2 -> obstacle3 = new Cactus(
							"/images/2Cactus.png", 2400);
					case 3 -> obstacle3 = new Cactus(
							"/images/3Cactus.png", 2400);
				}
			} else if (DinoGame.getScore() < 400) {
				switch ((int)(Math.random() * 5)) {
					case 0 -> obstacle3 = new Cactus(
							"/images/1CactusLarge.png", 2400);
					case 1 ->  obstacle3 = new Cactus(
							"/images/1CactusSmall.png", 2400);
					case 2 ->  obstacle3 = new Cactus(
							"/images/2Cactus.png", 2400);
					case 3 -> obstacle3 = new Cactus(
							"/images/3Cactus.png", 2400);
					case 4 -> obstacle3 = new Cactus(
							"/images/4Cactus.png", 2400);
				}
			} else {
				switch ((int)(Math.random() * 6)) {
					case 0 -> obstacle3 = new Cactus(
							"/images/1CactusLarge.png", 2400);
					case 1 ->  obstacle3 = new Cactus(
							"/images/1CactusSmall.png", 2400);
					case 2 ->  obstacle3 = new Cactus(
							"/images/2Cactus.png", 2400);
					case 3 -> obstacle3 = new Cactus(
							"/images/3Cactus.png", 2400);
					case 4 -> obstacle3 = new Cactus(
							"/images/4Cactus.png", 2400);
					case 5 -> obstacle3 = new Bird(2400,
							(int)(Math.random() * 3));
				}
			}
		}
	}
	
	// Add obstacle if need and move obstacles
	public static void addAndMoveObstacles() {
		
		// Move obstacles
		if (obstacle1 != null) {
			obstacle1.move();
		}
		if (obstacle2 != null) {
			obstacle2.move();
		}
		if (obstacle3 != null) {
			obstacle3.move();
		}
		
		// Add obstacles
		// Create a gap
		if (timer <= 20) {
			timer ++;
		} else {
			
			// Change this random number to increase
			// or decrease the frequency of obstacles
			if ((int)(Math.random() * 4) == 0) {
				addObstacle();
				timer = 0;
			}
		}
		
		// Destroy and shuffle obstacles
		if (obstacle2 == null) {
			obstacle2 = obstacle3;
			obstacle3 = null;
		}
		if (obstacle1 == null) {
			obstacle1 = obstacle2;
			obstacle2 = obstacle3;
			obstacle3 = null;
		}
		if (obstacle1 != null) {
			if (obstacle1.xPos < -50) {
				obstacle1 = null;
			}
		}
	}
	
	public static void drawObstacles(Graphics g) {
		if (obstacle1 != null) {
			obstacle1.draw(g);
		}
		if (obstacle2 != null) {
			obstacle2.draw(g);
		}
		if (obstacle3 != null) {
			obstacle3.draw(g);
		}
	}
	
	// This just sucks
	public static void getCollisions() {
		Rectangle dinoRect = DinoGame.getPlayer().getRect();
		
		if (obstacle1 != null) {
			Point p1 = new Point(dinoRect.x, dinoRect.y);
			Point p2 = new Point(dinoRect.x, dinoRect.y + dinoRect.height);
			Point p3 = new Point(dinoRect.x + dinoRect.width, dinoRect.y);
			Point p4 = new Point(dinoRect.x + dinoRect.width,
					dinoRect.y + dinoRect.width);
			Point p5 = new Point(dinoRect.x + (dinoRect.width / 2),
					dinoRect.y + dinoRect.height);
			Point p6 = new Point((int)(dinoRect.x + (dinoRect.getWidth() / 2)), dinoRect.y);
			if (obstacle1.getRect().contains(p1) || obstacle1.getRect().contains(p2) ||
					obstacle1.getRect().contains(p3) || obstacle1.getRect().contains(p4)
					|| obstacle1.getRect().contains(p5) || obstacle1.getRect().contains(p6)) {
				DinoGame.stopGame();
			}
		}
		
		if (obstacle2 != null) {
			Point p1 = new Point(dinoRect.x, dinoRect.y);
			Point p2 = new Point(dinoRect.x, dinoRect.y + dinoRect.height);
			Point p3 = new Point(dinoRect.x + dinoRect.width, dinoRect.y);
			Point p4 = new Point(dinoRect.x + dinoRect.width,
					dinoRect.y + dinoRect.width);
			Point p5 = new Point(dinoRect.x + (dinoRect.width / 2),
					dinoRect.y + dinoRect.height);
			Point p6 = new Point((int)(dinoRect.x + (dinoRect.getWidth() / 2)), dinoRect.y);
			if (obstacle2.getRect().contains(p1) || obstacle2.getRect().contains(p2) ||
					obstacle2.getRect().contains(p3) || obstacle2.getRect().contains(p4)
					|| obstacle2.getRect().contains(p5) || obstacle2.getRect().contains(p6)) {
				DinoGame.stopGame();
			}
		}
		
		if (obstacle3 != null) {
			Point p1 = new Point(dinoRect.x, dinoRect.y);
			Point p2 = new Point(dinoRect.x, dinoRect.y + dinoRect.height);
			Point p3 = new Point(dinoRect.x + dinoRect.width, dinoRect.y);
			Point p4 = new Point(dinoRect.x + dinoRect.width,
					dinoRect.y + dinoRect.width);
			Point p5 = new Point(dinoRect.x + (dinoRect.width / 2),
					dinoRect.y + dinoRect.height);
			Point p6 = new Point((int)(dinoRect.x + (dinoRect.getWidth() / 2)), dinoRect.y);
			if (obstacle3.getRect().contains(p1) || obstacle3.getRect().contains(p2) ||
					obstacle3.getRect().contains(p3) || obstacle3.getRect().contains(p4)
					|| obstacle3.getRect().contains(p5) || obstacle3.getRect().contains(p6)) {
				DinoGame.stopGame();
			}
		}
	}
}

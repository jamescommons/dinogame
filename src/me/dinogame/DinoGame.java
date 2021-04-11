/* Known issues:
 * I know the obstacles come in pairs of three. The way to fix this is to
 * add more obstacles, but I am lazy.
 *
 * The jump is a little too high, but this is intentional to demonstrated
 * the variability in jump height available.
 *
 * There is lag on the very first jump, and this has to do with loading in
 * sound effects. This is also a relatively easy fix, but I would have to
 * rewrite the sound engine.
 */

package me.dinogame;

//import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//import java.awt.image.BufferedImage;
//import java.io.IOException;

// Static
public class DinoGame {
	public static final Dimension SCREEN_SIZE =
			Toolkit.getDefaultToolkit().getScreenSize();
	public static final double TICK_RATE = 20.0; // Per second
	private static JFrame window;
	private static int score, highScore;
	private static int ticks; // Game time
	private static int groundObstacleVelocity;
	private static boolean isGameRunning;
	private static GraphicsPanel panel;
	private static Dinosaur player;
	private static int gamesPlayed = 0;
	private static SoundEngine soundEngine;
	
	public static SoundEngine getSoundEngine() {
		return soundEngine;
	}
	
	public static int getGamesPlayed() {
		return gamesPlayed;
	}
	
	public static int getHighScore() {
		return highScore;
	}
	
	public static int getScore() {
		return score;
	}
	
	public static GraphicsPanel getPanel() {
		return panel;
	}
	
	public static Dinosaur getPlayer() {
		return player;
	}
	
	public static boolean getGameRunning() {
		return isGameRunning;
	}
	
	public static int getGroundObstacleVelocity() {
		return groundObstacleVelocity;
	}
	
//	// Get images from JAR
//	public static BufferedImage getImageFromJar(String path) {
//		BufferedImage img = null;
//		try {
//			img = ImageIO.read(window.getClass().getResourceAsStream(path));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return img;
//	}
	
	public static void addMainMenuKeyLister() {
		window.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER ||
						e.getKeyCode() == KeyEvent.VK_SPACE ||
						e.getKeyCode() == KeyEvent.VK_UP) {
					startGame();
				}
			}
			
			// Do nothing
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		
		// Add a MouseEvent listener to register clicks
		window.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				startGame();
			}
			
			// Do nothing
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}
	
	// Send signals to relevant classes that game has started
	// Start game timer
	public static void startGame() {
		score = 0;
		
		// Remove the KeyListener from the window
		window.removeKeyListener(window.getKeyListeners()[0]);
		window.removeMouseListener(window.getMouseListeners()[0]);
		
		// Add a new key listener (User input for game)
		window.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					GameEngine.requestingDinoDuck(true);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_UP ||
						e.getKeyCode() == KeyEvent.VK_SPACE) {
					GameEngine.requestingDinoJump(true);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					GameEngine.requestingDinoDuck(false);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_UP ||
						e.getKeyCode() == KeyEvent.VK_SPACE) {
					GameEngine.requestingDinoJump(false);
				}
			}
		});
		
		player.resetImage();
		GameEngine.initializeObstacles();
		
		isGameRunning = true;
		
		SwingWorker<Void, Void> worker = new SwingWorker<>() {
			@Override
			protected Void doInBackground() {
				GameClock clock = new GameClock();
				clock.start();
				return null;
			}
		};
		worker.execute();
	}
	
	// Send signals to relevant classes that game has stopped
	// Stop game timer
	public static void stopGame() {
		soundEngine.playSound("/soundeffects/EndGame.wav");
		isGameRunning = false;
		ticks = 0;
		gamesPlayed ++;
		if (score > highScore) {
			highScore = score;
		}
		
		// Remove user input key listener
		window.removeKeyListener(window.getKeyListeners()[0]);
		addMainMenuKeyLister();
		panel.repaint();
	}
	
	public static void tick() {
		ticks ++;
		if (ticks % 2 == 0) {
			score ++;
			player.switchImage();
		}
		
		if (ticks % 200 == 0 && score != 0) {
			soundEngine.playSound("/soundeffects/Score100.wav");
		}
		
		// Set ground and obstacle velocity
		int maxVelocity = 30;
		int startVelocity = 15;
		
		// Logistic function
		groundObstacleVelocity = (int)((maxVelocity /
				(1 + Math.pow(Math.E, -.001 * (ticks - 3000)))) + startVelocity);
		
		panel.repaint();
		GameEngine.moveDino();
		GameEngine.addAndMoveObstacles();
		GameEngine.getCollisions();
	}
	
	public static void createAndShowWindow() {
		
		// Create window
		window = new JFrame("Dino Game");
		window.setBounds(SCREEN_SIZE.width / 4, 100,
				SCREEN_SIZE.width / 2, SCREEN_SIZE.height / 2);
		window.setMinimumSize(new Dimension(150, 150));
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Add GraphicsPanel to window
		panel = new GraphicsPanel();
		window.setIconImage(panel.getImageFromJar("/images/Dino.png"));
		window.getContentPane().add(panel);
		panel.repaint();
		
		// Add KeyEvent listener to window
		addMainMenuKeyLister();
		
		// Set window visibility
		window.setVisible(true);
		
		// Create player and obstacles
		player = new Dinosaur(100);
		
		// Create sound engine
		soundEngine = new SoundEngine();
	}
	
	public static void main(String[] args) {
		
		// Create game window
		SwingUtilities.invokeLater(DinoGame::createAndShowWindow);
	}
}

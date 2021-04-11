package me.dinogame;

// extends Thread
public class GameClock extends Thread {
	
	@Override
	public synchronized void start() {
		super.start();
		
		// Find the number of milliseconds per tick
		int millisPerTick = (int)(1000 / DinoGame.TICK_RATE);
		while (DinoGame.getGameRunning()) {
			long startTime = System.currentTimeMillis();
			
			DinoGame.tick();
			
			long elapsed = System.currentTimeMillis() - startTime;
			try {
				if (millisPerTick - elapsed > 0) {
					//noinspection BusyWait
					sleep((millisPerTick - elapsed));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // end of while loop
	}
}

package userinterface;
import javax.swing.*;
import java.util.Random;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPanel;

import gameobject.Clouds;
import gameobject.CoinsManager;
import gameobject.CoinsManager1;
import gameobject.CoinsManager2;
import gameobject.EnemiesManager;
import gameobject.Land;
import gameobject.MainCharacter;
import gameobject.SpCoinManager;
import util.Resource;

public class GameScreen extends JPanel implements Runnable, KeyListener {

	private static final int START_GAME_STATE = 0;
	private static final int GAME_PLAYING_STATE = 1;
	private static final int GAME_OVER_STATE = 2;
	private Random rand;
	private Land land;
	private MainCharacter mainCharacter;
	private CoinsManager coinsManager;
	private CoinsManager1 coinsManager1;
	private CoinsManager2 coinsManager2;
	private SpCoinManager scm;
	private EnemiesManager enemiesManager;
	private Clouds clouds;
	private Thread thread;
	private boolean isKeyPressed;

	private int gameState = START_GAME_STATE;

	private AudioClip startsound;
	private BufferedImage replayButtonImage;
	private BufferedImage gameOverButtonImage;
	private BufferedImage startingbuttonImage;

	public GameScreen() {
		mainCharacter = new MainCharacter();
		land = new Land(GameWindow.SCREEN_WIDTH, mainCharacter);
		mainCharacter.setSpeedX(4);
		startingbuttonImage = Resource.getResouceImage("data/start.png");
		replayButtonImage = Resource.getResouceImage("data/replay.png");
		gameOverButtonImage = Resource.getResouceImage("data/gameover_txt.png");
		enemiesManager = new EnemiesManager(mainCharacter);
		coinsManager = new CoinsManager(mainCharacter);
		coinsManager1 = new CoinsManager1(mainCharacter);
		coinsManager2 = new CoinsManager2(mainCharacter);
		scm = new SpCoinManager(mainCharacter);
		
		
		clouds = new Clouds(GameWindow.SCREEN_WIDTH, mainCharacter);
		try {
			startsound =  Applet.newAudioClip(new URL("file","","data/desert.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	 public void playtrack() {
		 startsound.play();
	 }
	public void startGame() {
		thread = new Thread(this);
		thread.start();
	}

	public void gameUpdate() {
		if (gameState == GAME_PLAYING_STATE) {
			clouds.update();
			land.update();
			mainCharacter.update();
			enemiesManager.update();
			coinsManager.update();
			coinsManager1.update();
			coinsManager2.update();
			scm.update();
			
			if (enemiesManager.isCollision()) {
				mainCharacter.playDeadSound();
				gameState = GAME_OVER_STATE;
				mainCharacter.dead(true);
			}
			if (coinsManager.isCollision()) {
				coinsManager.remove();
				mainCharacter.upScore();
				mainCharacter.playcoin();
				
				
			}
			if (coinsManager1.isCollision()) {
				coinsManager1.remove();
				mainCharacter.upScore();
				mainCharacter.playcoin();
			
		}
			if (coinsManager2.isCollision()) {
				coinsManager2.remove();
				mainCharacter.upScore();
				mainCharacter.playcoin();
			}
			if (scm.isCollision()) {
				scm.remove();
				if(scm.type==0)
				{
					mainCharacter.upScore1();
					mainCharacter.playcoin();
				}
				if(scm.type==1) {
				mainCharacter.downScore();}
			}
			else if(scm.type==2) {
				mainCharacter.setSpeedX(8);
			}
			else {
				mainCharacter.setSpeedX(5);
			}
		}
		
	}

	public void paint(Graphics g) {
		g.setColor(Color.decode("#ffdb4c"));
		g.fillRect(0, 0, getWidth(), getHeight());
	
		switch (gameState) {
		
		case START_GAME_STATE:
			land.draw(g);
			clouds.draw(g);
			clouds.update();
			g.setFont(new Font("Algerian", Font.PLAIN,80));
			g.setColor(Color.RED);
			g.drawString("....DESERT DASH....",250,250);
			g.drawImage(startingbuttonImage,525,300,null);
			g.setFont(new Font("Courier New", Font.BOLD,30)); 
			g.setColor(Color.WHITE);
			g.drawString("PRESS SPACE KEY TO START !",350,540);
			break;
		case GAME_PLAYING_STATE:
		case GAME_OVER_STATE:
			clouds.draw(g);
			land.draw(g);
			coinsManager.draw(g);
			coinsManager1.draw(g);
			coinsManager2.draw(g);
			enemiesManager.draw(g);
			scm.draw(g);
			mainCharacter.draw(g);
			g.setColor(Color.WHITE);
			Font currentFont = g.getFont();
			Font newFont = currentFont.deriveFont(currentFont.getSize() * 3F);
			g.setFont(newFont);
			g.drawString("SCORE: " + mainCharacter.score, 800, 40);
			g.drawString("Speed:"+mainCharacter.getSpeedX(),800 , 80);
			if (gameState == GAME_OVER_STATE) {
				g.drawImage(gameOverButtonImage, 200, 100, null);
				g.drawImage(replayButtonImage, 470, 260, null);
				g.setFont(new Font("Courier New", Font.BOLD,30)); 
				g.setColor(Color.WHITE);
				g.drawString("PRESS SPACE KEY TO RESTART !",350,540);
			}
			break;
		}
	}

	@Override
	public void run() {

		int fps = 100;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long elapsed;
		
		int msSleep;
		int nanoSleep;

		long endProcessGame;
		long lag = 0;

		while (true) {
			gameUpdate();
			repaint();
			if(mainCharacter.score>600)
			{
				mainCharacter.setSpeedX(10);
			}
			else if(mainCharacter.score>400)
			{
				mainCharacter.setSpeedX(9);
			}
			else if(mainCharacter.score>250)
			{
				mainCharacter.setSpeedX(8);
			}
			else if(mainCharacter.score>100)
			{
				mainCharacter.setSpeedX(7);
			}
			endProcessGame = System.nanoTime();
			elapsed = (lastTime + msPerFrame - System.nanoTime());
			msSleep = (int) (elapsed / 1000000);
			nanoSleep = (int) (elapsed % 1000000);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(msSleep, nanoSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!isKeyPressed) {
			isKeyPressed = true;
			switch (gameState) {
			case START_GAME_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = GAME_PLAYING_STATE;
				}
				break;
			case GAME_PLAYING_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					mainCharacter.jump();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					mainCharacter.down(true);
				}
				break;
			case GAME_OVER_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = GAME_PLAYING_STATE;
				resetGame();
				}
				break;

			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isKeyPressed = false;
		if (gameState == GAME_PLAYING_STATE) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mainCharacter.down(false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void resetGame() {
		enemiesManager.reset();
		mainCharacter.dead(false);
		mainCharacter.reset();
		coinsManager.reset();
		coinsManager1.reset();
		coinsManager2.reset();
		scm.reset(); 
		
	}

}

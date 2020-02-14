package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Resource;

public class SpCoinManager{
	
	private BufferedImage coin;
	
	private Random rand;
	public int type;
	private List<Coin> coins;
	private MainCharacter mainCharacter;
	
	public SpCoinManager(MainCharacter mainCharacter) {
		rand = new Random();
		coin = Resource.getResouceImage("data/spcoin.png");
		
		coins = new ArrayList<Coin>();
		this.mainCharacter = mainCharacter;
		coins.add(createCoin());
	}
	
	public void update() {
		for(Coin e : coins) {
			e.update();
		}
		type=rand.nextInt(3);
		Coin coin = coins.get(0);
		if(coin.isOutOfScreen()) {
			
			coins.clear();
			coins.add(createCoin());
		}
	}
	public void remove() {
		
		Coin coin = coins.get(0);
			coins.clear();
			coins.add(createCoin());
		}
	
	public void draw(Graphics g) {
		for(Coin e : coins) {
			e.draw(g);
		}
	}
	
	private Coin createCoin() {
		// if (enemyType = getRandom)
		
			return new Coinss1(mainCharacter, ((rand.nextInt(2)+1)*1000), coin.getWidth() - 10, coin.getHeight() - 10, coin);
			
		
	}
	
	public boolean isCollision() {
		for(Coin e : coins) {
			if (mainCharacter.getBound().intersects(e.getBound())) {
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		coins.clear();
		coins.add(createCoin());
	}
	
}
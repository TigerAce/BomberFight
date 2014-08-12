package com.game.bomberfight.core;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberfight.interfaces.Picker;
import com.game.bomberfight.model.Controller;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;

public class Bomber extends Player implements Picker{


 	private int bombPlacementCounter;
 	private boolean placeBomb;  //the flag indicate whether the bomb can be placed
 	private float timeCounter;        //time counter;
 	
 	private ArrayList<Item> inventory;  //store items for player
 	private ArrayList<Bomb> activatedBombList;


 	
 	/**
 	 * with default 1 bomb per round and 3 sec wait between rounds
 	 * @param xPos x position of bomber
 	 * @param yPos y position of bomber
 	 * @param speed speed can run
 	 * @param life life of bomber
 	 */
 	
  	public Bomber(float xPos, float yPos, float speed, float life) {
  		this(xPos, yPos, speed, life, 1, 3);
  	
  	}

	public Bomber(float xPos, float yPos, float width, float height, float speed, float life){
		this(xPos, yPos, width, height, speed, life, 1, 3);
	
	}
	
	public Bomber(float xPos, float yPos, float speed, float life, int numBombPerRound, float roundInterval) {
		super(xPos, yPos, speed, life);
	
		inventory = new ArrayList<Item>();
		activatedBombList = new ArrayList<Bomb>();
		
		this.attr.setNumBombPerRound(numBombPerRound);
  		this.attr.setRoundInterval(roundInterval);
  		attr.setPowerX(20);
  		attr.setPowerY(20);
  		attr.setOriginStyle(Explosion.Style.CROSS);
  		attr.setCurrStyle(Explosion.Style.CROSS);
	
  		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
	}
	
	public Bomber(float xPos, float yPos, float width, float height, float speed, float life,  int numBombPerRound, float roundInterval){
		super(xPos, yPos, width, height, speed, life);
		
		inventory = new ArrayList<Item>();
		activatedBombList = new ArrayList<Bomb>();
		
		this.attr.setNumBombPerRound(numBombPerRound);
  		this.attr.setRoundInterval(roundInterval);
  		attr.setPowerX(20);
  		attr.setPowerY(20);
  		attr.setOriginStyle(Explosion.Style.CROSS);
  		attr.setCurrStyle(Explosion.Style.CROSS);

  		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
	}
	

	
	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		super.draw(batch);
	}

    @Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		super.update(delta);
	
		//check item affect time
		for(int i = 0; i < inventory.size(); i++){
			
				inventory.get(i).setAffectTime(inventory.get(i).getAffectTime() - delta);
				//if affect time up
				if(inventory.get(i).getAffectTime() <= 0){
					
					//discard item effect from player
					this.attr.minus(inventory.get(i).getAttr());
					
					//remove from inventory
					Item item = inventory.remove(i);
					//Destroy item
					item.setDiscard(true);
				}
		}
		
		//check if can place bomb
		if(!placeBomb){
			timeCounter -= delta;
			if(timeCounter <= 0){
				placeBomb = true;
				timeCounter = attr.getRoundInterval();
				bombPlacementCounter = attr.getNumBombPerRound();
			}
		}
		
		if (controller != null) {
			controller.update();
		}
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void pickUp(Item item) {
		Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
		
		boolean isExist = false;
		
		int i = 0;
		for (; i < inventory.size(); i++) {
			if (inventory.get(i).getName().equalsIgnoreCase(item.getName())) {
				isExist = true;
				break;
			}
		}
		
		if(item.getAffectTime() >= 0){
			if (isExist) {
				//reset affect time
				inventory.get(i).setAffectTime(item.getAffectTime());
				item.setDiscard(true);
			} else {
				inventory.add(item);
		    	((GamePlay)currentScreen).getGui().pickUpBuff(this, item);
		    	//add the effects to player
				this.attr.add(item.getAttr());
			}
		} else {
			//add the effects to player
			this.attr.add(item.getAttr());
		}
		
		//set pickup to true
		item.setPicked(true);
	}


	/**
	 * @return the bombPlacementCounter
	 */
	public int getBombPlacementCounter() {
		return bombPlacementCounter;
	}

	/**
	 * @return the placeBomb
	 */
	public boolean isPlaceBomb() {
		return placeBomb;
	}

	/**
	 * @param bombPlacementCounter the bombPlacementCounter to set
	 */
	public void setBombPlacementCounter(int bombPlacementCounter) {
		this.bombPlacementCounter = bombPlacementCounter;
	}

	/**
	 * @param placeBomb the placeBomb to set
	 */
	public void setPlaceBomb(boolean placeBomb) {
		this.placeBomb = placeBomb;
	}

	public void placeBomb() {
		if (this.isPlaceBomb()) {
			float bombCoordX = this.getBox2dBody()
					.getPosition().x;
			float bombCoordY = this.getBox2dBody()
					.getPosition().y;
			switch (this.getDirection()) {
			case left:
				bombCoordX -= this.getWidth() / 2;
				break;
			case right:
				bombCoordX += this.getWidth() / 2;
				break;
			case down:
				bombCoordY -= this.getHeight() / 2;
				break;
			case up:
				bombCoordY += this.getHeight() / 2;
				break;
			case left_up:
				bombCoordX -= this.getWidth() / 2;
				bombCoordY += this.getHeight() / 2;
				break;
			case left_down:
				bombCoordX -= this.getWidth() / 2;
				bombCoordY -= this.getHeight() / 2;
				break;
			case right_up:
				bombCoordX += this.getWidth() / 2;
				bombCoordY += this.getHeight() / 2;
				break;
			case right_down:
				bombCoordX += this.getWidth() / 2;
				bombCoordY -= this.getHeight() / 2;
				break;
			}
			Bomb bomb = new Bomb(bombCoordX, bombCoordY, 3, this.getAttr().getPowerX(),
					this.getAttr().getPowerY(), this.getAttr().getCurrStyle());
			
			bomb.setOwner(this);
			this.activatedBombList.add(bomb);
			
			bomb.create();
			
			this.setBombPlacementCounter(this
					.getBombPlacementCounter() - 1);
			if (this.getBombPlacementCounter() <= 0) {
				this.setPlaceBomb(false);
			}
		}
	}

	/**
	 * @return the inventory
	 */
	public ArrayList<Item> getInventory() {
		return inventory;
	}
	
	public boolean hasItem(String name) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Bomb> getActivatedBombList() {
		return activatedBombList;
	}

	public void setActivatedBombList(ArrayList<Bomb> activatedBombList) {
		this.activatedBombList = activatedBombList;
	}

}

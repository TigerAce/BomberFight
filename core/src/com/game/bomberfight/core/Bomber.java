package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberfight.InputSource.BomberController;
import com.game.bomberfight.interfaces.Picker;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;

public class Bomber extends Player implements Picker{


 	private int bombPlacementCounter;
 	private boolean placeBomb;  //the flag indicate whether the bomb can be placed
 	private float timeCounter;        //time counter;
 	private BomberController bomberController = null;
 	

 	
 	/**
 	 * with default 1 bomb per round and 3 sec wait between rounds
 	 * @param xPos x position of bomber
 	 * @param yPos y position of bomber
 	 * @param speed speed can run
 	 * @param life life of bomber
 	 */
 	
  	public Bomber(float xPos, float yPos, float speed, float life) {
  		super(xPos, yPos, speed, life);
  		
  		this.attr.setNumBombPerRound(1);
  		this.attr.setRoundInterval(3);
  		attr.setPowerX(50);
  		attr.setPowerY(50);
  		attr.setExplosionStyle(Explosion.Style.CROSS);
 		
 		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
 		
 		
 	
  	}

	public Bomber(float xPos, float yPos, float width, float height, float speed, float life){
		super(xPos, yPos, width, height, speed, life);
		this.attr.setNumBombPerRound(1);
  		this.attr.setRoundInterval(3);
  		attr.setPowerX(50);
  		attr.setPowerY(50);
  		attr.setExplosionStyle(Explosion.Style.CROSS);
 		
  		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
	}
	
	public Bomber(float xPos, float yPos, float speed, float life, int numBombPerRound, float roundInterval) {
		super(xPos, yPos, speed, life);
		this.attr.setNumBombPerRound(numBombPerRound);
  		this.attr.setRoundInterval(roundInterval);
  		attr.setPowerX(50);
  		attr.setPowerY(50);
  		attr.setExplosionStyle(Explosion.Style.CROSS);
	
  		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
	}
	
	public Bomber(float xPos, float yPos, float width, float height, float speed, float life,  int numBombPerRound, float roundInterval){
		super(xPos, yPos, width, height, speed, life);
		this.attr.setNumBombPerRound(numBombPerRound);
  		this.attr.setRoundInterval(roundInterval);
  		attr.setPowerX(50);
  		attr.setPowerY(50);
  		attr.setExplosionStyle(Explosion.Style.CROSS);

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
    public void create() {
    	super.create();
    	Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
    	((GamePlay)currentScreen).getControllableObjects().add(this);
    }

    @Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		super.update(delta);
		if(!placeBomb){
			timeCounter -= delta;
			if(timeCounter <= 0){
				placeBomb = true;
				timeCounter = attr.getRoundInterval();
				bombPlacementCounter = attr.getNumBombPerRound();
			}
		}
		if (bomberController != null) {
			bomberController.processInput();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void pickUp(Item item) {
		this.attr.merge(item.getAttr());
		item.setPicked(true);
	}

	/**
	 * @param bomberController the bomberController to set
	 */
	public void setBomberController(BomberController bomberController) {
		this.bomberController = bomberController;
		this.bomberController.owner = this;
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


}

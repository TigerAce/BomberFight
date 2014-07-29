package com.game.bomberfight.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Player;

public class Bomber extends Player{

	private int numBombPerRound; //the number of bomb can be placed in one round
 	private int bombPlacementCounter;
 	private boolean placeBomb;  //the flag indicate whether the bomb can be placed
 	private float roundInterval; //time will be delay between two rounds
 	private float timeCounter;        //time counter;
 	
 	/**
 	 * with default 1 bomb per round and 3 sec wait between rounds
 	 * @param xPos x position of bomber
 	 * @param yPos y position of bomber
 	 * @param speed speed can run
 	 * @param life life of bomber
 	 */
 	
  	public Bomber(float xPos, float yPos, float speed, float life) {
  		super(xPos, yPos, speed, life);
  		
 		roundInterval = 3;
 		numBombPerRound = 1;
 		
 		timeCounter = roundInterval;
 		bombPlacementCounter = numBombPerRound;
 		placeBomb = true;
 	
  	}

	public Bomber(float xPos, float yPos, float width, float height, float speed, float life){
		super(xPos, yPos, width, height, speed, life);
 		roundInterval = 3;
 		numBombPerRound = 1;
 		
 		timeCounter = roundInterval;
 		bombPlacementCounter = numBombPerRound;
 		placeBomb = true;
	}
	
	public Bomber(float xPos, float yPos, float speed, float life, int numBombPerRound, float roundInterval) {
		super(xPos, yPos, speed, life);
		this.numBombPerRound = numBombPerRound;
		this.roundInterval = roundInterval;

		timeCounter = roundInterval;
		bombPlacementCounter = numBombPerRound;
		placeBomb = true;
	}
	
	public Bomber(float xPos, float yPos, float width, float height, float speed, float life,  int numBombPerRound, float roundInterval){
		super(xPos, yPos, width, height, speed, life);
		this.numBombPerRound = numBombPerRound;
		this.roundInterval = roundInterval;

		timeCounter = roundInterval;
		bombPlacementCounter = numBombPerRound;
		placeBomb = true;
	}
	


    public int getNumBombPerRound() {
		return numBombPerRound;
	}

	public void setNumBombPerRound(int numBombPerRound) {
		this.numBombPerRound = numBombPerRound;
	}

	public float getRoundInterval() {
		return roundInterval;
	}

	public void setRoundInterval(float roundInterval) {
		this.roundInterval = roundInterval;
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		super.draw(batch);
	}

    @Override
    public void create() {
    	super.create();
    }

    @Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		super.update(delta);
		if(!placeBomb){
			timeCounter -= delta;
			if(timeCounter <= 0){
				placeBomb = true;
				timeCounter = roundInterval;
				bombPlacementCounter = numBombPerRound;
			}
		}

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.game.bomberfight.model.Player#doKeyDown(int)
	 */
	@Override
	public boolean doKeyDown(int keycode) {
		// TODO Auto-generated method stub
		super.doKeyDown(keycode);
		switch (keycode) {
		case Input.Keys.SPACE:
			if(placeBomb){
				Bomb bomb = new Bomb(box2dBody.getPosition().x, box2dBody.getPosition().y,3, 50, 50, Explosion.Style.ANNULAR);
				bomb.create();
				bombPlacementCounter--;
				if(bombPlacementCounter <= 0){
					placeBomb = false;
				}
			}
			return true;
		}
		return false;
	}


}

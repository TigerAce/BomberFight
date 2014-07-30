package com.game.bomberfight.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Player;

public class Bomber extends Player{


 	private int bombPlacementCounter;
 	private boolean placeBomb;  //the flag indicate whether the bomb can be placed
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
  		
  		this.attr.setNumBombPerRound(1);
  		this.attr.setRoundInterval(3);
  		attr.setExplosionStyle(Explosion.Style.CROSS);
 		
 		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
 		
 		
 	
  	}

	public Bomber(float xPos, float yPos, float width, float height, float speed, float life){
		super(xPos, yPos, width, height, speed, life);
		this.attr.setNumBombPerRound(1);
  		this.attr.setRoundInterval(3);
  		attr.setExplosionStyle(Explosion.Style.CROSS);
 		
  		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
	}
	
	public Bomber(float xPos, float yPos, float speed, float life, int numBombPerRound, float roundInterval) {
		super(xPos, yPos, speed, life);
		this.attr.setNumBombPerRound(numBombPerRound);
  		this.attr.setRoundInterval(roundInterval);
  		attr.setExplosionStyle(Explosion.Style.CROSS);
	
  		timeCounter = attr.getRoundInterval();
 		bombPlacementCounter = attr.getNumBombPerRound();
 		placeBomb = true;
	}
	
	public Bomber(float xPos, float yPos, float width, float height, float speed, float life,  int numBombPerRound, float roundInterval){
		super(xPos, yPos, width, height, speed, life);
		this.attr.setNumBombPerRound(numBombPerRound);
  		this.attr.setRoundInterval(roundInterval);
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
				float bombCoordX = box2dBody.getPosition().x;
				float bombCoordY = box2dBody.getPosition().y;
				switch(direction){
				case left:
					bombCoordX -= width / 2;
					break;
				case right:
					bombCoordX += width / 2;
					break;
				case down:
					bombCoordY -= height / 2;
					break;
				case up:
					bombCoordY += height / 2;
					break;
				case left_up:
					bombCoordX -= width / 2;
					bombCoordY += height / 2;
					break;
				case left_down:
					bombCoordX -= width / 2;
					bombCoordY -= height / 2;
					break;
				case right_up:
					bombCoordX += width / 2;
					bombCoordY += height / 2;
					break;
				case right_down:
					bombCoordX += width / 2;
					bombCoordY -= height / 2;
					break;
				}
				Bomb bomb = new Bomb(bombCoordX, bombCoordY,3, 50, 50, attr.getExplosionStyle());
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

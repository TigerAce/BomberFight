package com.game.bomberfight.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Player;

public class Bomber extends Player{

	public Bomber(float xPos, float yPos, float speed) {
		super(xPos, yPos, speed);
		// TODO Auto-generated constructor stub
	}
	
	 @Override
     public boolean doKeyDown(int keycode) {

	        switch (keycode) {
	            case Input.Keys.W:
	            	keyMap.put(Input.Keys.W, true);
	                return true;
	            case Input.Keys.S:
	            	keyMap.put(Input.Keys.S, true);
	                return true;
	            case Input.Keys.A:
	            	keyMap.put(Input.Keys.A, true);
	                return true;
	            case Input.Keys.D:
	            	keyMap.put(Input.Keys.D, true);
	                return true;
	            case Input.Keys.SPACE:
	            	Bomb bomb = new Bomb(box2dBody.getPosition().x, box2dBody.getPosition().y,3, 50, 50, Explosion.Style.ANNULAR);
	            	bomb.create();
	            	return true;
	        }

	        return false;
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
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

}

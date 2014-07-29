package com.game.bomberfight.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Player;

public class Bomber extends Player{

	public Bomber(float xPos, float yPos, float speed, float life) {
		super(xPos, yPos, speed, life);
		// TODO Auto-generated constructor stub
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

	/* (non-Javadoc)
	 * @see com.game.bomberfight.model.Player#doKeyDown(int)
	 */
	@Override
	public boolean doKeyDown(int keycode) {
		// TODO Auto-generated method stub
		super.doKeyDown(keycode);
		switch (keycode) {
		case Input.Keys.SPACE:
			Bomb bomb = new Bomb(box2dBody.getPosition().x, box2dBody.getPosition().y,3, 50, 50, Explosion.Style.ANNULAR);
        	bomb.create();
			return true;
		}
		return false;
	}

}

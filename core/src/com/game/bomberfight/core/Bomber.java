package com.game.bomberfight.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	 * @see com.game.bomberfight.model.Player#processInput()
	 */
	@Override
	public void processInput() {
		// TODO Auto-generated method stub
		super.processInput();
		Iterator<Entry<Integer, Boolean>> iter = keyMap.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
		    int key = (Integer) entry.getKey();
		    boolean value = (Boolean) entry.getValue();
		    switch (key) {
			case Input.Keys.SPACE:
				if (value) {
					Bomb bomb = new Bomb(box2dBody.getPosition().x, box2dBody.getPosition().y,3, 50, 50, Explosion.Style.ANNULAR);
	            	bomb.create();
				}
				break;

			default:
				break;
			}
		} 
	}

}

package com.game.bomberfight.InputSource;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.InputProcessor;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.model.Controller;

public class BomberController extends Controller implements InputProcessor {
	
	private Map<Integer, Boolean> keyMap = new LinkedHashMap<Integer, Boolean>();
	private int[] keys;

	public BomberController(int[] keys) {
		// TODO Auto-generated constructor stub
		owner = null;
		this.keys = keys;
	}
	
	/**
	 * This function process input continuously based on status, different
	 * from doKeyDown and doKeyUp which only get executed once
	 */
	public void update() {
		if (owner == null) {
			return;
		}

		Bomber bomber = (Bomber) owner;
		Iterator<Entry<Integer, Boolean>> iter = keyMap.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			int key = (Integer) entry.getKey();
			boolean value = (Boolean) entry.getValue();
			
			if (key == keys[0]) {
				if (value) {
					bomber.moveUp();
				} else {
					bomber.stopVerticalMove();
					iter.remove();
				}
			}
			
			if (key == keys[1]) {
				if (value) {
					bomber.moveLeft();
				} else {
					bomber.stopHorizontalMove();
					iter.remove();
				}
			}
			
			if (key == keys[2]) {
				if (value) {
					bomber.moveDown();
				} else {
					bomber.stopVerticalMove();
					iter.remove();
				}
			}
			
			if (key == keys[3]) {
				if (value) {
					bomber.moveRight();
				} else {
					bomber.stopHorizontalMove();
					iter.remove();
				}
			}
			
			if (key == keys[4]) {
				if (value) {
					bomber.placeBomb();
					iter.remove();
				}
			}

		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		keyMap.put(keycode, true);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		keyMap.put(keycode, false);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}

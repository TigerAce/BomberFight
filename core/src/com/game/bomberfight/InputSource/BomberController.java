package com.game.bomberfight.InputSource;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Input;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.model.GameObject;

public class BomberController {
	
	public GameObject owner;
	public boolean isPrimary = true;

	public BomberController(boolean isPrimary) {
		// TODO Auto-generated constructor stub
		owner = null;
		this.isPrimary = isPrimary;
	}
	
	/**
	 * This function process input continuously based on status, different
	 * from doKeyDown and doKeyUp which only get executed once
	 */
	public void processInput() {
		if (owner == null) {
			return;
		}

		Bomber bomber = (Bomber) owner;
		Iterator<Entry<Integer, Boolean>> iter = bomber.getKeyMap().entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			int key = (Integer) entry.getKey();
			boolean value = (Boolean) entry.getValue();

			if (isPrimary) {
				switch (key) {
				case Input.Keys.W:
					if (value) {
						bomber.moveUp();
					} else {
						bomber.stopVerticalMove();
						iter.remove();
					}
					break;
				case Input.Keys.A:
					if (value) {
						bomber.moveLeft();
					} else {
						bomber.stopHorizontalMove();
						iter.remove();
					}
					break;
				case Input.Keys.S:
					if (value) {
						bomber.moveDown();
					} else {
						bomber.stopVerticalMove();
						iter.remove();
					}
					break;
				case Input.Keys.D:
					if (value) {
						bomber.moveRight();
					} else {
						bomber.stopHorizontalMove();
						iter.remove();
					}
					break;
				case Input.Keys.SPACE:
					if (value) {
						bomber.placeBomb();
						bomber.getKeyMap().put(key, false);
					} else {
						iter.remove();
					}
					break;

				default:
					break;
				}
			} else {
				switch (key) {
				case Input.Keys.UP:
					if (value) {
						bomber.moveUp();
					} else {
						bomber.stopVerticalMove();
						iter.remove();
					}
					break;
				case Input.Keys.LEFT:
					if (value) {
						bomber.moveLeft();
					} else {
						bomber.stopHorizontalMove();
						iter.remove();
					}
					break;
				case Input.Keys.DOWN:
					if (value) {
						bomber.moveDown();
					} else {
						bomber.stopVerticalMove();
						iter.remove();
					}
					break;
				case Input.Keys.RIGHT:
					if (value) {
						bomber.moveRight();
					} else {
						bomber.stopHorizontalMove();
						iter.remove();
					}
					break;
				case Input.Keys.CONTROL_RIGHT:
					if (value) {
						bomber.placeBomb();
						bomber.getKeyMap().put(key, false);
					} else {
						iter.remove();
					}
					break;

				default:
					break;
				}
			}

		}
	}

}

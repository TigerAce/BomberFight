package com.game.bomberfight.InputSource;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.game.bomberfight.core.Bomb;
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
		Iterator<Entry<Integer, Boolean>> iter = bomber.getKeyMap().entrySet()
				.iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			int key = (Integer) entry.getKey();
			boolean value = (Boolean) entry.getValue();

			if (isPrimary) {
				switch (key) {
				case Input.Keys.W:
					if (value) {
						bomber.getMovement().y = bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().y = 0;
						iter.remove();
					}
					break;
				case Input.Keys.A:
					if (value) {
						bomber.getMovement().x = -bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().x = 0;
						iter.remove();
					}
					break;
				case Input.Keys.S:
					if (value) {
						bomber.getMovement().y = -bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().y = 0;
						iter.remove();
					}
					break;
				case Input.Keys.D:
					if (value) {
						bomber.getMovement().x = bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().x = 0;
						iter.remove();
					}
					break;
				case Input.Keys.SPACE:
					if (value) {
						if (bomber.isPlaceBomb()) {
							float bombCoordX = bomber.getBox2dBody()
									.getPosition().x;
							float bombCoordY = bomber.getBox2dBody()
									.getPosition().y;
							switch (bomber.getDirection()) {
							case left:
								bombCoordX -= bomber.getWidth() / 2;
								break;
							case right:
								bombCoordX += bomber.getWidth() / 2;
								break;
							case down:
								bombCoordY -= bomber.getHeight() / 2;
								break;
							case up:
								bombCoordY += bomber.getHeight() / 2;
								break;
							case left_up:
								bombCoordX -= bomber.getWidth() / 2;
								bombCoordY += bomber.getHeight() / 2;
								break;
							case left_down:
								bombCoordX -= bomber.getWidth() / 2;
								bombCoordY -= bomber.getHeight() / 2;
								break;
							case right_up:
								bombCoordX += bomber.getWidth() / 2;
								bombCoordY += bomber.getHeight() / 2;
								break;
							case right_down:
								bombCoordX += bomber.getWidth() / 2;
								bombCoordY -= bomber.getHeight() / 2;
								break;
							}
							Bomb bomb = new Bomb(bombCoordX, bombCoordY, 3, bomber.getAttr().getPowerX(),
									bomber.getAttr().getPowerY(), bomber.getAttr().getCurrStyle());
							bomb.create();
							bomber.setBombPlacementCounter(bomber
									.getBombPlacementCounter() - 1);
							if (bomber.getBombPlacementCounter() <= 0) {
								bomber.setPlaceBomb(false);
							}
						}
						bomber.getKeyMap().put(key, false);
					}
					break;

				default:
					break;
				}
			} else {
				switch (key) {
				case Input.Keys.UP:
					if (value) {
						bomber.getMovement().y = bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().y = 0;
						iter.remove();
					}
					break;
				case Input.Keys.LEFT:
					if (value) {
						bomber.getMovement().x = -bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().x = 0;
						iter.remove();
					}
					break;
				case Input.Keys.DOWN:
					if (value) {
						bomber.getMovement().y = -bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().y = 0;
						iter.remove();
					}
					break;
				case Input.Keys.RIGHT:
					if (value) {
						bomber.getMovement().x = bomber.getAttr().getSpeed();
					} else {
						bomber.getMovement().x = 0;
						iter.remove();
					}
					break;
				case Input.Keys.CONTROL_RIGHT:
					if (value) {
						if (bomber.isPlaceBomb()) {
							float bombCoordX = bomber.getBox2dBody()
									.getPosition().x;
							float bombCoordY = bomber.getBox2dBody()
									.getPosition().y;
							switch (bomber.getDirection()) {
							case left:
								bombCoordX -= bomber.getWidth() / 2;
								break;
							case right:
								bombCoordX += bomber.getWidth() / 2;
								break;
							case down:
								bombCoordY -= bomber.getHeight() / 2;
								break;
							case up:
								bombCoordY += bomber.getHeight() / 2;
								break;
							case left_up:
								bombCoordX -= bomber.getWidth() / 2;
								bombCoordY += bomber.getHeight() / 2;
								break;
							case left_down:
								bombCoordX -= bomber.getWidth() / 2;
								bombCoordY -= bomber.getHeight() / 2;
								break;
							case right_up:
								bombCoordX += bomber.getWidth() / 2;
								bombCoordY += bomber.getHeight() / 2;
								break;
							case right_down:
								bombCoordX += bomber.getWidth() / 2;
								bombCoordY -= bomber.getHeight() / 2;
								break;
							}
							Bomb bomb = new Bomb(bombCoordX, bombCoordY, 3, bomber.getAttr().getPowerX(),
									bomber.getAttr().getPowerY(), bomber.getAttr().getCurrStyle());
							bomb.create();
							bomber.setBombPlacementCounter(bomber
									.getBombPlacementCounter() - 1);
							if (bomber.getBombPlacementCounter() <= 0) {
								bomber.setPlaceBomb(false);
							}
						}
						bomber.getKeyMap().put(key, false);
					}
					break;

				default:
					break;
				}
			}

		}
	}

}

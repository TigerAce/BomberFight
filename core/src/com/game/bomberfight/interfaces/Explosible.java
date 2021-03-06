package com.game.bomberfight.interfaces;

import com.game.bomberfight.model.Explosion;

public interface Explosible {

	/**
	 * explode on (x,y) with force powerX and Y
	 * @param x     explosion center x
	 * @param y     explosion center y
	 * @param powerX  explosion power x
	 * @param powerY  explosion power y
	 * @param Explosion.style style of explosion
	 */
	public abstract void explode(float x, float y, float powerX, float powerY, Explosion.Style style);
}

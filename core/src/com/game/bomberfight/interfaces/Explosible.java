package com.game.bomberfight.interfaces;

public interface Explosible {

	/**
	 * explode on (x,y) with force powerX and Y
	 * @param x     explosion center x
	 * @param y     explosion center y
	 * @param powerX  explosion power x
	 * @param powerY  explosion power y
	 */
	public abstract void explode(float x, float y, float powerX, float powerY, String style);
}

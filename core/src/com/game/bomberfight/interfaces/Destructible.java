package com.game.bomberfight.interfaces;

public interface Destructible {

/**
 * break object into fragments
 * @param fragCenterX       break center x
 * @param fragCenterY       break center y
 * @param fragPieces        break into pieces
 */
	abstract void fragment(float fragCenterX, float fragCenterY, int fragPieces);
}

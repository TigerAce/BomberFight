package com.game.bomberfight.interfaces;

/**
 * object implements breakable can be break into fragments
 * @author Yunhao Chen
 *
 */
public interface Breakable {

/**
 * break object into fragments
 * @param fragCenterX       break center x
 * @param fragCenterY       break center y
 * @param fragPieces        break into # pieces
 */
	abstract void toFragments(float fragCenterX, float fragCenterY, int fragPieces);
}

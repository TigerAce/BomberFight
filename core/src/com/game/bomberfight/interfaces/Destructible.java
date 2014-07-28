package com.game.bomberfight.interfaces;

import com.badlogic.gdx.physics.box2d.ContactImpulse;

/**
 * Destructible interface is for the game object witch have life attribute and can be destroyed by explosion.
 * @author Yunhao Chen
 *
 */
public interface Destructible {
	public abstract void damage(ContactImpulse impulse);
}

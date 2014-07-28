package com.game.bomberfight.core;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.bomberfight.interfaces.Destructible;



public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
	
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	
		/**
		 * particle will damage any object that is destructible
		 */
		Object userDataA = contact.getFixtureA().getBody().getUserData();
		Object userDataB = contact.getFixtureB().getBody().getUserData();
		
		if(userDataA != null && userDataB != null){
		
			if(userDataA instanceof Particle && userDataB instanceof Destructible){
				((Destructible)userDataB).damage(impulse);;
			}
			
			if(userDataB instanceof Particle && userDataA instanceof Destructible){
				((Destructible)userDataA).damage(impulse);
			}
			
			
		}
	}

}

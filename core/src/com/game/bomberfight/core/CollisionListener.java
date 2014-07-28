package com.game.bomberfight.core;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;



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
		 * particle will decrease crate and brick's life with the amount of impulse
		 */
		Object userDataA = contact.getFixtureA().getBody().getUserData();
		Object userDataB = contact.getFixtureB().getBody().getUserData();
		
		if(userDataA != null && userDataB != null){
			
			if(userDataA instanceof Particle && userDataB instanceof Crate){
				((Crate)userDataB).setLife(((Crate)userDataB).getLife() - impulse.getNormalImpulses()[0]);
			}
			if(userDataB instanceof Particle && userDataA instanceof Crate){
				((Crate)userDataA).setLife(((Crate)userDataA).getLife() - impulse.getNormalImpulses()[0]);
			}
			
		}
	}

}

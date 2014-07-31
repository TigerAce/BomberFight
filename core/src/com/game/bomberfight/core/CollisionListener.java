package com.game.bomberfight.core;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.interfaces.Picker;
import com.game.bomberfight.model.Player;



public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Object userDataA = contact.getFixtureA().getBody().getUserData();
		Object userDataB = contact.getFixtureB().getBody().getUserData();
		
		if(userDataA != null && userDataB != null){
		
			if(userDataA instanceof Picker && userDataB instanceof Item){
				((Picker)userDataA).pickUp((Item)userDataB);
			}else if(userDataB instanceof Picker && userDataA instanceof Item){
				((Picker)userDataB).pickUp((Item)userDataA);
			}
		
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object userDataA = contact.getFixtureA().getBody().getUserData();
		Object userDataB = contact.getFixtureB().getBody().getUserData();
		
		if(userDataA != null && userDataB != null){
		
			if(userDataA instanceof Bomb && userDataB instanceof Item){
				contact.setEnabled(false);
			}else if(userDataB instanceof Bomb && userDataA instanceof Item){
				contact.setEnabled(false);
			}
		
		}
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
			}else if(userDataB instanceof Particle && userDataA instanceof Destructible){
				((Destructible)userDataA).damage(impulse);
			}
			
			
		}
	}

}

package com.game.bomberfight.core;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.interfaces.Picker;
import com.game.bomberfight.utility.UserData;



public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		UserData userDataA = (UserData) contact.getFixtureA().getBody().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getBody().getUserData();
		
		if(userDataA != null && userDataB != null){
		
			if(userDataA.object instanceof Picker && userDataB.object instanceof Item){
				((Picker)userDataA.object).pickUp((Item)userDataB.object);
			}else if(userDataB.object instanceof Picker && userDataA.object instanceof Item){
				((Picker)userDataB.object).pickUp((Item)userDataA.object);
			}
		
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		UserData userDataA = (UserData) contact.getFixtureA().getBody().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getBody().getUserData();
		
		if(userDataA != null && userDataB != null){
		
			if(userDataA.object instanceof Bomb && userDataB.object instanceof Item){
				contact.setEnabled(false);
			}
			else if(userDataB.object instanceof Bomb && userDataA.object instanceof Item){
				contact.setEnabled(false);
			}
			
			
			else if(userDataA.object instanceof Bomb && userDataB.object instanceof Particle){
				((Bomb)userDataA.object).setTime(0);
			}else if(userDataB.object instanceof Bomb && userDataA.object instanceof Particle){
				((Bomb)userDataB.object).setTime(0);
			}
			

			else if(userDataA.object instanceof Item && userDataB.object instanceof Particle){
				contact.setEnabled(false);
			}else if(userDataB.object instanceof Item && userDataA.object instanceof Particle){
				contact.setEnabled(false);
			}
			
		
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	
		/**
		 * particle will damage any object that is destructible
		 */
		UserData userDataA = (UserData) contact.getFixtureA().getBody().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getBody().getUserData();
		
		if(userDataA != null && userDataB != null){
		
			if(userDataA.object instanceof Particle && userDataB.object instanceof Destructible){
				((Destructible)userDataB.object).damage(impulse);
			}else if(userDataB.object instanceof Particle && userDataA.object instanceof Destructible){
				((Destructible)userDataA.object).damage(impulse);
			}
			
			
		}
	}

}

package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.screen.GamePlay;

public class CrossExplosion extends Explosion{

	protected CrossExplosion(float x, float y, float lifespan,
			float blastPowerX, float blastPowerY, float totalDensity,
			int numParticles) {
		super(x, y, totalDensity, lifespan, blastPowerX, blastPowerY, numParticles);
		
		float density = (float) (totalDensity / numParticles);
		float angle;
		
		float explosionSize = 1f; // the width of particle set
		
		float particleX = x;
		float particleY = y;
		
		int particlesSet = numParticles / 4;
		float DistBetweenParticles = explosionSize/ particlesSet;
		
		
		for(int i = 0; i < numParticles; i++){
		
			
			if(i < particlesSet){
				angle = (0) * MathUtils.degreesToRadians;
			
				if(i == 0)
					 particleX = x - explosionSize / 2;
				else particleX += DistBetweenParticles;
				
			}else if(i >= particlesSet && i < particlesSet * 2){
			    angle = (90) * MathUtils.degreesToRadians;
			    
			    if(i == particlesSet)
			    	 particleY = y + explosionSize / 2;
			    else particleY += DistBetweenParticles;
			    
			}else if(i >= particlesSet * 2 && i < particlesSet * 3){
			    angle = (180) * MathUtils.degreesToRadians;
			    
			    if(i == particlesSet * 2)
			    	 particleX = x - explosionSize / 2;
			    else particleX += DistBetweenParticles;
			    
			}else{
				angle = (270) * MathUtils.degreesToRadians;
				
				if(i == particlesSet * 3)
					 particleY = y + explosionSize / 2;
				else particleY += DistBetweenParticles;
		    }
			
			Particle p = new Particle(particleX, particleY, angle, density, lifespan, blastPowerX, blastPowerY, this);
			p.create();
			Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
		    ((GamePlay)currentScreen).getGameObjectManager().addGameObject(p);
		
		
		}
		
	}
}
		
	



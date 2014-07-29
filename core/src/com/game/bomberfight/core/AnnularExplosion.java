package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.screen.GamePlay;

public class AnnularExplosion extends Explosion{

/**
 * create an annular explosion
 * @param x  explosion center x
 * @param y  explosion center y
 * @param lifespan  lifespan of particles
 * @param blastPowerX blast power on x axis
 * @param blastPowerY blast power on y axis
 * @param totalDensity total density for particles (each particles density will be total/numparticles)
 * @param numParticles number of particles
 */
protected AnnularExplosion(float x, float y, float lifespan, float blastPowerX,
			float blastPowerY, float totalDensity, int numParticles) {
		super(x, y, totalDensity, lifespan, blastPowerX, blastPowerY, numParticles);
		
		float density = (float) (totalDensity / numParticles);
		float randomStartAngle =  ((float)Math.random() * 90);
		
		for(int i = 0; i < numParticles; i++){
			float angle = ((((float)i / numParticles) * 360) + randomStartAngle) * MathUtils.degreesToRadians;
			Particle p = new Particle(x, y, angle, density, lifespan, blastPowerX, blastPowerY, this);
			p.create();
			
			Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
		    ((GamePlay)currentScreen).getGameObjectManager().addGameObject(p);
		
		
		}
		
	}

	
	
}

package com.game.bomberfight.core;

import com.badlogic.gdx.physics.box2d.World;
import com.game.bomberfight.model.Explosion;

public class CrossExplosion extends Explosion{

	protected CrossExplosion(float x, float y, float lifespan,
			float blastPowerX, float blastPowerY, float totalDensity,
			int numParticles) {
		super(x, y, lifespan, blastPowerX, blastPowerY, totalDensity, numParticles);
		// TODO Auto-generated constructor stub
	}


}

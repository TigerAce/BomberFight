package com.game.bomberfight.model;

public class Explosion {

	public enum Style{ANNULAR, CROSS};
	
	protected boolean completed;
	protected float x;
	protected float y;
	protected float lifespan;
	protected float blastPowerX;
	protected float blastPowerY;
	protected float totalDensity;
	protected int numParticles;
	protected int particleCounter;
	
	protected Explosion(float x, float y, float lifespan, float blastPowerX,
			float blastPowerY, float totalDensity, int numParticles){
	
		this.x = x;
		this.y = y;
		this.blastPowerX = blastPowerX;
		this.blastPowerY = blastPowerY;
		this.totalDensity = totalDensity;
		this.numParticles = numParticles;
		this.particleCounter = numParticles;
	
	
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	
	public void update(float delta){
		if(particleCounter <= 0){
			complete();
			return;
		}
	}
	
	public void complete(){
		completed = true;
	}

	public int getParticleCounter() {
		return particleCounter;
	}

	public void setParticleCounter(int particleCounter) {
		this.particleCounter = particleCounter;
	}
}

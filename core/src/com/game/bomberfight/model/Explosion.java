package com.game.bomberfight.model;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.game.bomberfight.core.Particle;
import com.game.bomberfight.screen.GamePlay;


public class Explosion {

	public enum Style{ANNULAR, CROSS};
	
	protected ArrayList<Particle> particles;
	protected boolean completed;
	protected float x;
	protected float y;
	protected float lifespan;
	protected float blastPowerX;
	protected float blastPowerY;
	protected float totalDensity;
	protected int numParticles;
	
	protected Explosion(float x, float y, float lifespan, float blastPowerX,
			float blastPowerY, float totalDensity, int numParticles){
		particles = new ArrayList<Particle>();
		
		this.x = x;
		this.y = y;
		this.blastPowerX = blastPowerX;
		this.blastPowerY = blastPowerY;
		this.totalDensity = totalDensity;
		this.numParticles = numParticles;
	
	
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void removeParticles(Particle p){
	    Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        ((GamePlay)currentScreen).getWorld().destroyBody(p.getParticleBody());
		particles.remove(p);
	}
	
	public void update(float delta){
		if(particles.isEmpty()){
			complete();
			return;
		}
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).update(delta);
		}
	}
	
	public void complete(){
		completed = true;
	}
}

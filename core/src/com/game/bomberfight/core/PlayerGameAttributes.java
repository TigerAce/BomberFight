package com.game.bomberfight.core;

import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Explosion.Style;

public class PlayerGameAttributes {
	/**
	 * basic attributes
	 */
	private float maxLife; //life of player
	private float currLife;
	private float speed; //speed of player
	private float lifeRegenPerSec;

	
	/**
	 * bomb  and explosion attributes
	 */
	private int numBombPerRound; //number of bomb player can put in one round
	private float roundInterval; //time delay between two rounds
	private float powerX; //power x of explosion
	private float powerY; //power y of explosion
	private Explosion.Style originStyle; //original style of explosion
	private Explosion.Style currStyle; //current style of explosion

	
	
	
	public PlayerGameAttributes(){
		this.maxLife = 0;
		this.currLife = 0;
		this.lifeRegenPerSec = 0;
		this.speed = 0;
		this.numBombPerRound = 0;
		this.roundInterval = 0;
		this.powerX = 0;
		this.powerY = 0;
		this.originStyle = null;
		this.currStyle = null;
	}
	
	
	public PlayerGameAttributes(float lifeRegen, float maxLife, float currLife, float speed, int numBombPerRound,
			float roundInterval, float powerX, float powerY,
			Style explosionStyle) {
		
		this.setLifeRegenPerSec(lifeRegen);
		this.setMaxLife(maxLife);
		this.setCurrLife(currLife);
		this.setSpeed(speed);
		this.setNumBombPerRound(numBombPerRound);
		this.setRoundInterval(roundInterval);
		this.setPowerX(powerX);
		this.setPowerY(powerY);
		this.setCurrStyle(explosionStyle);
	}
	
	public PlayerGameAttributes(PlayerGameAttributes attr){
		this(attr.getLifeRegenPerSec(), attr.getMaxLife(),attr.getCurrLife(), attr.getSpeed(), attr.getNumBombPerRound(), attr.getRoundInterval(), attr.getPowerX(), attr.getPowerY(), attr.getCurrStyle());
	}
	
	
	/**
	 * add given attributes
	 * @param attr
	 */
	
	public void add(PlayerGameAttributes attr){
		
			this.setLifeRegenPerSec(this.getLifeRegenPerSec() + attr.getLifeRegenPerSec());
			
			this.setMaxLife(this.getMaxLife() + attr.getMaxLife());
		
			this.setCurrLife(this.getCurrLife() + attr.getCurrLife());
			
			if(getCurrLife() > getMaxLife()){
				setCurrLife(getMaxLife());
			}
		
			this.setSpeed(this.getSpeed() + attr.getSpeed());
		
	
			this.setNumBombPerRound(this.getNumBombPerRound() + attr.getNumBombPerRound());
		
		
			this.setRoundInterval(this.getRoundInterval() + attr.getRoundInterval());
		
	
			this.setPowerX(this.getPowerX() + attr.getPowerX());
		
	
			this.setPowerY(this.getPowerY() + attr.getPowerY());
		

			if(attr.getCurrStyle() != null){
				this.setCurrStyle(attr.getCurrStyle());
			}
		/*	if (this.originStyle == null) {
				this.originStyle = this.currStyle;
			}
			if(attr.getCurrStyle() != null)
				this.setCurrStyle(attr.getCurrStyle());*/
		
	}
	
	public void minus(PlayerGameAttributes attr){
	
			this.setLifeRegenPerSec(this.getLifeRegenPerSec() - attr.getLifeRegenPerSec());
		
			this.setMaxLife(this.getMaxLife() - attr.getMaxLife());
			
			this.setCurrLife(this.getCurrLife() - attr.getCurrLife());
		
	
			this.setSpeed(this.getSpeed() - attr.getSpeed());
		
	
			this.setNumBombPerRound(this.getNumBombPerRound() - attr.getNumBombPerRound());
		
	
			this.setRoundInterval(this.getRoundInterval() - attr.getRoundInterval());
		
		
			this.setPowerX(this.getPowerX() - attr.getPowerX());
		
	
			this.setPowerY(this.getPowerY() - attr.getPowerY());
		
			if(attr.getCurrStyle() != null)
			this.setCurrStyle(this.originStyle);
		
	}
	

	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
		
	}
	public int getNumBombPerRound() {
		return numBombPerRound;
	}
	public void setNumBombPerRound(int numBombPerRound) {
		this.numBombPerRound = numBombPerRound;
	
	}
	public float getRoundInterval() {
		return roundInterval;
	}
	public void setRoundInterval(float roundInterval) {
		this.roundInterval = roundInterval;
	
	}
	public float getPowerX() {
		return powerX;
	}
	public void setPowerX(float powerX) {
		this.powerX = powerX;
		
	}
	public float getPowerY() {
		return powerY;
	}
	public void setPowerY(float powerY) {
		this.powerY = powerY;
		
	}

	public Explosion.Style getOriginStyle() {
		return originStyle;
	}


	public void setOriginStyle(Explosion.Style originStyle) {
		this.originStyle = originStyle;
	}


	public Explosion.Style getCurrStyle() {
		return currStyle;
	}


	public void setCurrStyle(Explosion.Style currStyle) {
		this.currStyle = currStyle;
	}


	public float getMaxLife() {
		return maxLife;
	}


	public void setMaxLife(float maxLife) {
		this.maxLife = maxLife;
	}


	public float getCurrLife() {
		return currLife;
	}


	public float getLifeRegenPerSec() {
		return lifeRegenPerSec;
	}


	public void setLifeRegenPerSec(float lifeRegenPerSec) {
		this.lifeRegenPerSec = lifeRegenPerSec;
	}


	public void setCurrLife(float currLife) {
		this.currLife = currLife;
	}

	
	
}

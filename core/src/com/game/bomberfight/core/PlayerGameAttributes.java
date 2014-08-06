package com.game.bomberfight.core;

import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Explosion.Style;

public class PlayerGameAttributes {
	/**
	 * basic attributes
	 */
	private float life; //life of player
	private boolean hasLife = false;
	private float speed; //speed of player
	private boolean hasSpeed = false;
	
	/**
	 * bomb  and explosion attributes
	 */
	private int numBombPerRound; //number of bomb player can put in one round
	private boolean hasNumBombPerRound = false;
	private float roundInterval; //time delay between two rounds
	private boolean hasRoundInterval = false;
	private float powerX; //power x of explosion
	private boolean hasPowerX = false;
	private float powerY; //power y of explosion
	private boolean hasPowerY = false;
	private Explosion.Style originStyle; //original style of explosion
	private Explosion.Style currStyle; //current style of explosion
	private boolean hasStyle = false;
	
	
	
	public PlayerGameAttributes(){
		this.life = 0;
		this.speed = 0;
		this.numBombPerRound = 0;
		this.roundInterval = 0;
		this.powerX = 0;
		this.powerY = 0;
		this.originStyle = null;
		this.currStyle = null;
	}
	
	
	public PlayerGameAttributes(float life, float speed, int numBombPerRound,
			float roundInterval, float powerX, float powerY,
			Style explosionStyle) {
		
		this.setLife(life);
		this.setSpeed(speed);
		this.setNumBombPerRound(numBombPerRound);
		this.setRoundInterval(roundInterval);
		this.setPowerX(powerX);
		this.setPowerY(powerY);
		this.setCurrStyle(explosionStyle);
	}
	
	public PlayerGameAttributes(PlayerGameAttributes attr){
		this(attr.getLife(), attr.getSpeed(), attr.getNumBombPerRound(), attr.getRoundInterval(), attr.getPowerX(), attr.getPowerY(), attr.getCurrStyle());
	}
	
	
	/**
	 * add given attributes
	 * @param attr
	 */
	public void add(PlayerGameAttributes attr){
		if (attr.hasLife) {
			this.setLife(this.getLife() + attr.getLife());
		}
		if (attr.hasSpeed) {
			this.setSpeed(this.getSpeed() + attr.getSpeed());
		}
		if (attr.hasNumBombPerRound) {
			this.setNumBombPerRound(this.getNumBombPerRound() + attr.getNumBombPerRound());
		}
		if (attr.hasRoundInterval) {
			this.setRoundInterval(this.getRoundInterval() + attr.getRoundInterval());
		}
		if (attr.hasPowerX) {
			this.setPowerX(this.getPowerX() + attr.getPowerX());
		}
		if (attr.hasPowerY) {
			this.setPowerY(this.getPowerY() + attr.getPowerY());
		}
		if (attr.hasStyle) {
			if (this.originStyle == null) {
				this.originStyle = this.currStyle;
			}
			if(attr.getCurrStyle() != null)
				this.setCurrStyle(attr.getCurrStyle());
		}
	}
	
	public void minus(PlayerGameAttributes attr){
		if (attr.hasLife) {
			this.setLife(this.getLife() - attr.getLife());
		}
		if (attr.hasSpeed) {
			this.setSpeed(this.getSpeed() - attr.getSpeed());
		}
		if (attr.hasNumBombPerRound) {
			this.setNumBombPerRound(this.getNumBombPerRound() - attr.getNumBombPerRound());
		}
		if (attr.hasRoundInterval) {
			this.setRoundInterval(this.getRoundInterval() - attr.getRoundInterval());
		}
		if (attr.hasPowerX) {
			this.setPowerX(this.getPowerX() - attr.getPowerX());
		}
		if (attr.hasPowerY) {
			this.setPowerY(this.getPowerY() - attr.getPowerY());
		}
		if (attr.hasStyle) {
			this.setCurrStyle(this.originStyle);
		}
	}
	
	public float getLife() {
		return life;
	}
	public void setLife(float life) {
		this.life = life;
		this.hasLife = true;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
		this.hasSpeed = true;
	}
	public int getNumBombPerRound() {
		return numBombPerRound;
	}
	public void setNumBombPerRound(int numBombPerRound) {
		this.numBombPerRound = numBombPerRound;
		this.hasNumBombPerRound = true;
	}
	public float getRoundInterval() {
		return roundInterval;
	}
	public void setRoundInterval(float roundInterval) {
		this.roundInterval = roundInterval;
		this.hasRoundInterval = true;
	}
	public float getPowerX() {
		return powerX;
	}
	public void setPowerX(float powerX) {
		this.powerX = powerX;
		this.hasPowerX = true;
	}
	public float getPowerY() {
		return powerY;
	}
	public void setPowerY(float powerY) {
		this.powerY = powerY;
		this.hasPowerY = true;
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
		this.hasStyle = true;
	}

	
	
}

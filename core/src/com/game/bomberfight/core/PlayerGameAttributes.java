package com.game.bomberfight.core;

import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Explosion.Style;

public class PlayerGameAttributes {
	/**
	 * basic attributes
	 */
	private float life; //life of player
	private float speed; //speed of player
	
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
		
		this.life = life;
		this.speed = speed;
		this.numBombPerRound = numBombPerRound;
		this.roundInterval = roundInterval;
		this.powerX = powerX;
		this.powerY = powerY;
		this.currStyle = explosionStyle;
		this.originStyle = currStyle;
	}
	
	public PlayerGameAttributes(PlayerGameAttributes attr){
		this.life = attr.life;
		this.speed = attr.speed;
		this.numBombPerRound = attr.numBombPerRound;
		this.roundInterval = attr.roundInterval;
		this.powerX = attr.powerX;
		this.powerY = attr.powerY;
		this.originStyle = attr.currStyle;
		this.currStyle = attr.originStyle;
	
	}
	
	
	/**
	 * add given attributes
	 * @param attr
	 */
	public void add(PlayerGameAttributes attr){
		this.setLife(this.getLife() + attr.getLife());
		this.setSpeed(this.getSpeed() + attr.getSpeed());
		this.setNumBombPerRound(this.getNumBombPerRound() + attr.getNumBombPerRound());
		this.setRoundInterval(this.getRoundInterval() + attr.getRoundInterval());
		this.setPowerX(this.getPowerX() + attr.getPowerX());
		this.setPowerY(this.getPowerY() + attr.getPowerY());
		if(attr.getCurrStyle() != null)
		this.setCurrStyle(attr.getCurrStyle());
	}
	
	public void minus(PlayerGameAttributes attr){
		this.setLife(this.getLife() - attr.getLife());
		this.setSpeed(this.getSpeed() - attr.getSpeed());
		this.setNumBombPerRound(this.getNumBombPerRound() - attr.getNumBombPerRound());
		this.setRoundInterval(this.getRoundInterval() - attr.getRoundInterval());
		this.setPowerX(this.getPowerX() - attr.getPowerX());
		this.setPowerY(this.getPowerY() - attr.getPowerY());
		this.setCurrStyle(this.originStyle);
	}
	
	public float getLife() {
		return life;
	}
	public void setLife(float life) {
		this.life = life;
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

	
	
}

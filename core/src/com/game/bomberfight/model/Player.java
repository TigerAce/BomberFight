package com.game.bomberfight.model;

public abstract class Player extends GameObject{
	//location of player
	protected float x;
	protected float y;
	
	protected float speed;
	
	protected Player(float xPos, float yPos, float speed){
		super(xPos, yPos);
		this.speed = speed;
	}
	

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public abstract void draw();
	public abstract void move();
	public abstract void placeBomb();
	
}

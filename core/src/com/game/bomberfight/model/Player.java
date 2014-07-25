package com.game.bomberfight.model;

public abstract class Player extends GameObject{

	protected float x;
	protected float y;
	protected float speed;

    /**
     * Constructor of Player
     * @param xPos x position
     * @param yPos y position
     * @param speed moving speed
     */
	public Player(float xPos, float yPos, float speed){
		super(xPos, yPos);
		this.speed = speed;
	}

    /**
     * Player's moving logic
     */
    public abstract void move();

    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

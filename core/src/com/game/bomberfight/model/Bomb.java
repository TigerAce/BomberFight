package com.game.bomberfight.model;

import com.game.bomberfight.interfaces.Explosible;

public abstract class Bomb extends GameObject implements Explosible{
	
	protected float time;
	protected float powerX;
	protected float powerY;
	
	protected Bomb(float x, float y, float time, float powerX, float powerY){
		super(x, y);
		this.time = time;
		this.powerX = powerX;
		this.powerY = powerY;
	}
	
}

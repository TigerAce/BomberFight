package com.game.bomberfight.model;

import com.game.bomberfight.interfaces.Explosible;

public abstract class Explosive extends GameObject implements Explosible{
	
	protected float time;
	protected float powerX;
	protected float powerY;
	
	protected Explosive(float x, float y, float time, float powerX, float powerY){
		super(x, y);
		this.time = time;
		this.powerX = powerX;
		this.powerY = powerY;
	}
	
}

package com.game.bomberfight.model;

public abstract class Barrier extends GameObject{
	protected float size;
	
	protected Barrier(float x, float y, float size){
		super(x, y);
		this.size = size;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}
	
}

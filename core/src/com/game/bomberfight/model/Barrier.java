package com.game.bomberfight.model;

public abstract class Barrier extends GameObject{
	protected float width;
	protected float height;
	
	protected Barrier(float x, float y, float width, float height){
		super(x, y);
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	
	
}

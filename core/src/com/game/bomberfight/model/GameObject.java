package com.game.bomberfight.model;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject implements Disposable{
	protected float x;
	protected float y;
	

	
	public GameObject(float x, float y){
		this.x = x;
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float f) {
		this.y = f;
	}
	
	
	public abstract void update(float delta);
	public abstract void draw();



}

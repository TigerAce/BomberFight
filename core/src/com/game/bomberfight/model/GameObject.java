package com.game.bomberfight.model;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject implements Disposable{
	protected float x;
	protected float y;


    /**
     * Constructor of game object
     * @param x x position
     * @param y y position
     */
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

    /**
     * Game object update logic goes here
     * @param delta
     */
	public abstract void update(float delta);

    /**
     * Game object drawing logic goes here
     */
	public abstract void draw();



}

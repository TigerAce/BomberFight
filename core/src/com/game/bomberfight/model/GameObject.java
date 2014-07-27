package com.game.bomberfight.model;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject implements Disposable{

	protected float x;
	protected float y;
    protected Body box2dBody;


    /**
     * Constructor of game object
     * @param x x position
     * @param y y position
     */
	public GameObject(float x, float y){
		this.x = x;
		this.y = y;
	}

    /**
     * To create body inside the box2d world
     */
    public abstract void create();

    /**
     * Game object update logic goes here
     * @param delta
     */
	public abstract void update(float delta);

    /**
     * Game object drawing logic goes here
     */
	public abstract void draw();

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
}

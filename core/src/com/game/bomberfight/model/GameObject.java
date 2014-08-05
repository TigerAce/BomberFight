package com.game.bomberfight.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject implements Disposable{

    /**
     * Different state of GameObject
     */
    public static int RECYCLE = 0;
    public static int ACTIVE = 1;

	protected float x;
	protected float y;
    protected Body box2dBody;
    protected Sprite sprite;
    protected String name;
    protected int state;



    public GameObject(){
    	this(0, 0);
    	state = ACTIVE;
    }
    /**
     * Constructor of game object
     * @param x x position
     * @param y y position
     */
	public GameObject(float x, float y){
		this.x = x;
		this.y = y;
        state = ACTIVE;
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
	public abstract void draw(SpriteBatch batch);

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Default dispose function
     */
    @Override
    public void dispose () {
        state = RECYCLE;
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
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

	public Body getBox2dBody() {
		return box2dBody;
	}

	public void setBox2dBody(Body box2dBody) {
		this.box2dBody = box2dBody;
	}
	/**
	 * @return the sprite
	 */
	public Sprite getSprite() {
		return sprite;
	}
	/**
	 * @param sprite the sprite to set
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}

package com.game.bomberfight.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.screen.GamePlay;

public class Player extends GameObject implements Controllable, Destructible{

	protected float x;
	protected float y;
	protected float speed;
	protected float life;
	protected Shape shape;
    protected Vector2 movement = new Vector2();
    protected float width = -1;
    protected float height = -1;
    protected Sprite sprite;
    protected Map<Integer, Boolean> keyMap = new LinkedHashMap<Integer, Boolean>();
    

    /**
     * Constructor of Player
     * @param xPos x position
     * @param yPos y position
     * @param speed moving speed
     */
	public Player(float xPos, float yPos, float speed, float life){
		super(xPos, yPos);
		this.speed = speed;
		this.life = life;
	}
	
	/**
	 * @param xPos
	 * @param yPos
	 * @param width width of player body
	 * @param height height of player body
	 * @param speed
	 */
	public Player(float xPos, float yPos, float width, float height, float speed){
		super(xPos, yPos);
		this.speed = speed;
		this.width = width;
		this.height = height;
	}

    /***************************************
     * GameObject implementations          *
     ***************************************/

    @Override
    public void create() {
        /**
         * Box2d body creation
         */
        Gdx.app.log("INFO", "Create player box2d body");

        BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(x, y);
        playerDef.fixedRotation = true;
        

        shape = new PolygonShape();
        if (width <= 0 && height <= 0) {
        	((PolygonShape) shape).setAsBox(.5f, .5f);
		} else {
			((PolygonShape) shape).setAsBox(width, height);
		}

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = shape;
        playerFixtureDef.density = 2f;
        playerFixtureDef.friction = .25f;
        playerFixtureDef.restitution = 0f;

        Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        box2dBody = ((GamePlay)currentScreen).getWorld().createBody(playerDef);
        box2dBody.createFixture(playerFixtureDef);
        box2dBody.setUserData(this);

        // Add into GameObjectManager
        ((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
    }

    @Override
    public void update(float delta) {

        /**
         * Don't know what's going on here?
         * See this: http://www.iforce2d.net/b2dtut/constant-speed
         */
    	if(this.life <= 0){
    		 Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
    		((GamePlay)currentScreen).getWorld().destroyBody(box2dBody);
    		 dispose();
    	}else{
    		box2dBody.setLinearVelocity(movement);
    		processInput();
    	}
    }

    @Override
    public void draw(SpriteBatch batch) {

    }
    
    @Override
    public void dispose(){
    	this.shape.dispose();
    	super.dispose();
    }

    /***************************************
	 * Controllable implementations *
	 ***************************************/

	@Override
	public boolean doKeyUp(int keycode) {
		keyMap.put(keycode, false);
		return true;
	}

    @Override
    public boolean doKeyDown(int keycode) {
        keyMap.put(keycode, true);
        return true;
    }

    @Override
    public boolean doKeyTyped(char character) {
        return false;
    }

    @Override
    public boolean doTouchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean doTouchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean doTouchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean doMouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean doScrolled(int amount) {
        return false;
    }

	/**
	 * This function process input continuously based on status, different
	 * from doKeyDown and doKeyUp which only get executed once
	 */
	public void processInput() {
		Iterator<Entry<Integer, Boolean>> iter = keyMap.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
		    int key = (Integer) entry.getKey();
		    boolean value = (Boolean) entry.getValue();
		    switch (key) {
			case Input.Keys.W:
				if (value) {
					movement.y = speed;
				} else {
					movement.y = 0;
					iter.remove();
				}
				break;
			case Input.Keys.A:
				if (value) {
					movement.x = -speed;
				} else {
					movement.x = 0;
					iter.remove();
				}
				break;
			case Input.Keys.S:
				if (value) {
					movement.y = -speed;
				} else {
					movement.y = 0;
					iter.remove();
				}
				break;
			case Input.Keys.D:
				if (value) {
					movement.x = speed;
				} else {
					movement.x = 0;
					iter.remove();
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void damage(ContactImpulse impulse) {
		this.life -= impulse.getNormalImpulses()[0];
	}
}

package com.game.bomberfight.model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.screen.GamePlay;

public class Player extends GameObject implements Controllable{

	protected float x;
	protected float y;
	protected float speed;
    protected Vector2 movement = new Vector2();
    protected float width = -1;
    protected float height = -1;
    protected Sprite sprite;

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

        PolygonShape playerShape = new PolygonShape();
        if (width <= 0 && height <= 0) {
        	playerShape.setAsBox(.5f, .5f);
		} else {
			playerShape.setAsBox(width, height);
		}

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerShape;
        playerFixtureDef.density = 2f;
        playerFixtureDef.friction = .25f;
        playerFixtureDef.restitution = 0f;

        Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        box2dBody = ((GamePlay)currentScreen).getWorld().createBody(playerDef);
        box2dBody.createFixture(playerFixtureDef);
        box2dBody.setUserData(this);
        playerShape.dispose();

        // Add into GameObjectManager
        ((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
    }

    @Override
    public void update(float delta) {

        /**
         * Don't know what's going on here?
         * See this: http://www.iforce2d.net/b2dtut/constant-speed
         */
        Vector2 velocity = box2dBody.getLinearVelocity();
        Vector2 velChange = movement.cpy().sub(velocity);
        Vector2 impulse = velChange.scl(box2dBody.getMass());
        box2dBody.applyLinearImpulse(impulse,box2dBody.getWorldCenter(), true);

    }

    @Override
    public void draw() {

    }

    /***************************************
     * Controllable implementations        *
     ***************************************/

    @Override
    public boolean doKeyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.S:
                movement.y = 0;
                return true;
            case Input.Keys.A:
            case Input.Keys.D:
                movement.x = 0;
                return true;
        }
        return false;
    }

    @Override
    public boolean doKeyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                movement.y = speed;
                return true;
            case Input.Keys.S:
                movement.y = -speed;
                return true;
            case Input.Keys.A:
                movement.x = -speed;
                return true;
            case Input.Keys.D:
                movement.x = speed;
                return true;
        }

        return false;
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
}

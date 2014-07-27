package com.game.bomberfight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.screen.GamePlay;

public class Player extends GameObject implements Controllable{

	protected float x;
	protected float y;
	protected float speed;

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
        playerShape.setAsBox(.5f, .5f);

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerShape;
        playerFixtureDef.density = 2f;
        playerFixtureDef.friction = .25f;
        playerFixtureDef.restitution = 0f;

        Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        box2dBody = ((GamePlay)currentScreen).getWorld().createBody(playerDef);
        box2dBody.createFixture(playerFixtureDef);
        playerShape.dispose();

        // Add into GameObjectManager
        ((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw() {

    }

    @Override
    public void dispose() {

    }

    /***************************************
     * Controllable implementations        *
     ***************************************/

    @Override
    public boolean doKeyUp(int keycode) {
        return false;
    }

    @Override
    public boolean doKeyDown(int keycode) {
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

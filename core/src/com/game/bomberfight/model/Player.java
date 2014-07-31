package com.game.bomberfight.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.game.bomberfight.core.PlayerGameAttributes;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.screen.GamePlay;

public class Player extends GameObject implements Controllable, Destructible{
	
	protected PlayerGameAttributes attr;

	protected Shape shape;
    protected Vector2 movement = new Vector2();
    protected float width = -1;
    protected float height = -1;
    protected Sprite sprite;
    protected Map<Integer, Boolean> keyMap = new LinkedHashMap<Integer, Boolean>();
    protected Direction direction = Direction.up;
    protected Animation animation = null;
    protected float animTime;
    
    public enum Direction {
    	left, right, down, up, left_up, left_down, right_up, right_down
    }
    

    /**
     * Constructor of Player
     * @param xPos x position
     * @param yPos y position
     * @param speed moving speed
     */
	public Player(float xPos, float yPos, float speed, float life){
		super(xPos, yPos);
		attr = new PlayerGameAttributes();
		attr.setSpeed(speed);
		attr.setLife(life);
	}
	
	/**
	 * @param xPos
	 * @param yPos
	 * @param width width of player body
	 * @param height height of player body
	 * @param speed
	 */
	public Player(float xPos, float yPos, float width, float height, float speed, float life){
		super(xPos, yPos);
		attr = new PlayerGameAttributes();
		attr.setSpeed(speed);
		attr.setLife(life);
		
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
        	((PolygonShape) shape).setAsBox(1, 1);
		} else {
			((PolygonShape) shape).setAsBox(width / 2, height / 2);
		}

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = shape;
        playerFixtureDef.density = 0.0001f;
        playerFixtureDef.friction = .25f;
        playerFixtureDef.restitution = 0f;

        Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        box2dBody = ((GamePlay)currentScreen).getWorld().createBody(playerDef);
        box2dBody.createFixture(playerFixtureDef);
        box2dBody.setUserData(this);

        // Add into GameObjectManager
        ((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
        
        sprite = new Sprite();
        sprite.setSize(width, height);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    @Override
    public void update(float delta) {

        /**
         * Don't know what's going on here?
         * See this: http://www.iforce2d.net/b2dtut/constant-speed
         */
    	if(attr.getLife() <= 0){
    		System.out.println(box2dBody);
    		 Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
    		((GamePlay)currentScreen).getWorld().destroyBody(box2dBody);
    		 dispose();
    	}else{
    		//box2dBody.setLinearVelocity(movement);
    		forceToVelocity();
    		if (this.movement.x != 0 || this.movement.y != 0) {
        		animTime += delta;
    		}
        	updateDirection();
    	}
    }

    @Override
    public void draw(SpriteBatch batch) {
    	if (animation != null) {
			sprite.setRegion(animation.getKeyFrame(animTime));
			sprite.setPosition(box2dBody.getPosition().x - sprite.getWidth()/2, box2dBody.getPosition().y - sprite.getHeight()/2);
			sprite.setRotation(box2dBody.getAngle() * MathUtils.radiansToDegrees);
			sprite.draw(batch);
		}
    }
    
    @Override
    public void dispose(){
    	shape.dispose();
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

	@Override
	public void damage(ContactImpulse impulse) {
		attr.setLife(attr.getLife() - impulse.getNormalImpulses()[0]);
	}

	/**
	 * @return the animation
	 */
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * @param animation the animation to set
	 */
	public void setAnimation(Texture texture, int col, int row) {
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/col, texture.getHeight()/row);
        TextureRegion[] walkFrames = new TextureRegion[col * row];
        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        animation = new Animation(0.25f, walkFrames);
        animation.setPlayMode(PlayMode.LOOP);
        animTime = 0f;
	}
	
	public void updateDirection() {
		float angle = box2dBody.getAngle() * MathUtils.radiansToDegrees;
		if (movement.x > 0) {
			direction = Direction.right;
			angle = -90;
		}
		if (movement.x < 0) {
			direction = Direction.left;
			angle = 90;
		}
		if (movement.y > 0) {
			direction = Direction.up;
			angle = 0;
		}
		if (movement.y < 0) {
			direction = Direction.down;
			angle = 180;
		}
		if (movement.x > 0 && movement.y > 0) {
			direction = Direction.right_up;
			angle = -45;
		}
		if (movement.x < 0 && movement.y > 0) {
			direction = Direction.left_up;
			angle = 45;
		}
		if (movement.x > 0 && movement.y < 0) {
			direction = Direction.right_down;
			angle = -135;
		}
		if (movement.x < 0 && movement.y < 0) {
			direction = Direction.left_down;
			angle = 135;
		}
		box2dBody.setTransform(box2dBody.getPosition(), angle * MathUtils.degreesToRadians);
	}

	public PlayerGameAttributes getAttr() {
		return attr;
	}

	public void setAttr(PlayerGameAttributes attr) {
		this.attr = attr;
	}

	/**
	 * @return the movement
	 */
	public Vector2 getMovement() {
		return movement;
	}

	/**
	 * @return the keyMap
	 */
	public Map<Integer, Boolean> getKeyMap() {
		return keyMap;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	
	public void moveUp() {
		this.movement.y = this.attr.getSpeed();
	}
	
	public void moveDown() {
		this.movement.y = -this.attr.getSpeed();
	}
	
	public void stopVerticalMove() {
		this.movement.y = 0;
	}
	
	public void moveLeft() {
		this.movement.x = -this.attr.getSpeed();
	}
	
	public void moveRight() {
		this.movement.x = this.attr.getSpeed();
	}
	
	public void stopHorizontalMove() {
		this.movement.x = 0;
	}
	
	public void forceToVelocity() {
		Vector2 vel = box2dBody.getLinearVelocity();
		float velChangex = movement.x - vel.x;
		float velChangey = movement.y - vel.y;
	    float impulsex = box2dBody.getMass() * velChangex;
	    float impulsey = box2dBody.getMass() * velChangey;
	    box2dBody.applyLinearImpulse(impulsex, impulsey, box2dBody.getWorldCenter().x, box2dBody.getWorldCenter().y, true);
	}
}

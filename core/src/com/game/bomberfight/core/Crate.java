package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.game.bomberfight.interfaces.Breakable;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.model.Barrier;
import com.game.bomberfight.screen.GamePlay;

public class Crate extends Barrier implements Destructible, Breakable{

	private Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
	private Shape crateShape;
	private float life;
	
	public Crate(float x, float y, float width, float height, float life) {
		super(x, y, width, height);
		this.life = life;
	}

    @Override
    public void create() {
    	this.name = "crate";
    	
    	FixtureDef crateFixtureDef = new FixtureDef();
		BodyDef crateDef = new BodyDef();
		// default crate
		
		crateDef.type = BodyType.DynamicBody;
		crateDef.position.set(x, y);
		
		//shape
		crateShape = new PolygonShape();
		((PolygonShape) crateShape).setAsBox(width/2, height/2);
		
		//fixture
		
		crateFixtureDef.density = 10.0f;
		crateFixtureDef.friction = 0.6f;
		crateFixtureDef.restitution = 0;
		crateFixtureDef.shape = crateShape;
		
		
		box2dBody = ((GamePlay)currentScreen).getWorld().createBody(crateDef);
		box2dBody.createFixture(crateFixtureDef);
		box2dBody.setLinearDamping(0.7f);
		box2dBody.setAngularDamping(0.9f);
		box2dBody.setUserData(this);
		
		sprite = new Sprite(((GamePlay)currentScreen).getResourcesManager().getTexture("crate"));
		sprite.setSize(width, height);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
    	
    }

    @Override
	public void update(float delta) {
    	if(this.life <= 0){
    		((GamePlay)currentScreen).getWorld().destroyBody(box2dBody);
			dispose();
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if( sprite != null){
			sprite.setPosition(box2dBody.getPosition().x - sprite.getWidth()/2, box2dBody.getPosition().y - sprite.getHeight()/2);
			sprite.setRotation(box2dBody.getAngle() * MathUtils.radiansToDegrees);
			sprite.draw(batch);
		}
		
	}

	@Override
	public void dispose() {
		crateShape.dispose();
		super.dispose();
		
	}

	@Override
	public void toFragments(float fragCenterX, float fragCenterY, int fragPieces) {
		// TODO Auto-generated method stub
		
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	@Override
	public void damage(ContactImpulse impulse) {
		this.life -= impulse.getNormalImpulses()[0];
	}

}

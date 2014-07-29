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
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.game.bomberfight.interfaces.Breakable;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.model.Barrier;
import com.game.bomberfight.screen.GamePlay;

public class Brick extends Barrier implements Destructible, Breakable{

	private Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
	private Shape brickShape;
	private float life;
	
	public Brick(float x, float y, float width, float height, float life) {
		super(x, y, width, height);
		this.life = life;
	}

	@Override
	public void create() {
		this.name = "brick";
		
		FixtureDef brickFixtureDef = new FixtureDef();
		BodyDef brickDef = new BodyDef();
	
		
		brickDef.type = BodyType.DynamicBody;
		brickDef.position.set(x, y);
		
		//shape
		brickShape = new PolygonShape();
		((PolygonShape) brickShape).setAsBox(width/2, height/2);
		
		//fixture
		
		brickFixtureDef.density = 30.0f;
		brickFixtureDef.friction = 0.6f;
		brickFixtureDef.restitution = 0;
		brickFixtureDef.shape = brickShape;
		
		
		box2dBody = ((GamePlay)currentScreen).getWorld().createBody(brickDef);
		box2dBody.createFixture(brickFixtureDef);
		box2dBody.setLinearDamping(1.0f);
		box2dBody.setAngularDamping(1.0f);
		box2dBody.setUserData(this);
		
		//sprite
		sprite = new Sprite(new Texture(Gdx.files.internal("img/texture/brick3.jpg")));
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
		brickShape.dispose();
		super.dispose();
		
	}


	@Override
	public void damage(ContactImpulse impulse) {
		this.life -= impulse.getNormalImpulses()[0];
	}

	@Override
	public void toFragments(float fragCenterX, float fragCenterY, int fragPieces) {
		// TODO Auto-generated method stub
		
	}

	

}

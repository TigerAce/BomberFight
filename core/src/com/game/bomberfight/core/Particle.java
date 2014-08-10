package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.GameObject;
import com.game.bomberfight.screen.GamePlay;
import com.game.bomberfight.utility.UserData;

public class Particle extends GameObject{

	private float density;
	private float lifespan;
	private float blastPowerX;
	private float blastPowerY;
	private Explosion parent;
	private Vector2 dirVector;
	private Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
	private ParticleEffect effect;
	
	public Particle(float x, float y, float angle, float density, float lifespan, float blastPowerX, float blastPowerY, Explosion parent) {
		super(x, y);
		dirVector = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
		this.density = density;
		this.lifespan = lifespan;
		this.blastPowerX = blastPowerX;
		this.blastPowerY = blastPowerY;
		this.parent = parent;
	
		effect = new ParticleEffect(((GamePlay)currentScreen).getAssetManager().get("particle/flame.p", ParticleEffect.class));
		effect.start();
	}

    @Override
    public void create() {
    	this.name = "PARTICLE";
    	
    	FixtureDef particleFixtureDef = new FixtureDef();
		BodyDef particleBodyDef = new BodyDef();
		
	
	
		particleFixtureDef.density = density;
		particleFixtureDef.friction = 0;
		particleFixtureDef.restitution = 0.99f;
		particleFixtureDef.filter.groupIndex = -1;
	
		CircleShape shape = new CircleShape();
		shape.setRadius(0.1f);
		
		particleFixtureDef.shape = shape;
		
		particleBodyDef.type = BodyType.DynamicBody;
		particleBodyDef.position.x = x;
		particleBodyDef.position.y = y;
		particleBodyDef.fixedRotation = true;
		particleBodyDef.bullet = true;
		particleBodyDef.linearDamping = 3.5f;
		
	
		
	    box2dBody= ((GamePlay)currentScreen).getWorld().createBody(particleBodyDef);
		
		box2dBody.createFixture(particleFixtureDef);
		
		
		box2dBody.setLinearVelocity(blastPowerX * dirVector.x,
							 blastPowerY * dirVector.y);
		
		box2dBody.setUserData(new UserData(this, false));

    }

    @Override
	public void update(float delta) {
    	this.lifespan -= delta;
		
		if(this.lifespan <= 0){
		    dispose();
			return;
		}
		
		this.x = this.box2dBody.getPosition().x ;
		this.y = this.box2dBody.getPosition().y ;
		
		effect.setPosition(this.x, this.y);
		effect.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		effect.draw(batch);
	}

	@Override
	public void dispose() {
		parent.setParticleCounter(parent.getParticleCounter() - 1);
		
		if(box2dBody.getUserData() != null){
			((UserData)box2dBody.getUserData()).isDead = true;
			}
		//((GamePlay)currentScreen).getWorld().destroyBody(this.box2dBody);
		super.dispose();
		effect.dispose();
		
	}



}

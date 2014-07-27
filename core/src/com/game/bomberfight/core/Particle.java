package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Explosive;
import com.game.bomberfight.model.GameObject;
import com.game.bomberfight.screen.GamePlay;

public class Particle extends GameObject{

	/*
	 * private float x;
	private float y;
	private Body body;
	private int lifespan = 1000;
	private Explosion parent;
	
	private World world;
	private Vector2 dirVector;
	private FixtureDef fixtureDef;
	private BodyDef bodyDef;
	
	
	
	public Particles(float x, float y, float angle, float density, float blastPower,
			Explosion parent, World world) {
		dirVector = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
		fixtureDef = new FixtureDef();
		bodyDef = new BodyDef();
		
		this.world = world;
		this.x = x;
		this.y = y;
		this.parent = parent;
	
		fixtureDef.density = density;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0.99f;
		fixtureDef.filter.groupIndex = -1;
	
		CircleShape shape = new CircleShape();
		shape.setRadius(0.1f);
		
		fixtureDef.shape = shape;
		
		bodyDef.type = BodyType.DynamicBody;
		
		//really need?
		bodyDef.position.x = x;
		bodyDef.position.y = y;
		
		bodyDef.fixedRotation = true;
		
		bodyDef.bullet = true;
		
		bodyDef.linearDamping = 3.5f;
		
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		
		
		body.setLinearVelocity(blastPower * dirVector.x,
							 blastPower * dirVector.y);
		
		body.setUserData(new MyUserData("particle", this, null));
		
	}
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public void update(){
		this.lifespan -= 15;
		
		if(this.lifespan <= 0){
			
			kill(); 
			return;
		}
		
		this.x = this.body.getPosition().x ;
		this.y = this.body.getPosition().y ;
	}
	
	public void kill(){
		parent.removeParticles(this);
	}

	 */
	private Body particleBody;
	private float angle;
	private float density;
	private float lifespan;
	private float blastPowerX;
	private float blastPowerY;
	private Explosion parent;
	private Vector2 dirVector;
	
	
	public Particle(float x, float y, float angle, float density, float lifespan, float blastPowerX, float blastPowerY, Explosion parent) {
		super(x, y);
		dirVector = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
	//	this.angle = angle;
		this.density = density;
		this.lifespan = lifespan;
		this.blastPowerX = blastPowerX;
		this.blastPowerY = blastPowerY;
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

    @Override
    public void create() {
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
		
		//really need?
		particleBodyDef.position.x = x;
		particleBodyDef.position.y = y;
		
		particleBodyDef.fixedRotation = true;
		
		particleBodyDef.bullet = true;
		
		particleBodyDef.linearDamping = 3.5f;
		
		
	    Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
	    particleBody= ((GamePlay)currentScreen).getWorld().createBody(particleBodyDef);
		
		particleBody.createFixture(particleFixtureDef);
		
		
		particleBody.setLinearVelocity(blastPowerX * dirVector.x,
							 blastPowerY * dirVector.y);
    }

    @Override
	public void update(float delta) {
    	this.lifespan -= delta;
		
		if(this.lifespan <= 0){
		    dispose();
			return;
		}
		
		this.x = this.particleBody.getPosition().x ;
		this.y = this.particleBody.getPosition().y ;
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		parent.removeParticles(this);
		
	}

	public Body getParticleBody() {
		return particleBody;
	}

}

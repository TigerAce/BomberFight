package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.game.bomberfight.model.GameObject;
import com.game.bomberfight.screen.GamePlay;

public class Item extends GameObject{

	private Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
	private PlayerGameAttributes attr;
	private Shape itemShape;
	private boolean picked = false;
	
	public Item(float x, float y, PlayerGameAttributes attr) {
		super(x, y);
		this.attr = attr;
	}
	
	public Item(float x, float y) {
		super(x, y);
		attr = new PlayerGameAttributes();
	}



	@Override
	public void create() {
		this.name = "item";
    	
    	FixtureDef itemFixtureDef = new FixtureDef();
		BodyDef itemDef = new BodyDef();
		// default crate
		
		itemDef.type = BodyType.DynamicBody;
		itemDef.position.set(x, y);
		
		//shape
		itemShape = new PolygonShape();
		((PolygonShape) itemShape).setAsBox(0.5f, 0.5f);
		
		//fixture
		
		itemFixtureDef.density = 10.0f;
		itemFixtureDef.friction = 0.6f;
		itemFixtureDef.restitution = 0;
		itemFixtureDef.shape = itemShape;
		
		
		box2dBody = ((GamePlay)currentScreen).getWorld().createBody(itemDef);
		box2dBody.createFixture(itemFixtureDef);
		box2dBody.setLinearDamping(0.7f);
		box2dBody.setAngularDamping(0.9f);
		box2dBody.setUserData(this);
		
	/*	sprite = new Sprite(((GamePlay)currentScreen).getResourcesManager().getTexture("crate"));
		sprite.setSize(width, height);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);*/
		
		((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
		
	}

	@Override
	public void update(float delta) {
		if(picked){
			((GamePlay)currentScreen).getWorld().destroyBody(box2dBody);
			this.dispose();
		}
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose(){
		itemShape.dispose();
		super.dispose();
	}
	
	public PlayerGameAttributes getAttr() {
		return attr;
	}

	public void setAttr(PlayerGameAttributes attr) {
		this.attr = attr;
	}

	public boolean isPicked() {
		return picked;
	}

	public void setPicked(boolean picked) {
		this.picked = picked;
	}
 
}

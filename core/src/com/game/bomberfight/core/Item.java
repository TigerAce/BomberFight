package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
	private float affectTime;  //-1 indicates forever affect time
	private int dropProbability; //probability interval
	private boolean picked = false;
	private boolean discard = false;
	
	public Item(float x, float y, PlayerGameAttributes attr, float affectTime, int dropProbability) {
		super(x, y);
		this.attr = attr;
		this.affectTime = affectTime;
		this.dropProbability = dropProbability;
		
	}
	
	public Item(float x, float y) {
		this(x, y, new PlayerGameAttributes(), -1, 10);
	}


	public Item(){
		this(0, 0, new PlayerGameAttributes(), -1, 10);
	}
	
	public Item(Item item){
		this(0, 0, new PlayerGameAttributes(item.getAttr()), item.affectTime, item.dropProbability);
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
		((PolygonShape) itemShape).setAsBox(2f, 2f);
		
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

		
		((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
	}

	

	public void setSprite(Texture t){
		sprite = new Sprite(t);
		sprite.setSize(4, 4);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
	}
	@Override
	public void update(float delta) {
		if(picked){
			((GamePlay)currentScreen).getWorld().destroyBody(box2dBody);
			if(sprite != null){
				sprite = null;
			}
			picked = false;
		}
		if(discard){
			dispose();
		}
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		if( sprite != null){
			System.out.println("d");
			sprite.setPosition(box2dBody.getPosition().x - sprite.getWidth()/2, box2dBody.getPosition().y - sprite.getHeight()/2);
			sprite.setRotation(box2dBody.getAngle() * MathUtils.radiansToDegrees);
			sprite.draw(batch);
		}
		
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

	public float getAffectTime() {
		return affectTime;
	}

	public void setAffectTime(float affectTime) {
		this.affectTime = affectTime;
	}

	public int getDropProbability() {
		return dropProbability;
	}

	public void setDropProbability(int dropProbability) {
		this.dropProbability = dropProbability;
	}

	public boolean isDiscard() {
		return discard;
	}

	public void setDiscard(boolean discard) {
		this.discard = discard;
	}
 
}

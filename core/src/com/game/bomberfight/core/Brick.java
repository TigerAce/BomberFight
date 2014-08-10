package com.game.bomberfight.core;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.game.bomberfight.interfaces.Breakable;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.interfaces.DropItem;
import com.game.bomberfight.model.Barrier;
import com.game.bomberfight.screen.GamePlay;
import com.game.bomberfight.utility.UserData;

public class Brick extends Barrier implements Destructible, Breakable, DropItem{

	private Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
	private Shape brickShape;
	private float life;
	
	public Brick(float x, float y, float width, float height, float life) {
		super(x, y, width, height);
		this.life = life;
	}

	@Override
	public void create() {
		this.name = "BRICK";
		
		FixtureDef brickFixtureDef = new FixtureDef();
		BodyDef brickDef = new BodyDef();
	
		
		brickDef.type = BodyType.DynamicBody;
		brickDef.position.set(x, y);
		
		//shape
		brickShape = new PolygonShape();
		((PolygonShape) brickShape).setAsBox(width/2, height/2);
		
		//fixture
		
		brickFixtureDef.density = 800.0f;
		brickFixtureDef.friction = 0.6f;
		brickFixtureDef.restitution = 0;
		brickFixtureDef.shape = brickShape;
		
		
		box2dBody = ((GamePlay)currentScreen).getWorld().createBody(brickDef);
		box2dBody.createFixture(brickFixtureDef);
		box2dBody.setLinearDamping(6.0f);
		box2dBody.setAngularDamping(4.0f);
		box2dBody.setUserData(new UserData(this, false));
		
		//sprite
		sprite = new Sprite(((GamePlay)currentScreen).getAssetManager().get("img/texture/brick3.jpg", Texture.class));
		sprite.setSize(width, height);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
		
	}
	@Override
	public void update(float delta) {
	
		if(this.life <= 0){
			dropItem();
    		
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
		((UserData)box2dBody.getUserData()).isDead = true;
		//((GamePlay)currentScreen).getWorld().destroyBody(box2dBody);
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

	@Override
	public Item dropItem() {
		//give 1/4 possibility to generate an random item in item list
		ArrayList<Item> items = ((GamePlay)currentScreen).getItemList();
		if(items.size() != 0){
			Random r = new Random();
			int rand = r.nextInt(4);
			if(rand == 1){
				int totalDropProbabilityInterval = 0;
				//add on probability intervals from item list
				for(Item item : items){
					totalDropProbabilityInterval += item.getDropProbability();
				}
				
				rand = r.nextInt(totalDropProbabilityInterval);
				
				//pick item if the random number is in its interval and create item
				int counter = 0;
				for(Item i : items){
					int prob = i.getDropProbability();
					if(rand >= counter && rand < counter + prob){
						Item tmp = new Item(i);
						
						if(i.getName() == "POWER_UP"){
							tmp.setSprite(((GamePlay)currentScreen).getAssetManager().get("img/texture/item1.png", Texture.class));
						}
						if(i.getName() == "ANNULAR")
							tmp.setSprite(((GamePlay)currentScreen).getAssetManager().get("img/texture/item2.png", Texture.class));
						if(i.getName() == "ADDBOMB")
							tmp.setSprite(((GamePlay)currentScreen).getAssetManager().get("img/texture/item3.png", Texture.class));
						tmp.setX(box2dBody.getPosition().x);
	    				tmp.setY(box2dBody.getPosition().y);
	    				tmp.create();
	    				return tmp;
					}else counter += prob;
				}
				
			}
		}
		return null;
	}

	

}

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
import com.game.bomberfight.net.Network.RequireUpdateDropItem;
import com.game.bomberfight.screen.GamePlay;

public class Crate extends Barrier implements Destructible, Breakable, DropItem{

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
		
		crateFixtureDef.density = 400.0f;
		crateFixtureDef.friction = 0.6f;
		crateFixtureDef.restitution = 0;
		crateFixtureDef.shape = crateShape;
		
		
		box2dBody = ((GamePlay)currentScreen).getWorld().createBody(crateDef);
		box2dBody.createFixture(crateFixtureDef);
		box2dBody.setLinearDamping(4f);
		box2dBody.setAngularDamping(2f);
		box2dBody.setUserData(this);
		
		sprite = new Sprite(((GamePlay)currentScreen).getAssetManager().get("img/texture/crate4.jpg", Texture.class));
		sprite.setSize(width, height);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
    	
    }

    @Override
	public void update(float delta) {
    	if(this.life <= 0){
    		if (GamePlay.gameInfo.networkMode.equals("WAN")) {
				String name = this.dropItemName();
				if (name != null) {
					RequireUpdateDropItem requireUpdateDropItem = new RequireUpdateDropItem();
					requireUpdateDropItem.id = this.getId();
					requireUpdateDropItem.name = name;
					requireUpdateDropItem.x = this.getBox2dBody().getPosition().x;
					requireUpdateDropItem.y = this.getBox2dBody().getPosition().y;
					GamePlay.client.sendTCP(requireUpdateDropItem);
					Gdx.app.log("item", requireUpdateDropItem.name+" id "+requireUpdateDropItem.id);
				}
			} else {
				this.dropItem();
			}
    		//Destroy crate
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
		//if(sprite != null)
		//	sprite.getTexture().dispose();
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

	@Override
	public Item dropItem() {
		//give 1/6 possibility to generate an random item in item list
		ArrayList<Item> items = ((GamePlay)currentScreen).getItemList();
		if(items.size() != 0){
			Random r = new Random();
			int rand = r.nextInt(6);
			if(rand > 0){
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
						
						if(i.getName() == "POWER_UP")
							tmp.setSprite(((GamePlay)currentScreen).getAssetManager().get("img/texture/item1.png", Texture.class));
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
	
	public String dropItemName() {
		ArrayList<Item> items = ((GamePlay)currentScreen).getItemList();
		if(items.size() != 0){
			Random r = new Random();
			int rand = r.nextInt(6);
			if(rand > 0){
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
						return i.getName();
					}else counter += prob;
				}
				
			}
		}
		return null;
	}

}

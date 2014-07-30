package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Explosive;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;

public class Bomb extends Explosive{

	private Player owner;
	
	
	/**
	 * create a bomb
	 * @param x x position of bomb
	 * @param y y position of bomb
	 * @param time time before explode
	 * @param powerX power on X axis
	 * @param powerY power on Y axis
	 * @param explosionStyle style to explode
	 */
	public Bomb(float x, float y, float time, float powerX, float powerY, Explosion.Style explosionStyle) {
		super(x, y, time, powerX, powerY, explosionStyle);
		
	}

	@Override
	public void explode(float x, float y, float powerX, float powerY, Explosion.Style explosionStyle) {
		
		 Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
		 
		if(explosionStyle == Explosion.Style.ANNULAR){
			AnnularExplosion e = new AnnularExplosion(x, y, 1, powerX, powerY, 1000, 64);
		   ((GamePlay)currentScreen).getExplosions().add(e);
		}
		else if(explosionStyle == Explosion.Style.CROSS){
			CrossExplosion e = new CrossExplosion(x, y, 1, powerX, powerY, 1000, 64);
			((GamePlay)currentScreen).getExplosions().add(e);
		}
	
	     ((GamePlay)currentScreen).getWorld().destroyBody(box2dBody);
	     this.dispose();
		
	}

	@Override
	public void dispose() {
		 super.dispose();
		
	}

    @Override
    public void create() {
    	BodyDef bombDef = new BodyDef();
		FixtureDef bombFixtureDef = new FixtureDef();
	
		//default setting of bomb
	
		bombDef.type = BodyType.DynamicBody;
		bombDef.position.set(x, y);
		
		//shape
		CircleShape bombShape = new CircleShape();
		bombShape.setRadius(1f);
		
		//fixture
		bombFixtureDef.density = 2.5f;
		bombFixtureDef.friction = .25f;
		bombFixtureDef.restitution = .75f;
		
		bombFixtureDef.shape = bombShape;
		

        Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        box2dBody = ((GamePlay)currentScreen).getWorld().createBody(bombDef);
		box2dBody.createFixture(bombFixtureDef);
		//bombBody.setUserData(new MyUserData("bomb", this, null));
		
		((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
		
    }

    @Override
	public void update(float delta) {
		this.time -= delta;
		if(time <= 0) this.explode(box2dBody.getPosition().x, box2dBody.getPosition().y, powerX, powerY, explosionStyle);
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	
	
}

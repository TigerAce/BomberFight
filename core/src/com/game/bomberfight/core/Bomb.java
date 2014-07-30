package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
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
	private Sound timerSound;
	private float radius;
	protected Animation animation = null;
	protected float animTime;
	private Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
	
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
		radius = 1.f;
		this.timerSound = ((GamePlay) currentScreen).getAssetManager().get("audio/timer/timer1.mp3", Sound.class);
		timerSound.play();
	}

	@Override
	public void explode(float x, float y, float powerX, float powerY, Explosion.Style explosionStyle) {
		/**
		 * stop timer sound
		 */
		timerSound.stop();
		 /**
		  * play explosion sound
		  */
		 Sound explosionSound = ((GamePlay) currentScreen).getAssetManager().get("audio/explosion/explosion1.mp3", Sound.class);
		 explosionSound.play();
		 
		 /**
		  * create explosion
		  */
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
		bombShape.setRadius(radius);
		
		//fixture
		bombFixtureDef.density = 2.5f;
		bombFixtureDef.friction = .25f;
		bombFixtureDef.restitution = .75f;
		
		bombFixtureDef.shape = bombShape;
		

        Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        box2dBody = ((GamePlay)currentScreen).getWorld().createBody(bombDef);
		box2dBody.createFixture(bombFixtureDef);
		//bombBody.setUserData(new MyUserData("bomb", this, null));
		
		//sprite
		sprite = new Sprite(((GamePlay)currentScreen).getAssetManager().get("img/texture/bomb.png", Texture.class));
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		((GamePlay)currentScreen).getGameObjectManager().addGameObject(this);
		
    }

    @Override
	public void update(float delta) {
		this.time -= delta;
		if(time <= 0) this.explode(box2dBody.getPosition().x, box2dBody.getPosition().y, powerX, powerY, explosionStyle);
		animTime += delta;
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		if( sprite != null){
			if (animation != null) {
				sprite.setRegion(animation.getKeyFrame(animTime));
			}
			sprite.setPosition(box2dBody.getPosition().x - sprite.getWidth()/2, box2dBody.getPosition().y - sprite.getHeight()/2);
			sprite.setRotation(box2dBody.getAngle() * MathUtils.radiansToDegrees);
			sprite.draw(batch);
		}
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
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
	
}

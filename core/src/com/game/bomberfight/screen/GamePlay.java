package com.game.bomberfight.screen;


import box2dLight.ConeLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.game.bomberfight.core.CollisionListener;
import com.game.bomberfight.core.InputController;


import com.game.bomberfight.model.GameObject;

import java.util.ArrayList;


public class GamePlay implements Screen {

	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private CollisionListener cl;
	
	public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATIONS = 3;
	

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


	    //world render 
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		
		/**
		 * update and redraw game objects
		 */
		for(int i = 0; i <gameObjects.size(); i++){
			GameObject o = gameObjects.get(i);
			o.update(delta);
			o.draw();
		}

		//debug render
		debugRenderer.render(this.world, camera.combined);
		
		
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width/10;
		camera.viewportHeight = height/10;
		
		

	}

	@Override
	public void show() {
		
		/**********************************************************
		 *                 environment setup                      *
		 **********************************************************/
		/**
		 * box2d world setup
		 */
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();

		
		/**
		 * camera setup
		 */
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		/**
		 * collision listener setup
		 */
		 cl = new CollisionListener();
		 world.setContactListener(cl);
	
		
		  
		  
	    /**********************************************************
	     *                 game objects creation                  *
	     **********************************************************/
		  

	    
	    /**********************************************************
		 *                 input listener                         *
		 **********************************************************/
		Gdx.input.setInputProcessor(new InputController(){
	
		});
	
	    
	  

	}

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void pause() {
		

	}

	@Override
	public void resume() {
		

	}

	@Override
	public void dispose() {
		for(GameObject o : gameObjects)
			o.dispose();
		world.dispose();
		debugRenderer.dispose();

	}

}

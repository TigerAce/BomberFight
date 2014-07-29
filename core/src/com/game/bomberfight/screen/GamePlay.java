package com.game.bomberfight.screen;


import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.bomberfight.InputSource.GamePlayScreenKeyboard;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.BomberFight;
import com.game.bomberfight.core.Brick;
import com.game.bomberfight.core.CollisionListener;
import com.game.bomberfight.core.Crate;
import com.game.bomberfight.core.GameObjectManager;
import com.game.bomberfight.core.Wall;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.utility.Config;
import com.game.bomberfight.utility.FpsDisplayer;


public class GamePlay implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private ExtendViewport viewport;
    private SpriteBatch batch;
    private CollisionListener collisionListener;

    private GameObjectManager gameObjectManager = new GameObjectManager();
    /**
     * TODO: Not sure whether we need to put this Controllable thingy into a manager?
     * For simplicity, I just create a HashSet for it.
     */
    private HashSet<Controllable> controllableObjects = new HashSet<Controllable>();
    
    /**
     * store all explosions
     */
    private ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    private final float TIMESTEP = 1 / 60f;
    private final int VELOCITYITERATIONS = 8;
    private final int POSITIONITERATIONS = 3;

    /**
     * Since world.step only accept a fixed TIMESTEP, a timeAccumulator is used to
     * smooth display in different environment with varies FPS
     */
    private float timeAccumulator = 0.0f;

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        /**
         * render camera
         */
        viewport.update();

        /**
         * Handles world collision calculation
         */
        this.timeAccumulator += delta;
        while (this.timeAccumulator >= this.TIMESTEP) {
            // Only perform collision calculation when timeAccumulator is greater than default TIMESTEP
            world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
            this.timeAccumulator -= this.TIMESTEP;
        }

        
        /**
         * update and redraw game objects
         */
        gameObjectManager.updateAll(delta);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        gameObjectManager.drawAll(batch);
        FpsDisplayer.getInstance().draw(batch, 0, Gdx.graphics.getHeight() /10);
        batch.end();

        /**
         * render explosion
         */
        for(int i = 0; i < explosions.size(); i++){
			if(explosions.get(i) != null && explosions.get(i).isCompleted())
				explosions.remove(i);
		}
    	
        //debug render
    	if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
    		debugRenderer.render(this.world, viewport.getCamera().combined);
		}
    }

    @Override
    public void resize(int width, int height) {
    	viewport.update(width, height, false);
    }

    @Override
    public void show() {
        // Set the current game state to BomberFight.GAME_PLAY_STATE
        ((BomberFight) Gdx.app.getApplicationListener()).setGameState(BomberFight.GAME_PLAY_STATE);

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
        viewport = new ExtendViewport(Config.getInstance().get("viewportWidth", Float.class), Config.getInstance().get("viewportHeight", Float.class));
      

        /**
         * collision listener setup
         */
        collisionListener = new CollisionListener();
        world.setContactListener(collisionListener);


        /**********************************************************
         *                 game objects creation                  *
         **********************************************************/


        /**
         * Create player
         */
        Bomber bomber = new Bomber(0, 1, 10);
        bomber.create();
        this.controllableObjects.add(bomber);

        /**
         * create wall frame
         */
        Wall gameWallFrame = new Wall(0, 0, 100, 70);
        gameWallFrame.setAsRectangleFrame();
        
     
        /**
         * create a brick
         *
         */
        
        Brick brick = new Brick(12, 12, 4, 4, 300);
        brick.create();
    	/**
		 * create bunch of crate
		 */
		  float factor = 0f;
		  float crateSize = 4;
		  float scaleSize = 5;
		  float x = -10;
		  float y = 10;
		  
			for(int w = 0; w < scaleSize; w++){
				for(int h = 0; h < scaleSize; h++){
					Crate c = new Crate(x - ((crateSize * scaleSize)/2) + crateSize/2 + (w * (crateSize + factor)) , y - ((crateSize * scaleSize)/2) + crateSize/2 + (h * (crateSize + factor)), crateSize, crateSize, 100);
					c.create();
				}
			}
		


        /**********************************************************
         *                 input listener                         *
         **********************************************************/
        Gdx.input.setInputProcessor(new GamePlayScreenKeyboard());
        
        batch = new SpriteBatch();
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
        gameObjectManager.disposeAll();
        world.dispose();
        debugRenderer.dispose();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

    public void setGameObjectManager(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
    }

    public HashSet<Controllable> getControllableObjects() {
        return controllableObjects;
    }

    public void setControllableObjects(HashSet<Controllable> controllableObjects) {
        this.controllableObjects = controllableObjects;
    }

	public ArrayList<Explosion> getExplosions() {
		return explosions;
	}

	public void setExplosions(ArrayList<Explosion> explosions) {
		this.explosions = explosions;
	}
}

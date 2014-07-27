package com.game.bomberfight.screen;


import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.bomberfight.InputSource.GamePlayScreenKeyboard;
import com.game.bomberfight.core.Bomb;
import com.game.bomberfight.core.BomberFight;
import com.game.bomberfight.core.CollisionListener;
import com.game.bomberfight.core.GameObjectManager;
import com.game.bomberfight.core.Wall;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.Player;


public class GamePlay implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
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
        gameObjectManager.drawAll();

        /**
         * render explosion
         */
		for(int i = 0; i < explosions.size(); i++){
			if(explosions.get(i) != null && !explosions.get(i).isCompleted())
				explosions.get(i).update(delta);
			else explosions.remove(i);
		}
        /**
         * render camera
         */
    	camera.update();
    	
        //debug render
    	if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
    		debugRenderer.render(this.world, camera.combined);
		}
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 10;
        camera.viewportHeight = height / 10;
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
        camera = new OrthographicCamera(Gdx.graphics.getWidth() /10, Gdx.graphics.getHeight() /10);
      

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
        Player bomber = new Player(0, 1, 30.0f);
        bomber.create();
        this.controllableObjects.add(bomber);

        //create wall frame
        Wall gameWallFrame = new Wall(0, 0, 100, 70);
        gameWallFrame.setAsRectangleFrame();
        
        /**
         * create a bomb
         * 
         */
        Bomb bomb = new Bomb(10, 10, 3, 50, 50, Explosion.Style.ANNULAR);
        bomb.create();
        
        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(5, 5);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = .25f;
        fixtureDef.restitution = .8f;
 
        world.createBody(ballDef).createFixture(fixtureDef);


        /**********************************************************
         *                 input listener                         *
         **********************************************************/
        Gdx.input.setInputProcessor(new GamePlayScreenKeyboard());
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

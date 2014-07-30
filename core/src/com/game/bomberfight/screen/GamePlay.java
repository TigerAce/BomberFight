package com.game.bomberfight.screen;

import java.util.ArrayList;
import java.util.HashSet;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.bomberfight.InputSource.BomberController;
import com.game.bomberfight.InputSource.GamePlayScreenKeyboard;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.BomberFight;
import com.game.bomberfight.core.Brick;
import com.game.bomberfight.core.CollisionListener;
import com.game.bomberfight.core.Crate;
import com.game.bomberfight.core.GameObjectManager;
import com.game.bomberfight.core.Item;
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
	private RayHandler rayHandler;

	private AssetManager assetManager = new AssetManager();
	private GameObjectManager gameObjectManager = new GameObjectManager();
	/**
	 * TODO: Not sure whether we need to put this Controllable thingy into a
	 * manager? For simplicity, I just create a HashSet for it.
	 */
	private HashSet<Controllable> controllableObjects = new HashSet<Controllable>();

	/**
	 * store all explosions
	 */
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	
	/**
	 * item list
	 */
	private ArrayList<Item> itemList = new ArrayList<Item>();

	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATIONS = 3;

	/**
	 * Since world.step only accept a fixed TIMESTEP, a timeAccumulator is used
	 * to smooth display in different environment with varies FPS
	 */
	private float timeAccumulator = 0.0f;

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/**
		 * render camera
		 */
		viewport.getCamera().update();
		batch.setProjectionMatrix(viewport.getCamera().combined);

		/**
		 * Handles world collision calculation
		 */
		this.timeAccumulator += delta;
		while (this.timeAccumulator >= this.TIMESTEP) {
			// Only perform collision calculation when timeAccumulator is
			// greater than default TIMESTEP
			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
			this.timeAccumulator -= this.TIMESTEP;
		}

		/**
		 * update and redraw game objects
		 */
		gameObjectManager.updateAll(delta);
		gameObjectManager.drawAll(batch);

		/**
		 * render explosion
		 */
		for (int i = 0; i < explosions.size(); i++) {
			if (explosions.get(i) != null && explosions.get(i).isCompleted())
				explosions.remove(i);
		}

		/**
		 * render light
		 */
		rayHandler.setCombinedMatrix(viewport.getCamera().combined);
		rayHandler.updateAndRender();

		// debug render
		if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
			debugRenderer.render(this.world, viewport.getCamera().combined);
		}
		
		FpsDisplayer.getInstance().draw(batch, 0, 0);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
		FpsDisplayer.getInstance().update(width, height);
	}

	@Override
	public void show() {
		// Set the current game state to BomberFight.GAME_PLAY_STATE
		((BomberFight) Gdx.app.getApplicationListener())
				.setGameState(BomberFight.GAME_PLAY_STATE);

		/**********************************************************
		 * environment setup *
		 **********************************************************/
		/**
		 * box2d world setup
		 */
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();

		/**
		 * camera setup
		 */
		viewport = new ExtendViewport(Config.getInstance().get("viewportWidth",
				Float.class), Config.getInstance().get("viewportHeight",
				Float.class));

		/**
		 * collision listener setup
		 */
		collisionListener = new CollisionListener();
		world.setContactListener(collisionListener);

		/**
		 * load resources
		 */
		TextureParameter textureParameter = new TextureParameter();
		textureParameter.minFilter = TextureFilter.Linear;
		textureParameter.magFilter = TextureFilter.Linear;
		assetManager.load("img/texture/crate4.jpg", Texture.class, textureParameter);
		assetManager.load("img/texture/brick3.jpg", Texture.class, textureParameter);
		assetManager.load("img/animation/soldier.png", Texture.class, textureParameter);
		assetManager.load("particle/flame.p", ParticleEffect.class);
		assetManager.load("img/texture/bomb.png", Texture.class, textureParameter);
		while (!assetManager.update()) {
			Gdx.app.log("Loading progress", ""+assetManager.getProgress()+"%");
		}

		/**********************************************************
		 * game objects creation *
		 **********************************************************/

		/**
		 * Create a bomber
		 */

		Bomber bomber = new Bomber(-45, -30, 4, 4,10, 100, 2, 3);

		bomber.create();
		bomber.setAnimation(assetManager.get("img/animation/soldier.png", Texture.class), 3, 1);
		this.controllableObjects.add(bomber);
		bomber.setBomberController(new BomberController(true));
		
		Bomber bomber1 = new Bomber(45, 30, 4, 4,10, 100, 2, 3);
		bomber1.create();
		bomber1.setAnimation(assetManager.get("img/animation/soldier.png", Texture.class), 3, 1);
		this.controllableObjects.add(bomber1);
		bomber1.setBomberController(new BomberController(false));

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

		for (int w = 0; w < scaleSize; w++) {
			for (int h = 0; h < scaleSize; h++) {
				Crate c = new Crate(x - ((crateSize * scaleSize) / 2)
						+ crateSize / 2 + (w * (crateSize + factor)), y
						- ((crateSize * scaleSize) / 2) + crateSize / 2
						+ (h * (crateSize + factor)), crateSize, crateSize, 100);
				c.create();
			}
		}
		/**
		 * create items 
		 */
		//change explosion style to annular
		Item item = new Item();
		item.getAttr().setExplosionStyle(Explosion.Style.ANNULAR);
		itemList.add(item);
		
		//add 1 to number of bomb can be placed in one round
		item = new Item();
		item.getAttr().setNumBombPerRound(1);
		itemList.add(item);
		
		//add blast power
		item = new Item();
		item.getAttr().setPowerX(500f);
		itemList.add(item);
		

		/**********************************************************
		 * lights setup *
		 **********************************************************/

		rayHandler = new RayHandler(world);
		new ConeLight(rayHandler, 1000, new Color(1f, 0.1f, 0.1f, 1f), 70,
				-49.9f, -34.9f, 45, 45);
		
		new ConeLight(rayHandler, 1000, new Color(0.1f, 0.5f, 1f, 1f), 70,
				49.9f, 34.9f, 225, 45);
		
		PointLight p = new PointLight(rayHandler, 1000, new Color(0.1f, 0.5f,
				0.5f, 1f), 50, 0, 0);
		
		p.attachToBody(bomber.getBox2dBody(), 0, 0);
		
		rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.1f);
		
		Light.setContactFilter((short)1, (short)-1, (short)1);

		/**********************************************************
		 * input listener *
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

		rayHandler.dispose();
		assetManager.dispose();
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

	/**
	 * @return the assetManager
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * @param assetManager the assetManager to set
	 */
	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}
}
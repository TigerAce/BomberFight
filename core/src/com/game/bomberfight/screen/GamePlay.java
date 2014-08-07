package com.game.bomberfight.screen;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.bomberfight.InputSource.BomberController;
import com.game.bomberfight.InputSource.GamePlayScreenKeyboard;
import com.game.bomberfight.InputSource.RemoteController;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.CollisionListener;
import com.game.bomberfight.core.GameObjectManager;
import com.game.bomberfight.core.Gui;
import com.game.bomberfight.core.Item;
import com.game.bomberfight.core.TileMapManager;
import com.game.bomberfight.core.TileMapManager.PlayerSpawnPoint;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.system.TileMapEffectSystem;
import com.game.bomberfight.utility.Config;
import com.game.bomberfight.utility.FpsDisplayer;

public class GamePlay implements Screen {

	protected World world;
	protected Box2DDebugRenderer debugRenderer;
	protected ExtendViewport viewport;
	protected SpriteBatch batch;
	protected CollisionListener collisionListener;
	protected RayHandler rayHandler;

	protected AssetManager assetManager = new AssetManager();
	protected GameObjectManager gameObjectManager = new GameObjectManager();

	/**
	 * store all explosions
	 */
	protected ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	
	/**
	 * item list
	 */
	protected ArrayList<Item> itemList = new ArrayList<Item>();

	protected final float TIMESTEP = 1 / 60f;
	protected final int VELOCITYITERATIONS = 3;
	protected final int POSITIONITERATIONS = 2;

	/**
	 * Since world.step only accept a fixed TIMESTEP, a timeAccumulator is used
	 * to smooth display in different environment with varies FPS
	 */
	protected float timeAccumulator = 0.0f;
	
	/*
	 * tileMapManager load tile map first and create objects(include players) on the map
	 * then update and render it
	 */
	protected TileMapManager tileMapManager;
	
	protected long timeNow = 0;
	
	protected boolean isEfficiencyTest = false;
	
	protected Gui gui;
	
	protected InputMultiplexer inputMultiplexer;
	
	protected GameInfo gameInfo;
	
	protected TileMapEffectSystem tileMapEffectSystem;
	
	protected Array<Player> playerList = new Array<Player>();;

	protected ArrayList<LinkedHashMap> keyMaps = new ArrayList<LinkedHashMap>();
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/**
		 * render camera
		 */
		timeNow = System.currentTimeMillis();
		
		viewport.getCamera().update();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "camera update + batch setProjectionMatrix: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}
		
		tileMapManager.renderBackground();
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "tileMapManager renderBackground: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}

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
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "accumulate and world step: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}

		/**
		 * update and redraw game objects
		 */
		gameObjectManager.updateAll(delta);
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "gameObjectManager updateAll: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}
		
		gameObjectManager.drawAll(batch);
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "gameObjectManager drawAll: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}
		
		tileMapManager.renderForeground();
		tileMapEffectSystem.update(delta);
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "tileMapManager renderForeground: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}

		/**
		 * render explosion
		 */
		for (int i = 0; i < explosions.size(); i++) {
			if (explosions.get(i) != null && explosions.get(i).isCompleted())
				explosions.remove(i);
		}
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "render explosion: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}

		/**
		 * render light
		 */
		rayHandler.setCombinedMatrix(viewport.getCamera().combined);
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "rayHandler setCombinedMatrix: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}
		
		//if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
			rayHandler.updateAndRender();
		//}
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "rayHandler updateAndRender: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}

		// debug render
		if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
			debugRenderer.render(this.world, viewport.getCamera().combined);
		}
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "debugRenderer render: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}
		
		gui.update();
		gui.draw();
		
		FpsDisplayer.getInstance().draw(batch, 0, 0);
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "FpsDisplayer draw: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
			Gdx.app.log("Efficiency test", "\n\n");
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
		FpsDisplayer.getInstance().update(width, height);
		tileMapManager.update(viewport.getCamera().combined, viewport.getMinWorldWidth(), viewport.getMinWorldHeight());
		gui.resize(viewport);
	}

	@Override
	public void show() {
		// Set the current game state to BomberFight.GAME_PLAY_STATE
	//	((BomberFight) Gdx.app.getApplicationListener())
	//			.setGameState(BomberFight.SINGLE_GAME_PLAY_STATE);

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

		// create ray handler
		rayHandler = new RayHandler(world);
		Light.setContactFilter((short)1, (short)-1, (short)1);
		

		/**********************************************************
		 * input listener *
		 **********************************************************/
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new GamePlayScreenKeyboard(this));
		Gdx.input.setInputProcessor(inputMultiplexer);

		// create spritebatch
		batch = new SpriteBatch();
		
		//create tile map manager
		tileMapManager = new TileMapManager(this);
		
		// create items
		// change explosion style to annular
		Item item = new Item();
		item.setName("ANNULAR");
		item.setAffectTime(10);
		item.getAttr().setCurrStyle(Explosion.Style.ANNULAR);
		this.itemList.add(item);
		// add 1 to number of bomb can be placed in one round
		item = new Item();
		item.setName("ADDBOMB");
		item.getAttr().setNumBombPerRound(1);
		this.itemList.add(item);
		// add blast power
		item = new Item();
		item.setName("POWER_UP");
		item.getAttr().setPowerX(10f);
		item.getAttr().setPowerY(10f);
		this.itemList.add(item);
		
		gui = new Gui(this);
		
		for (int i = 0; i < tileMapManager.getPlayerSpawnPointList().size; i++) {
			PlayerSpawnPoint spawnPoint = tileMapManager.getPlayerSpawnPointList().get(i);
			Bomber bomber = new Bomber(spawnPoint.x, spawnPoint.y, spawnPoint.width, spawnPoint.height, 
					spawnPoint.speed, spawnPoint.hitPoint, spawnPoint.numBombPerRound, spawnPoint.roundInterval);
			bomber.setAnimation(assetManager.get("img/animation/soldier1.png",  Texture.class), 3, 1);
			bomber.create();
		
			playerList.add(bomber);
			// Attach a point light to player
			PointLight pl = new PointLight(getRayHandler(), 1000, new Color(0.1f, 0.5f,
					0.5f, 1f), 50, 0, 0);
			pl.attachToBody(bomber.getBox2dBody(), 0, 0);
			
			
			if(gameInfo.networkMode.equals("WAN")){
				
				keyMaps.add(i, new LinkedHashMap());
				gui.setHUD(bomber);
				bomber.setController(new RemoteController(new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D, Input.Keys.SPACE},keyMaps.get(i)));
			}else{
				if (i == 0) {
					gui.setFixedStatusBar(bomber);
					BomberController bomberController = new BomberController(new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D, Input.Keys.SPACE});
					inputMultiplexer.addProcessor(bomberController);
					bomber.setController(bomberController);
				} else {
					gui.setHUD(bomber);
					BomberController bomberController = new BomberController(new int[]{Input.Keys.UP, Input.Keys.LEFT, Input.Keys.DOWN, Input.Keys.RIGHT, Input.Keys.CONTROL_RIGHT});
					inputMultiplexer.addProcessor(bomberController);
					bomber.setController(bomberController);
				}
			}
		}
		
		tileMapEffectSystem = new TileMapEffectSystem(tileMapManager, playerList);
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
		tileMapManager.dispose();
		
		gui.dispose();
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

	/**
	 * @return the rayHandler
	 */
	public RayHandler getRayHandler() {
		return rayHandler;
	}

	/**
	 * @return the gui
	 */
	public Gui getGui() {
		return gui;
	}

	/**
	 * @return the gameInfo
	 */
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	/**
	 * @param gameInfo the gameInfo to set
	 */
	public void setGameInfo(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}
}
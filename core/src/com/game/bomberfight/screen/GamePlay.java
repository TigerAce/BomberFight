package com.game.bomberfight.screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.game.bomberfight.InputSource.BomberController;
import com.game.bomberfight.InputSource.GamePlayScreenKeyboard;
import com.game.bomberfight.InputSource.RemoteController;
import com.game.bomberfight.core.Bomb;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.CollisionListener;
import com.game.bomberfight.core.GameObjectManager;
import com.game.bomberfight.core.Gui;
import com.game.bomberfight.core.Item;
import com.game.bomberfight.core.Particle;
import com.game.bomberfight.core.TileMapManager;
import com.game.bomberfight.core.TileMapManager.PlayerSpawnPoint;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.GameObject;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.model.PlayerInfo;
import com.game.bomberfight.net.BomberFightClient;
import com.game.bomberfight.net.Network;
import com.game.bomberfight.net.Network.RequireJoinGame;
import com.game.bomberfight.net.Network.RequireUpdateBombPositionToOthers;
import com.game.bomberfight.net.Network.RequireUpdatePositionToOthers;
import com.game.bomberfight.net.Network.RespondJoinGame;
import com.game.bomberfight.net.Network.SignalBarrierDestroyed;
import com.game.bomberfight.net.Network.StartGame;
import com.game.bomberfight.net.Network.UpdateBombPosition;
import com.game.bomberfight.net.Network.UpdateDropItem;
import com.game.bomberfight.net.Network.UpdateHealth;
import com.game.bomberfight.net.Network.UpdateInput;
import com.game.bomberfight.net.Network.UpdatePosition;
import com.game.bomberfight.system.CameraSystem;
import com.game.bomberfight.system.TileMapEffectSystem;
import com.game.bomberfight.utility.Config;
import com.game.bomberfight.utility.FpsDisplayer;
import com.game.bomberfight.utility.UserData;

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
	
	/**
	 * remote process queue
	 */
	protected ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<Object>();

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
	
	public static GameInfo gameInfo = new GameInfo();
	
	protected TileMapEffectSystem tileMapEffectSystem;
	
	protected Array<Player> playerList = new Array<Player>();
	
	public static final BomberFightClient client = new BomberFightClient();
	
	protected Map<Integer, Player> connToPlayerMap = new HashMap<Integer, Player>();
	
	protected Map<Integer, Map<Integer, Boolean>> connToInputSourceMap = new HashMap<Integer, Map<Integer,Boolean>>();
	
	protected float positionUpdateTime = 1.f;
	
	protected Vector2 lastPosition = new Vector2();
	
	protected CameraSystem cameraSystem = null;
	
	protected Array<String> serverList = new Array<String>();
	
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
		tileMapManager.update(viewport.getCamera().combined, viewport.getMinWorldWidth(), viewport.getMinWorldHeight());
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
		processingRemote();
		removeBodies();
		
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
		//rayHandler.setCombinedMatrix(viewport.getCamera().combined);
		
		if (isEfficiencyTest) {
			Gdx.app.log("Efficiency test", "rayHandler setCombinedMatrix: " + (System.currentTimeMillis() - timeNow) + "ms");
			timeNow = System.currentTimeMillis();
		}
		
		//if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
		//	rayHandler.updateAndRender();
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
		
		if (cameraSystem != null) {
			cameraSystem.update(delta);
		}
		
		updatePositionToOthers(delta);
		//updateBombPositionToOthers(delta);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
		FpsDisplayer.getInstance().update(width, height);
		//tileMapManager.update(viewport.getCamera().combined, viewport.getMinWorldWidth(), viewport.getMinWorldHeight());
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
		item.getAttr().setPowerX(5f);
		item.getAttr().setPowerY(5f);
		this.itemList.add(item);
		// add heal
		item = new Item();
		item.setName("HEAL");
		item.getAttr().setMaxLife(100);
		item.getAttr().setCurrLife(100);
		this.itemList.add(item);
		
		gui = new Gui();
		
		createPlayer();
		tileMapEffectSystem = new TileMapEffectSystem(tileMapManager, playerList);
		
		if (GamePlay.gameInfo.networkMode.equalsIgnoreCase("WAN")) {
			connect("data/server.xml");//yijiasup.no-ip.org 128.199.207.133
		}
		
		Particle.bombEffectPool = new ParticleEffectPool(getAssetManager().get("particle/flame.p", ParticleEffect.class), 100, 1000);
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
	
	public void removeBodies(){
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for(Body b : bodies){
			UserData data = (UserData) b.getUserData();
			if(data != null && data.isDead == true){
				world.destroyBody(b);
				data = null;
			}
		}
	}
	
	public void processingRemote(){
		while(!queue.isEmpty()){
			
			Object object = queue.remove();
			
		if (object instanceof RespondJoinGame) {
			RespondJoinGame respondJoinGame = (RespondJoinGame) object;
			GamePlay.gameInfo = respondJoinGame.gameInfo;
			gui.showWaitingDialog(true);
			Gdx.app.log("received RespondJoinGame", respondJoinGame.result);
			Gdx.app.log("received RespondJoinGame GameInfo", "room number "+respondJoinGame.gameInfo.playerInfo.roomNumber+"spawn point "+respondJoinGame.gameInfo.playerInfo.spawnPoint);
		}
		
		if (object instanceof StartGame) {
			gui.showWaitingDialog(false);
			StartGame startGame = (StartGame) object;
			setupInputSource(startGame.playerInfoList);
		}
		
		if (object instanceof UpdateInput) {
			UpdateInput updateInput = (UpdateInput) object;
			Player player = (Player) connToPlayerMap.get(updateInput.conn);
			if (player.getAttr().getCurrLife() <= 0) {
				return;
			}
			connToInputSourceMap.get(updateInput.conn).put(updateInput.keyCode, updateInput.keyState);
		}
		
		if (object instanceof UpdatePosition) {
			UpdatePosition updatePosition = (UpdatePosition) object;
			Player player = (Player) connToPlayerMap.get(updatePosition.conn);
			if (player.getAttr().getCurrLife() <= 0) {
				return;
			}
			Body body = connToPlayerMap.get(updatePosition.conn).getBox2dBody();
			Vector2 pos = body.getPosition();
			float angle = body.getAngle();
			if (pos.x != updatePosition.x || pos.y != updatePosition.y) {
				if (!world.isLocked()) {
					pos.set(updatePosition.x, updatePosition.y);
					connToPlayerMap.get(updatePosition.conn).getBox2dBody().setTransform(pos, angle);
				}
			}
		}
		
		if(object instanceof UpdateBombPosition){
			UpdateBombPosition updateBombPosition = (UpdateBombPosition)object;
			Bomber bomber = (Bomber) connToPlayerMap.get(updateBombPosition.conn);
			if(updateBombPosition.bombIndex < bomber.getActivatedBombList().size()){
				Bomb bomb = bomber.getActivatedBombList().get(updateBombPosition.bombIndex);
				Body bombBody = bomb.getBox2dBody();
					Vector2 pos = bombBody.getPosition();
				if(pos.x != updateBombPosition.x || pos.y != updateBombPosition.y){
					if(!world.isLocked()){
						pos.set(updateBombPosition.x, updateBombPosition.y);
						bombBody.setTransform(pos, updateBombPosition.angle);
						bombBody.setAngularVelocity(updateBombPosition.angularVelocity);
						bombBody.setLinearVelocity(updateBombPosition.linearVelocityX, updateBombPosition.linearVelocityY);
					}
				}
			}
			
		}
		
		if(object instanceof UpdateHealth){
			UpdateHealth updateHealth = (UpdateHealth)object;
			Player player = connToPlayerMap.get(updateHealth.conn);
			if(player != null){
				player.getAttr().setCurrLife(updateHealth.health);
			}
		}
		
		if (object instanceof UpdateDropItem) {
			UpdateDropItem updateDropItem = (UpdateDropItem) object;
			
			boolean create = true;
			
			//id >= 0 indicates the item is generated by destroyed crate or brick, < 0 indicates it form spawn point
			//if it is from crate or brick, need to check crate or brick is removed.
			if(updateDropItem.id >= 0){
				GameObject gameObject = gameObjectManager.findGameObject(updateDropItem.id);
				if (gameObject != null) {
					gameObject.dispose();
				}
			}
			
			//if it is spawn point item, check if spawn point still have item, if so, dont create this item.
			if(updateDropItem.id < 0){
				ItemExistCallback callback = new ItemExistCallback();
				world.QueryAABB(callback, updateDropItem.x, updateDropItem.y, updateDropItem.x, updateDropItem.y);
				create = callback.create;
			}
			
			if(create){
			Item item = null;
			for(Item i : itemList){
				if(i.getName().equals(updateDropItem.name)){
					item = new Item(i);
					break;
				}
			}
			if (item != null) {
				if(updateDropItem.name.equals("POWER_UP")){
					item.setSprite(assetManager.get("img/texture/item1.png", Texture.class));
				}
				if(updateDropItem.name.equals("ANNULAR")){
					item.setSprite(assetManager.get("img/texture/item2.png", Texture.class));
				}
				if(updateDropItem.name.equals("ADDBOMB")){
					item.setSprite(assetManager.get("img/texture/item3.png", Texture.class));
				}
				if(updateDropItem.name.equals("HEAL")){
					item.setSprite(assetManager.get("img/texture/item6.jpg", Texture.class));
				}
				item.setX(updateDropItem.x);
				item.setY(updateDropItem.y);
			
				item.create();
		
			}
			}
		}
		
		if(object instanceof SignalBarrierDestroyed){
			SignalBarrierDestroyed signal = (SignalBarrierDestroyed) object;
			GameObject gameObject = gameObjectManager.findGameObject(signal.id);
			if (gameObject != null) {
				gameObject.dispose();
			}
		}
		}
	}


	public void createPlayer() {
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
			
			bomber.p = pl;
			
			if (gameInfo.networkMode.equals("WAN")) {
				gui.setHUD(bomber);
			} else {
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
	}
	
	public void setupInputSource(PlayerInfo[] playerInfoList) {
		for (int i = 0; i < playerList.size; i++) {
			Bomber bomber = (Bomber) playerList.get(i);
			bomber.setName(playerInfoList[i].name);
			
			connToPlayerMap.put(playerInfoList[i].conn, bomber);
			
			if (gameInfo.playerInfo.spawnPoint == i) {
				BomberController bomberController = new BomberController(new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D, Input.Keys.SPACE});
				inputMultiplexer.addProcessor(bomberController);
				bomber.setController(bomberController);
				cameraSystem = new CameraSystem(viewport.getCamera(), bomber, 
						tileMapManager.getMapWidth(), tileMapManager.getMapHeight(), 
						viewport.getMinWorldWidth(), viewport.getMinWorldHeight());
			} else {
				Map<Integer, Boolean> inputSource = new LinkedHashMap<Integer, Boolean>();
				connToInputSourceMap.put(playerInfoList[i].conn, inputSource);
				RemoteController remoteController = new RemoteController(
						new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D, Input.Keys.SPACE}, inputSource);
				bomber.setController(remoteController);
			}
		}
	}
	
	public class ClientListener extends Listener {
		public void connected (Connection connection) {
			RequireJoinGame requireJoinGame = new RequireJoinGame();
			requireJoinGame.gameInfo = GamePlay.gameInfo;
			requireJoinGame.gameInfo.playerInfo.conn = connection.getID();
			client.sendTCP(requireJoinGame);
		}

		public void received (Connection connection, Object object) {
			queue.add(object);
		}

		public void disconnected (Connection connection) {
			client.removeListener(this);
			gui.showDisconnectDialog();
		}
	}
	
	public void connect(String host) {
		Network.register(client);
		
		client.addListener(new ClientListener());
		
		client.start();
		
		loadServerList(host);
		
		for (int i = 0; i < serverList.size; i++) {
			try {
				client.connect(5000, serverList.get(i), Network.portTCP, Network.portUDP);
				break;
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * @return the connToPlayerMap
	 */
	public Map<Integer, Player> getConnToPlayerMap() {
		return connToPlayerMap;
	}

	/**
	 * @return the connToInputSourceMap
	 */
	public Map<Integer, Map<Integer, Boolean>> getConnToInputSourceMap() {
		return connToInputSourceMap;
	}
	
	public void updatePositionToOthers (float delta) {
		if (!connToPlayerMap.isEmpty()) {
			positionUpdateTime -= delta;
			if (positionUpdateTime <= 0) {
				if(connToPlayerMap.get(gameInfo.playerInfo.conn) != null){
					Vector2 position = connToPlayerMap.get(gameInfo.playerInfo.conn).getBox2dBody().getPosition();
					if (lastPosition.x != position.x || lastPosition.y != position.y) {
						RequireUpdatePositionToOthers requireUpdatePositionToOthers = new RequireUpdatePositionToOthers();
						requireUpdatePositionToOthers.x = position.x;
						requireUpdatePositionToOthers.y = position.y;
						client.sendTCP(requireUpdatePositionToOthers);
						lastPosition.set(position);
					}
					positionUpdateTime = 1.f;
				}
			}
		}
	}
	
	public void updatePositionToOthers () {
		if (!connToPlayerMap.isEmpty()) {
			if(connToPlayerMap.get(gameInfo.playerInfo.conn) != null){
				Vector2 position = connToPlayerMap.get(gameInfo.playerInfo.conn).getBox2dBody().getPosition();
				if (lastPosition.x != position.x || lastPosition.y != position.y) {
					RequireUpdatePositionToOthers requireUpdatePositionToOthers = new RequireUpdatePositionToOthers();
					requireUpdatePositionToOthers.x = position.x;
					requireUpdatePositionToOthers.y = position.y;
					client.sendTCP(requireUpdatePositionToOthers);
					lastPosition.set(position);
				}
			}
		}
	}
	
	public void updateBombPositionToOthers (float delta) {
		if (!connToPlayerMap.isEmpty()) {
			positionUpdateTime -= delta;
			if (positionUpdateTime <= 0) {
				Bomber bomber = (Bomber) connToPlayerMap.get(gameInfo.playerInfo.conn);
				if(bomber != null){
				for(Bomb b : bomber.getActivatedBombList()){
					Vector2 position = b.getBox2dBody().getPosition();
					RequireUpdateBombPositionToOthers requireUpdateBombPositionToOthers = new RequireUpdateBombPositionToOthers();
					requireUpdateBombPositionToOthers.x = position.x;
					requireUpdateBombPositionToOthers.y = position.y;
					requireUpdateBombPositionToOthers.bombIndex = bomber.getActivatedBombList().indexOf(b);
					client.sendTCP(requireUpdateBombPositionToOthers);
				}
				positionUpdateTime = 0.5f;
				}
			}
		}
	}
	
	public void updateBombPositionToOthers(Bomb bomb) {
		Bomber bomber = (Bomber) connToPlayerMap.get(gameInfo.playerInfo.conn);
		if (bomber != null) {
			if (bomber.getActivatedBombList().contains(bomb)) {
				Vector2 position = bomb.getBox2dBody().getPosition();
				RequireUpdateBombPositionToOthers requireUpdateBombPositionToOthers = new RequireUpdateBombPositionToOthers();
				requireUpdateBombPositionToOthers.x = position.x;
				requireUpdateBombPositionToOthers.y = position.y;
				requireUpdateBombPositionToOthers.angle = bomb.getBox2dBody().getAngle();
				requireUpdateBombPositionToOthers.angularVelocity = bomb.getBox2dBody().getAngularVelocity();
				requireUpdateBombPositionToOthers.linearVelocityX = bomb.getBox2dBody().getLinearVelocity().x;
				requireUpdateBombPositionToOthers.linearVelocityY = bomb.getBox2dBody().getLinearVelocity().y;
				requireUpdateBombPositionToOthers.bombIndex = bomber.getActivatedBombList().indexOf(bomb);
				client.sendTCP(requireUpdateBombPositionToOthers);
			}
		}
	}

	/**
	 * @return the cameraSystem
	 */
	public CameraSystem getCameraSystem() {
		return cameraSystem;
	}
	
	public class ItemExistCallback implements QueryCallback{
		public boolean create = true;
		@Override
		public boolean reportFixture(Fixture fixture) {
			if(fixture != null){
				Body b = fixture.getBody();
				if(b.getUserData() != null){
					UserData data = (UserData) b.getUserData();
					if(data.object instanceof Item){
						create = false;
					}
				}
			}			
			return false;
		}
	
	}

	/**
	 * @return the playerList
	 */
	public Array<Player> getPlayerList() {
		return playerList;
	}
	
	public void loadServerList(String filename) {
		XmlReader reader = new XmlReader();
		Element root = null;
		try {
			root = reader.parse(Gdx.files.internal(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			String address = root.getChild(i).getText();
			serverList.add(address);
		}
	}
}


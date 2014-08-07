package com.game.bomberfight.core;

import java.util.Iterator;

import box2dLight.ConeLight;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.bomberfight.InputSource.BomberController;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;
import com.game.bomberfight.screen.MultiplayerGamePlay;
import com.game.bomberfight.utility.Config;

public class TileMapManager {
	
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private Matrix4 mat = new Matrix4();
	private int[] backgroundLayers = { 0, 1 };
	private int[] foregroundLayers = { 2 };
	private float unitScale = 1.f / 8.f;
	private boolean isObjectLoaded = false;
	private GamePlay gamePlay;
	private Player playerA;
	private Player playerB;

	public TileMapManager(GamePlay gamePlay) {
		// TODO Auto-generated constructor stub
		// Initialize tile map and tiledMapRenderer and set the unitscale
		this.gamePlay = gamePlay;
		this.tiledMap = this.gamePlay.getAssetManager().get(gamePlay.getGameInfo().gameMap);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(this.tiledMap, unitScale);
		if (!isObjectLoaded) {
			loadObject(Config.getInstance().get("viewportWidth", Float.class), 
					Config.getInstance().get("viewportHeight",Float.class));
			isObjectLoaded = true;
		}
	}
	
	public void loadObject(float viewportWidth, float viewportHeight) {
		// All properties related to the whole game world is stored in the properties of image_layer_1
		// Such as the width and height of game world
		TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("image_layer_1");
		MapProperties mapProperties = tiledMapTileLayer.getProperties();
		
		// create wall frame
		Wall gameWallFrame = new Wall(0, 0, Float.parseFloat((String) mapProperties.get("world_width")), Float.parseFloat((String) mapProperties.get("world_height")));
		gameWallFrame.setAsRectangleFrame();
		

		// create all objects
		MapLayer objectLayer = tiledMap.getLayers().get("object_layer_1");
		MapObjects mapObjects = objectLayer.getObjects();
		Iterator<MapObject> iter = mapObjects.iterator();
		Matrix4 trans = new Matrix4();
		trans.setTranslation(-viewportWidth / 2.f, -viewportHeight / 2.f, 0);
		
		while (iter.hasNext()) {
			MapObject mapObject = iter.next();

			// Usually light is represented by a poly line on the map 
			// The start point of poly line will be the position of that light
			// ConeLight should provide properties: rays, color, distance, directiondegree, conedegree
			// AmbientLight should provide properties: color
			if (mapObject instanceof PolylineMapObject) {
				PolylineMapObject polylineMapObject = (PolylineMapObject) mapObject;
				Vector3 position = new Vector3(polylineMapObject.getPolyline().getX(), polylineMapObject.getPolyline().getY(), 0);
				position.scl(unitScale);
				position.mul(trans);
				
				// create light
				if (polylineMapObject.getName().equalsIgnoreCase("light")) {
					MapProperties objectProperties = polylineMapObject.getProperties();
					String lightType = (String) objectProperties.get("type");
					// Create ConeLight
					if (lightType.equalsIgnoreCase("ConeLight")) {
						int lightRays = Integer.parseInt((String) objectProperties.get("rays"));
						Color color = new com.game.bomberfight.utility.Color((String) objectProperties.get("color"));
						float distance = Float.parseFloat((String) objectProperties.get("distance"));
						float directionDegree = Float.parseFloat((String) objectProperties.get("directiondegree"));
						float coneDegree = Float.parseFloat((String) objectProperties.get("conedegree"));
						new ConeLight(gamePlay.getRayHandler(), lightRays, color, distance,
								position.x, position.y, directionDegree, coneDegree);
					}
					
					// Create AmbientLight
					if (lightType.equalsIgnoreCase("AmbientLight")) {
						Color color = new com.game.bomberfight.utility.Color((String) objectProperties.get("color"));
						gamePlay.getRayHandler().setAmbientLight(color);
					}
				}
			}
			
			// Create brick or crate
			// Usually brick and crate game object is represented as rectangle map object
			// The common property of a rectangle map object is hitpoint
			// Should make sure that hitpoint is a property in your rectangle map object in tiled editor
			if (mapObject instanceof RectangleMapObject) {
				RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
				// Calculate object position and stored in vec3
				Vector2 center = new Vector2();
				rectangleMapObject.getRectangle().getCenter(center);
				Vector3 vec3 = new Vector3(center.x, center.y, 0);
				vec3.scl(unitScale);
				vec3.mul(trans);
				
				// Compute width and height of object
				Float width = rectangleMapObject.getRectangle().getWidth() * unitScale - 0.1f;
				Float height = rectangleMapObject.getRectangle().getHeight() * unitScale - 0.1f;
				
				// Fetch properties of object
				MapProperties objectProperties = rectangleMapObject.getProperties();
				float hitpoint = Float.parseFloat((String) objectProperties.get("hitpoint"));
				
				//create a crate
				if (rectangleMapObject.getName().equalsIgnoreCase("crate")) {
					Crate c = new Crate(vec3.x, vec3.y, width, height, hitpoint);
					c.create();
				}
				
				//create a brick
				if (rectangleMapObject.getName().equalsIgnoreCase("brick")) {
					Brick b = new Brick(vec3.x, vec3.y, width, height, hitpoint);
					b.create();
				}
				
				//create bomber a
				if (rectangleMapObject.getName().equalsIgnoreCase("player")) {
					// Speed, numBombPerRound, roundInterval are properties specially for player
					float speed = Float.parseFloat((String) objectProperties.get("speed"));
					int numBombPerRound = Integer.parseInt((String) objectProperties.get("numBombPerRound"));
					float roundInterval = Float.parseFloat((String) objectProperties.get("roundInterval"));
					
					Bomber bomber = new Bomber(vec3.x, vec3.y, width, height, speed, hitpoint, numBombPerRound, roundInterval);
					bomber.create();
					bomber.setAnimation(gamePlay.getAssetManager().get("img/animation/soldier1.png", Texture.class), 3, 1);
					
					// Set the controller according to the controller property
					String controller = (String) objectProperties.get("controller");
					
					
					
					
					
					if(((BomberFight) Gdx.app.getApplicationListener()).getGameState() == BomberFight.MULTIPLAYER_GAME_PLAY_STATE){
						
							if (controller.equalsIgnoreCase("wasdspace")) {
							
								playerA = bomber;
								playerA.setRemoteControl(true);
								bomber.setName("playerA");
							} else {
								
								//bomber.setController(new BomberController(false));
								playerB = bomber;
								playerB.setRemoteControl(true);
								bomber.setName("playerB");
							}
						
				
					}else{
						
						if (controller.equalsIgnoreCase("wasdspace")) {
							bomber.setController(new BomberController(true));
							//bomber.setController(new AndroidController());
							playerA = bomber;
							bomber.setName("playerA");
						} else {
							
							bomber.setController(new BomberController(false));
							playerB = bomber;
							playerB.setRemoteControl(true);
							bomber.setName("playerB");
						}
					}
					
					
					// Attach a point light to player
					PointLight pl = new PointLight(gamePlay.getRayHandler(), 1000, new Color(0.1f, 0.5f,
							0.5f, 1f), 50, 0, 0);
					pl.attachToBody(bomber.getBox2dBody(), 0, 0);
				}
			}
			
		}
	}
	
	public void update(Matrix4 projectionMatrix, float viewportWidth, float viewportHeight) {
		mat = projectionMatrix.cpy();
		mat.translate(-viewportWidth / 2.f, -viewportHeight / 2.f, 0);
		tiledMapRenderer.setView(mat, 0, 0, viewportWidth, viewportHeight);
	}
	
	public void renderBackground() {
		tiledMapRenderer.render(backgroundLayers);
	}
	
	public void renderForeground() {
		tiledMapRenderer.render(foregroundLayers);
	}
	
	public void dispose() {
		tiledMapRenderer.dispose();
		tiledMap.dispose();
	}

	/**
	 * @return the playerA
	 */
	public Player getPlayerA() {
		return playerA;
	}

	/**
	 * @return the playerB
	 */
	public Player getPlayerB() {
		return playerB;
	}

	/**
	 * @return the unitScale
	 */
	public float getUnitScale() {
		return unitScale;
	}

	/**
	 * @return the mat
	 */
	public Matrix4 getMat() {
		return mat;
	}

	/**
	 * @return the tiledMap
	 */
	public TiledMap getTiledMap() {
		return tiledMap;
	}

}

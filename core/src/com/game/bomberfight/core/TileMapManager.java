package com.game.bomberfight.core;

import java.util.Iterator;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.bomberfight.InputSource.BomberController;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.screen.GamePlay;

public class TileMapManager {
	
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private Matrix4 mat = new Matrix4();
	private int[] backgroundLayers = { 0, 1 };
	private int[] foregroundLayers = { 2 };
	private float unitScale = 1.f / 8.f;
	private boolean isObjectLoaded = false;
	private GamePlay gamePlay;

	public TileMapManager(GamePlay gamePlay) {
		// TODO Auto-generated constructor stub
		// Initialize tile map and tiledMapRenderer and set the unitscale
		this.gamePlay = gamePlay;
		this.tiledMap = this.gamePlay.getAssetManager().get("img/tmx/ground2.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(this.tiledMap, unitScale);
	}
	
	public void loadObject(float viewportWidth, float viewportHeight) {
		// create wall frame
		Wall gameWallFrame = new Wall(0, 0, 100, 70);
		gameWallFrame.setAsRectangleFrame();
		
		//create items
		//change explosion style to annular
		Item item = new Item();
		item.getAttr().setExplosionStyle(Explosion.Style.ANNULAR);
		
		//add 1 to number of bomb can be placed in one round
		item = new Item();
		item.getAttr().setNumBombPerRound(1);
		
		//add blast power
		item = new Item();
		item.getAttr().setPowerX(500f);
		item.getAttr().setPowerY(500f);
		
		//create light
		new ConeLight(gamePlay.getRayHandler(), 1000, new Color(1f, 0.1f, 0.1f, 1f), 70,
				-49.9f, -34.9f, 45, 45);
		
		new ConeLight(gamePlay.getRayHandler(), 1000, new Color(0.1f, 0.5f, 1f, 1f), 70,
				49.9f, 34.9f, 225, 45);
		
		PointLight p1 = new PointLight(gamePlay.getRayHandler(), 1000, new Color(0.1f, 0.5f,
				0.5f, 1f), 50, 0, 0);
		
		PointLight p2 = new PointLight(gamePlay.getRayHandler(), 1000, new Color(0.1f, 0.5f,
				0.5f, 1f), 50, 0, 0);
		
		gamePlay.getRayHandler().setAmbientLight(0.1f, 0.1f, 0.1f,0.7f);
		
		Light.setContactFilter((short)1, (short)-1, (short)1);
		
		// create all objects
		MapLayer objectLayer = tiledMap.getLayers().get("object_layer_1");
		MapObjects mapObjects = objectLayer.getObjects();
		Iterator<MapObject> iter = mapObjects.iterator();
		Matrix4 trans = new Matrix4();
		trans.setTranslation(-viewportWidth / 2.f, -viewportHeight / 2.f, 0);
		while (iter.hasNext()) {
			RectangleMapObject rectangleMapObject = (RectangleMapObject) iter.next();
			Vector2 center = new Vector2();
			rectangleMapObject.getRectangle().getCenter(center);
			Vector3 vec3 = new Vector3(center.x, center.y, 0);
			vec3.scl(unitScale);
			vec3.mul(trans);
			
			//create a crate
			if (rectangleMapObject.getName().equalsIgnoreCase("crate")) {
				Crate c = new Crate(vec3.x, vec3.y, 3.9f, 3.9f, 100);
				c.create();
			}
			
			//create a brick
			if (rectangleMapObject.getName().equalsIgnoreCase("brick")) {
				Brick b = new Brick(vec3.x, vec3.y, 3.9f, 3.9f, 300);
				b.create();
			}
			
			//create bomber a
			if (rectangleMapObject.getName().equalsIgnoreCase("playera")) {
				Bomber bomber = new Bomber(vec3.x, vec3.y, 4, 4,10, 100, 2, 3);
				bomber.create();
				bomber.setAnimation(gamePlay.getAssetManager().get("img/animation/soldier.png", Texture.class), 3, 1);
				bomber.setBomberController(new BomberController(true));
				p1.attachToBody(bomber.getBox2dBody(), 0, 0);
			}
			
			//create bomber b
			if (rectangleMapObject.getName().equalsIgnoreCase("playerb")) {
				Bomber bomber = new Bomber(vec3.x, vec3.y, 4, 4,10, 100, 2, 3);
				bomber.create();
				bomber.setAnimation(gamePlay.getAssetManager().get("img/animation/soldier.png", Texture.class), 3, 1);
				bomber.setBomberController(new BomberController(false));
				p2.attachToBody(bomber.getBox2dBody(), 0, 0);
			}
		}
	}
	
	public void update(Matrix4 projectionMatrix, float viewportWidth, float viewportHeight) {
		mat = projectionMatrix.cpy();
		mat.translate(-viewportWidth / 2.f, -viewportHeight / 2.f, 0);
		tiledMapRenderer.setView(mat, 0, 0, viewportWidth, viewportHeight);
		if (!isObjectLoaded) {
			loadObject(viewportWidth, viewportHeight);
			isObjectLoaded = true;
		}
	}
	
	public void renderBackground() {
		tiledMapRenderer.render(backgroundLayers);
	}
	
	public void renderForeground() {
		tiledMapRenderer.render(foregroundLayers);
	}
	
	public void dispose() {
		tiledMapRenderer.dispose();
	}

}

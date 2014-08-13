package com.game.bomberfight.system;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.Item;
import com.game.bomberfight.core.TileMapManager;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;

public class TileMapEffectSystem {
	
	private TileMapManager tileMapManager;
	private float unitScale;
	private Matrix4 trans = new Matrix4();
	private TiledMapTileLayer tiledMapTileLayer;
	private Vector2 tempVector2 = new Vector2();
	private Vector3 tempVector3 = new Vector3();
	private Array<Player> playerList;

	public TileMapEffectSystem(TileMapManager tileMapManager, Array<Player> playerList) {
		// TODO Auto-generated constructor stub
		this.tileMapManager = tileMapManager;
		unitScale = this.tileMapManager.getUnitScale();
		tiledMapTileLayer = (TiledMapTileLayer) this.tileMapManager.getTiledMap().getLayers().get("image_layer_1");
		trans.setTranslation(tileMapManager.getMapWidth() / 2.f, 
				tileMapManager.getMapHeight() / 2.f, 0);
		this.playerList = playerList;
	}
	
	public void update(float delta) {
		for (Player player : playerList) {
			update(player);
		}
	}
	
	public void update(Player player) {
		tempVector2 = player.getBox2dBody().getPosition();
		tempVector3 = new Vector3(tempVector2, 0);
		tempVector3.mul(trans);
		tempVector3.scl(1f / unitScale);
		int x = (int) (tempVector3.x / 32f);
		int y = (int) (tempVector3.y / 32f);
		Cell cell = tiledMapTileLayer.getCell(x, y);
		if (cell == null) {
			return;
		}
		MapProperties mapProperties = cell.getTile().getProperties();
		Object object = mapProperties.get("effect");
		Bomber bomber = (Bomber) player;
		if (object != null) {
			String value = (String) object;
			if (value.equalsIgnoreCase("speedup")) {
				if (!bomber.hasItem("SPEED_UP")) {
					Item item = new Item();
					item.setName("SPEED_UP");
					item.setAffectTime(1);
					item.getAttr().setSpeed(5f);
					item.setSprite(((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("img/texture/item4.png", Texture.class));
					bomber.pickUp(item);
				}
			}
			if (value.equalsIgnoreCase("speeddown")) {
				if (!bomber.hasItem("SPEED_DOWN")) {
					Item item = new Item();
					item.setName("SPEED_DOWN");
					item.setAffectTime(1);
					item.getAttr().setSpeed(-5f);
					item.setSprite(((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("img/texture/item5.png", Texture.class));
					bomber.pickUp(item);
				}
			}
		}
	}

}

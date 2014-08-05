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
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.Item;
import com.game.bomberfight.core.TileMapManager;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;

public class TileMapEffectSystem {
	
	private TileMapManager tileMapManager;
	private Player playerA;
	private Player playerB;
	private float unitScale;
	private Matrix4 trans = new Matrix4();
	private TiledMapTileLayer tiledMapTileLayer;
	private Vector2 tempVector2 = new Vector2();
	private Vector3 tempVector3 = new Vector3();

	public TileMapEffectSystem(TileMapManager tileMapManager) {
		// TODO Auto-generated constructor stub
		this.tileMapManager = tileMapManager;
		unitScale = this.tileMapManager.getUnitScale();
		playerA = this.tileMapManager.getPlayerA();
		playerB = this.tileMapManager.getPlayerB();
		tiledMapTileLayer = (TiledMapTileLayer) this.tileMapManager.getTiledMap().getLayers().get("image_layer_1");
		trans.setTranslation(100f / 2.f, 70f / 2.f, 0);
	}
	
	public void update(float delta) {
		update(playerA);
		update(playerB);
	}
	
	public void update(Player player) {
		tempVector2 = player.getBox2dBody().getPosition();
		tempVector3 = new Vector3(tempVector2, 0);
		tempVector3.mul(trans);
		tempVector3.scl(1f / unitScale);
		int x = (int) (tempVector3.x / 32f);
		int y = (int) (tempVector3.y / 32f);
		Cell cell = tiledMapTileLayer.getCell(x, y);
		MapProperties mapProperties = cell.getTile().getProperties();
		Object object = mapProperties.get("effect");
		if (object != null) {
			String value = (String) object;
			if (value.equalsIgnoreCase("speedup")) {
				Item item = new Item();
				item.setName("SPEED_UP");
				item.setAffectTime(1);
				item.setStackable(false);
				item.getAttr().setSpeed(5f);
				item.setSprite(((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("img/texture/item4.png", Texture.class));
				((Bomber)player).pickUp(item);
			}
			if (value.equalsIgnoreCase("speeddown")) {
				Item item = new Item();
				item.setName("SPEED_DOWN");
				item.setAffectTime(1);
				item.setStackable(false);
				item.getAttr().setSpeed(-5f);
				item.setSprite(((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("img/texture/item5.png", Texture.class));
				((Bomber)player).pickUp(item);
			}
		}
	}

}

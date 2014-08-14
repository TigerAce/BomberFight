package com.game.bomberfight.model;

import com.badlogic.gdx.utils.Array;

public class MapInfo {
	
	public String name;
	public String tmx;
	public String preview;
	public int maxNumPlayer;
	public Array<ItemSpawnPoint> itemSpawnPoint = new Array<ItemSpawnPoint>();
	
	public String toString() {
		return name;
	}
	
	public boolean equals(MapInfo other) {
		if (this.name.equals(other.name) && 
				this.tmx.equals(other.tmx) && 
				this.preview.equals(other.preview) && 
				this.maxNumPlayer == other.maxNumPlayer) {
			return true;
		} else {
			return false;
		}
	}
	
	public static class ItemSpawnPoint {
		public float x;
		public float y;
		public float refreshTime;
		public String itemName;
	}

}

package com.game.bomberfight.model;

public class MapInfo {
	
	public String name;
	public String tmx;
	public String preview;
	public int maxNumPlayer;
	
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

}

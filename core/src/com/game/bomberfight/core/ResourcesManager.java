package com.game.bomberfight.core;

import java.util.HashMap;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ResourcesManager {

	private HashMap<String, Texture> textures;
	
	public ResourcesManager(){
		textures = new HashMap<String, Texture>();
	}
	
	public void loadTexture(String path, String key){
		Texture t = new Texture(Gdx.files.internal(path));
		if(t != null) textures.put(key, t);
	}
	
	public Texture getTexture(String key){
		return textures.get(key);
	}
	
	public void disposeTexture(String key){
		Texture t = textures.get(key);
		if(t != null) t.dispose();
	}
	
	public void disposeAll(){
		Iterator<Entry<String, Texture>> iter = textures.entrySet().iterator();
		while(iter.hasNext()){
			 iter.next().getValue().dispose();
			 iter.remove();
		}
	}
}

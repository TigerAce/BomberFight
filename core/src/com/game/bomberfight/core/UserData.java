package com.game.bomberfight.core;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class UserData {

	private String name;
	private Object object;
	private Sprite sprite;
	
	/**
	 * store information into this body
	 * @param name           give a name to body
	 * @param object         the object instance that body belongs to
	 * @param sprite         the sprite of body
	 */
	public UserData(String name, Object object, Sprite sprite) {
		this.name = name;
		this.object = object;
		this.sprite = sprite;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}

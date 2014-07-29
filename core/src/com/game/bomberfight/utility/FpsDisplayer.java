package com.game.bomberfight.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FpsDisplayer {
	
	private static FpsDisplayer instance = null;
	private BitmapFont font;

	protected FpsDisplayer() {
		// TODO Auto-generated constructor stub
		font = new BitmapFont();
		font.setScale(0.09f, 0.1f);
	}
	
	public static FpsDisplayer getInstance() {
		if (instance == null) {
			return instance = new FpsDisplayer();
		}
		return instance;
	}
	
	public void draw(SpriteBatch batch, float x, float y) {
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), x, y);
	}

}

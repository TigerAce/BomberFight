package com.game.bomberfight.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class FpsDisplayer {
	
	private static FpsDisplayer instance = null;
	private BitmapFont font;
	private Matrix4 mat;
	private float width;
	private float height;

	protected FpsDisplayer() {
		// TODO Auto-generated constructor stub
		font = new BitmapFont();
		mat = new Matrix4();
	}
	
	public static FpsDisplayer getInstance() {
		if (instance == null) {
			return instance = new FpsDisplayer();
		}
		return instance;
	}
	
	public void draw(SpriteBatch batch, float x, float y) {
		Matrix4 oldmat = batch.getProjectionMatrix();
		batch.setProjectionMatrix(mat);
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), x, height - y);
		batch.end();
		batch.setProjectionMatrix(oldmat);
	}
	
	public void update(float width, float height) {
		this.width = width;
		this.height = height;
		mat.setToOrtho2D(0, 0, this.width, this.height);
	}

}

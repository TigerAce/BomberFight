package com.game.bomberfight.utility;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TiledNinePatch extends TextureRegionDrawable {
	
	private NinePatch ninePatch;
	private float ninePatchWidth;
	private float ninePatchHeight;

	public TiledNinePatch(NinePatch ninePatch, float ninePatchWidth, float ninePatchHeight) {
		this.ninePatch = ninePatch;
		this.ninePatchWidth = ninePatchWidth;
		this.ninePatchHeight = ninePatchHeight;
		this.setMinWidth(ninePatchWidth);
		this.setMinHeight(ninePatchHeight);
	}

	public TiledNinePatch(TextureRegion region) {
		super(region);
	}

	public TiledNinePatch(TextureRegionDrawable drawable) {
		super(drawable);
	}
	
	public void draw (Batch batch, float x, float y, float width, float height) {
		float regionWidth = ninePatchWidth, regionHeight = ninePatchHeight;
		float remainingX = width % regionWidth, remainingY = height % regionHeight;
		float startX = x, startY = y;
		float endX = x + width - remainingX, endY = y + height - remainingY;
		while (x < endX) {
			y = startY;
			while (y < endY) {
				ninePatch.draw(batch, x, y, ninePatchWidth, ninePatchHeight);
				y += regionHeight;
			}
			x += regionWidth;
		}
		if (remainingX > 0) {
			ninePatch.draw(batch, x, startY, remainingX, ninePatchHeight);
		}
		if (remainingY > 0) {
			ninePatch.draw(batch, x, startY, regionWidth, remainingY);
		}
	}

	/**
	 * @return the ninePatchWidth
	 */
	public float getNinePatchWidth() {
		return ninePatchWidth;
	}

	/**
	 * @param ninePatchWidth the ninePatchWidth to set
	 */
	public void setNinePatchWidth(float ninePatchWidth) {
		this.ninePatchWidth = ninePatchWidth;
		this.setMinWidth(ninePatchWidth);
	}

	/**
	 * @return the ninePatchHeight
	 */
	public float getNinePatchHeight() {
		return ninePatchHeight;
	}

	/**
	 * @param ninePatchHeight the ninePatchHeight to set
	 */
	public void setNinePatchHeight(float ninePatchHeight) {
		this.ninePatchHeight = ninePatchHeight;
		this.setMinHeight(ninePatchHeight);
	}

}

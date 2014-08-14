package com.game.bomberfight.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.bomberfight.model.GameObject;

public class CameraSystem {
	
	private Camera camera;
	private GameObject target;
	private Vector2 cameraPos = new Vector2();
	private Vector2 targetPos = new Vector2();
	private Vector3 tempVector3 = new Vector3();
	private float mapWidth;
	private float mapHeight;
	private float viewPortWidth;
	private float viewPortHeight;
	private float shakeTime = 1;
	private float shakeTimeCount = 1;
	private float offset = 0;

	public CameraSystem(Camera camera, GameObject target, float mapWidth, float mapHeight, 
			float viewPortWidth, float viewPortHeight) {
		this.camera = camera;
		this.target = target;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.viewPortWidth = viewPortWidth;
		this.viewPortHeight = viewPortHeight;
	}
	
	public void update(float delta) {
		cameraPos.set(this.camera.position.x, this.camera.position.y);
		targetPos.set(this.target.getBox2dBody().getPosition());
		tempVector3.set(targetPos, 0);
		
		if (shakeTimeCount <= shakeTime) {
			this.camera.position.x = this.camera.position.x + MathUtils.random(offset) * (MathUtils.randomBoolean() ? 1.f : -1.f);
			this.camera.position.y = this.camera.position.y + MathUtils.random(offset) * (MathUtils.randomBoolean() ? 1.f : -1.f);
			shakeTimeCount += delta;
		} else {
			this.camera.position.lerp(tempVector3, delta);
		}
		if (this.camera.position.x < -(mapWidth - viewPortWidth) / 2) {
			this.camera.position.x = -(mapWidth - viewPortWidth) / 2;
		}
		if (this.camera.position.x > (mapWidth - viewPortWidth) / 2) {
			this.camera.position.x = (mapWidth - viewPortWidth) / 2;
		}
		if (this.camera.position.y < -(mapHeight - viewPortHeight) / 2) {
			this.camera.position.y = -(mapHeight - viewPortHeight) / 2;
		}
		if (this.camera.position.y > (mapHeight - viewPortHeight) / 2) {
			this.camera.position.y = (mapHeight - viewPortHeight) / 2;
		}
	}
	
	public void shake(float dist2, float powerX, float powerY) {
		shakeTimeCount = 0;
		offset = (powerX + powerY) / dist2;
		if(offset > 10){
			offset = 10;
		}
		if (offset < 0.1f) {
			offset = 0;
		}
		if (offset > 10) {
			offset = 10;
		}
	}

}

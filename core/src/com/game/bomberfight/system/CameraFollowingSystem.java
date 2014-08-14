package com.game.bomberfight.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.bomberfight.model.GameObject;

public class CameraFollowingSystem {
	
	private Camera camera;
	private GameObject target;
	private Vector2 cameraPos = new Vector2();
	private Vector2 targetPos = new Vector2();
	private Vector3 tempVector3 = new Vector3();
	private float mapWidth;
	private float mapHeight;
	private float viewPortWidth;
	private float viewPortHeight;

	public CameraFollowingSystem(Camera camera, GameObject target, float mapWidth, float mapHeight, 
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
		
		this.camera.position.lerp(tempVector3, delta);
		
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

}

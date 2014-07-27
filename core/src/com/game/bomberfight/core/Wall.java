package com.game.bomberfight.core;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;

import com.game.bomberfight.model.Barrier;
import com.game.bomberfight.screen.GamePlay;

public class Wall extends Barrier{

	/**
	 * store all bodies in current wall
	 */
	private ArrayList<Body> wallBodies;
	
	/**
	 * form a wall with center point x,y and given width and height
	 * 
	 * @param x 	center point x  (world position)
	 * @param y     center point y  (world position)
	 * @param width 	wall width (meters) only used in rectangle
	 * @param height	wall height (meters) only used in rectangle
	 */
	public Wall(float x, float y, float width, float height) {
		super(x, y, width, height);
	
		wallBodies = new ArrayList();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Body> getWallBodies() {
		return wallBodies;
	}

	/**
	 * create a rectangle wall frame with given width and height in constructor
	 */
	public void setAsRectangleFrame(){
		//top
		createWallLine(x - (width / 2), y + (height / 2), x + (width / 2), y + (height / 2), "upWallFrame");
		//right
		createWallLine(x + (width / 2), y + (height / 2), x + (width / 2), y - (height / 2), "rightWallFrame");
		//bottom
		createWallLine(x + (width / 2), y - (height / 2), x - (width / 2), y - (height / 2), "bottomWallFrame");
		//left
		createWallLine(x - (width / 2), y - (height / 2), x - (width / 2), y + (height / 2), "leftWallFrame");
	}
	
	/**
	 * create a wall line between two point
	 * @param ax     Point A   x coordinate    (world position)
	 * @param ay     Point A   y coordinate    (world position)  
	 * @param bx     Point B   x coordinate    (world position)
	 * @param by     Point B   y coordinate    (world position)
	 * @param name   name of body
	 */
	public Body createWallLine(float ax, float ay, float bx, float by, String name){
		BodyDef wallDef = new BodyDef();
		wallDef.type = BodyType.StaticBody;

		
		//shape
		ChainShape wallShape = new ChainShape();
		wallShape.createChain(new Vector2[]{new Vector2(ax, ay), new Vector2(bx, by)});
		
		//fixture
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = wallShape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;

        Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
		Body wall = ((GamePlay)currentScreen).getWorld().createBody(wallDef);
		wall.createFixture(fixtureDef);
	
		wall.setUserData(new UserData(name, null, null));
		
		this.wallBodies.add(wall);
		
		return wall;
	}
	
	@Override
	public void dispose() {
		for(Body b : wallBodies){
			for(Fixture f : b.getFixtureList()){
				Shape s = f.getShape();
				if(s != null){
					f.getShape().dispose();
				}
			}
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create() {
		
		
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

}

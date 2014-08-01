package com.game.bomberfight.model;

public abstract class Controller {
	
	public GameObject owner;

	public Controller() {
		// TODO Auto-generated constructor stub
		owner = null;
	}
	
	public abstract void update();

}

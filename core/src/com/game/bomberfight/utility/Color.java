package com.game.bomberfight.utility;

public class Color extends com.badlogic.gdx.graphics.Color {

	public Color(String color) {
		// TODO Auto-generated constructor stub
		super();
		String[] rgba = color.split("\\s");
		r = Float.parseFloat(rgba[0]);
		g = Float.parseFloat(rgba[1]);
		b = Float.parseFloat(rgba[2]);
		a = Float.parseFloat(rgba[3]);
	}

}

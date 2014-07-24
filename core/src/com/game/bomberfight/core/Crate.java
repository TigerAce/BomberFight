package com.game.bomberfight.core;

import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.model.Barrier;

public class Crate extends Barrier implements Destructible{

	protected Crate(float x, float y, float size) {
		super(x, y, size);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fragment(float fragCenterX, float fragCenterY, int fragPieces) {
		// TODO Auto-generated method stub
		
	}

}

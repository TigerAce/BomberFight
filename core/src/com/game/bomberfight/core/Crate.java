package com.game.bomberfight.core;

import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.model.Barrier;

public class Crate extends Barrier implements Destructible{

	protected Crate(float x, float y, float width, float height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

    @Override
    public void create() {
    	System.out.println("branch and merge test");
    	System.out.println("branch and merge test2");
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

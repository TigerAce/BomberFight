package com.game.bomberfight.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener{

	@Override
	public void connected(Connection arg0) {
		// TODO Auto-generated method stub
		super.connected(arg0);
	}

	@Override
	public void disconnected(Connection arg0) {
		// TODO Auto-generated method stub
		super.disconnected(arg0);
	}

	@Override
	public void idle(Connection arg0) {
		// TODO Auto-generated method stub
		super.idle(arg0);
	}

	@Override
	public void received(Connection arg0, Object arg1) {
		
	}

}

package com.game.bomberfight.screen;


import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.game.bomberfight.core.BomberFight;
import com.game.bomberfight.net.Network;


public class MultiplayerGamePlay extends GamePlay{

	public static final Client client = new Client();

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
	}

	@Override
	public void show(){
		// TODO Auto-generated method stub
		((BomberFight) Gdx.app.getApplicationListener())
		.setGameState(BomberFight.MULTIPLAYER_GAME_PLAY_STATE);
		
		
		//register client
		Network.register(client);
		
		//start client
		client.start();
		
		//connect to server
	    try {
			client.connect(5000, "192.168.1.5", Network.portTCP, Network.portUDP);	
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
	    
	    //add listener
	    client.addListener(new Listener() {
	    	
	    
	        public void received (Connection connection, Object object) {
	        	
	        	if(object instanceof Network.StartMovePlayer){
	        	
	    			tileMapManager.getPlayerB().startMovePlayer();
	        		
	    		}
	    		
	    		if(object instanceof Network.StopMovePlayer){
	    			
	    			tileMapManager.getPlayerB().stopMovePlayer();
	    			
	    		}
	        }
	     });
	    
	 
	    super.show();

	}
	
}

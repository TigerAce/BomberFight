package com.game.bomberfight.screen;


import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.game.bomberfight.core.BomberFight;
import com.game.bomberfight.net.Network;


public class MultiplayerGamePlay extends GamePlay{

	public static final Client client = new Client();
	private float correction = 1;

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		
		//correct position every 1 sec
		correction -= delta;
		if(correction <= 0){
			Body b = tileMapManager.getPlayerA().getBox2dBody();
			client.sendTCP(new Network.CorrectPosition(new Vector2(b.getPosition().x,b.getPosition().y)));
			correction = 1;
		}
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
			client.sendTCP(new Network.JoinGame("map1", (short)2));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
	    
	    //add listener
	    client.addListener(new Listener() {
	    	
	    
	        public void received (Connection connection, Object object) {
	        	if(object instanceof Network.BornPosition){
	        		System.out.println(((Network.BornPosition)object).positionNumber);
	        	}
	        	
	        	if(object instanceof Network.StartMovePlayer){
	       
	    			tileMapManager.getPlayerB().startMovePlayer(((Network.StartMovePlayer)object).direction);
	        		
	    		}
	    		
	    		if(object instanceof Network.StopMovePlayer){
	    			
	    			tileMapManager.getPlayerB().stopMovePlayer();
	    			
	    		}
	    		
	    		if(object instanceof Network.CorrectPosition){
	    		
	    			Vector2 p = ((Network.CorrectPosition)object).pos;
	    			Body b = tileMapManager.getPlayerB().getBox2dBody();
	    			
	    			if(b.getPosition().x != p.x || b.getPosition().y != p.y)
	    			b.setTransform(((Network.CorrectPosition)object).pos, b.getAngle());
	    		}
	        }
	     });
	    
	 
	    super.show();

	}
	
}

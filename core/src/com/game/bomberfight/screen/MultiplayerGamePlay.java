package com.game.bomberfight.screen;


import java.io.IOException;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.game.bomberfight.InputSource.BomberController;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.BomberFight;
import com.game.bomberfight.core.Item;
import com.game.bomberfight.net.Network;


public class MultiplayerGamePlay extends GamePlay{

	public static final Client client = new Client();
	private float correction = 1;
	private int position;
	public static boolean dead = false;
	private String startGame[] = {"false"};
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		
		
		
		if(!dead){
		//correct position every 1 sec
		correction -= delta;
		if(correction <= 0){
			
			Body b = playerList.get(position).getBox2dBody();
		
			client.sendTCP(new Network.CorrectPosition(new Vector2(b.getPosition().x,b.getPosition().y),position));
			correction = 1;
		}
		}
	}

	@Override
	public void show(){
		
		// TODO Auto-generated method stub
		((BomberFight) Gdx.app.getApplicationListener())
		.setGameState(BomberFight.MULTIPLAYER_GAME_PLAY_STATE);
		
		super.show();
		
		
		
		
		
		//register client
		Network.register(client);
		
		//start client
		client.start();
		
		//connect to server
	    try {
			client.connect(5000, "localhost", Network.portTCP, Network.portUDP);	//yijiasup.no-ip.org
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
	    
	    //add listener
	    client.addListener(new Listener() {
	    	
	    
	        @SuppressWarnings("unchecked")
			public void received (Connection connection, Object object) {
	        	
	        	if(object instanceof Network.StartGame){
	        		synchronized(startGame){
	        			startGame[0] = "true";
	        			startGame.notify();
	        		}
	        		System.out.println("start the game");
	        	}
	        	
	        	if(object instanceof Network.AssignBornPosition){
	        		
	        		position = ((Network.AssignBornPosition)object).positionNumber;
	        		assignController(position);
	        		
	        		//response for completion of initialization of the game
	        		connection.sendTCP(new Network.CompleteInitGame());
	        		
	        	}
	        	
	        	if(object instanceof Network.ConfirmDropItem){
	        		Network.ConfirmDropItem confirm = (Network.ConfirmDropItem)object;
	        		
	        		Item tmp = null;
	        		
	        		for(Item i : itemList){
	        			if(i.getName().equals(confirm.itemName)){
	        				tmp = new Item(i);
	        				break;
	        			}
	        		}
	        		
	        		if(tmp != null){
					if(confirm.itemName == "POWER_UP")
						tmp.setSprite(assetManager.get("img/texture/item1.png", Texture.class));
					if(confirm.itemName == "ANNULAR")
						tmp.setSprite(assetManager.get("img/texture/item2.png", Texture.class));
					if(confirm.itemName == "ADDBOMB")
						tmp.setSprite(assetManager.get("img/texture/item3.png", Texture.class));
    				tmp.setX(confirm.dropPosition.x);
    				tmp.setY(confirm.dropPosition.y);
    				tmp.create();
	        		}
	        	}
	        	
	        	if(object instanceof Network.RemoteControl){
	        		Network.RemoteControl remoteControl = (Network.RemoteControl)object;
	        		keyMaps.get(remoteControl.playerID).put(remoteControl.keycode, remoteControl.upOrDown);
	        	}
	    		
	        	if(object instanceof Network.LeaveGame){
	        		//the gamePosition of the player need to be delete
	        		int gamePosition = ((Network.LeaveGame)object).gamePosition;
	        		playerList.get(gamePosition).dispose();
	        		playerList.removeIndex(gamePosition);
	        	}
	    		
	    		if(object instanceof Network.CorrectPosition){
	    		
	    			
	    				Vector2 p = ((Network.CorrectPosition)object).pos;
	    				int playerID = ((Network.CorrectPosition)object).playerID;
		    		
		    			Body b = playerList.get(playerID).getBox2dBody();
		    			if(b.getPosition().x != p.x || b.getPosition().y != p.y){
		    				if(!world.isLocked()){
		    					b.setTransform(((Network.CorrectPosition)object).pos, b.getAngle());
		    				}
		    			}
	    		}
	    		   		
	        }
	     });
	    
	    
		client.sendTCP(new Network.JoinGame(gameInfo.mapInfo.name, (short)2));
		
		System.out.println("waiting");
		synchronized(startGame){
			while(startGame[0] != "true")
				try {
					startGame.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	 
	    

	}
	
	@Override
	public void dispose(){
		super.dispose();
		client.close();
	}
	
	public void assignController(int position){
		Bomber me = (Bomber) playerList.get(position);
		//gui.setFixedStatusBar(me);
		BomberController bomberController = new BomberController(new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D, Input.Keys.SPACE});
		inputMultiplexer.addProcessor(bomberController);
		me.setController(bomberController);

	}
	
}

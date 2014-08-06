package com.game.bomberfight.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;





public class BomberFightServer {

	public static void main(String[] args){
		final Server server = new Server();
		Network.register(server);
	    server.start();
	    try {
			server.bind(Network.portTCP, Network.portUDP);
			
			server.addListener(new Listener() {
			       public void received (Connection connection, Object object) {
			    	   
			    	if(server.getConnections().length == 2){
			    			if(connection.getID() == 1){
			    				if(object instanceof Network.StartMovePlayer || object instanceof Network.StopMovePlayer || object instanceof Network.CorrectPosition)
			    				server.sendToTCP(2, object);
			    				
			    				
			    				
			    			}
			    			if(connection.getID() == 2){
			    				if(object instanceof Network.StartMovePlayer || object instanceof Network.StopMovePlayer || object instanceof Network.CorrectPosition)
			    				server.sendToTCP(1, object);
			    				
			    			}
			    			  
			    	  }
			       }
			    	 
			      
			    });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
}

package com.game.bomberfight.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.game.bomberfight.server.Network;
import com.game.bomberfight.server.Player;
import com.game.bomberfight.server.Room;






public class BomberFightServer{


	public static ArrayList<Room> waitingRoomList = new ArrayList<Room>();
	public static ArrayList<Room> playingRoomList = new ArrayList<Room>();
	public static HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	public static final Server server = new Server();
	
	

	public static void startGame(Room room){
	
		//distribute player born position  & send player position to each client
		for(int i = 0; i < room.getPlayerInRoom().size(); i++){

			server.sendToTCP(room.getPlayerInRoom().get(i).getPlayerID(), new Network.BornPosition((short)(i + 1)));
		}
		//wait all clients create players and signal back to server for completion
		
		//signal to all clients to start game
		
	}
	
	public static void main(String[] args){
	
		//register server
		Network.register(BomberFightServer.server);
		
		//start server and bind port
		BomberFightServer.server.start();
	    
	    try {
			server.bind(Network.portTCP, Network.portUDP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	    
	    
	    //add server listener
	    server.addListener(new Listener() {
	    	
	    	public void connected(Connection connection){
	    		//if a client is connected server, create a player status for the client
	    		//give room number -1 indicates no room assigned
	    		BomberFightServer.players.put(connection.getID(),new Player(connection.getID(), -1));
	    		System.out.println("connected player number:" + connection.getID());
	    	}
	    	
	    	public void disconnected(Connection connection){
	    		/*
	    		 * TODO: if client disconnected, remove relative player form server
	    		 */
	    		
	    	}
	    	
	    	public void received (Connection connection, Object object) {
			    	  
	    		/********************************
	    		 * if a player want to join game*
	    		 ********************************/
	    		
			    	if(object instanceof Network.JoinGame){
			    		Network.JoinGame jg = (Network.JoinGame)object;
			    		
			    		//check if there exist a game satisfy player's requirements
			    		for(Room room : BomberFightServer.waitingRoomList){
			    		
			    			if(jg.mapName.equals(room.getMapName()) && jg.numPlayers == room.getDesireNumPlayer()){
			    				
			    				System.out.println("found room for player id:" + connection.getID());
			    				
			    				//match the condition, add player to room
			    				room.addPlayer(BomberFightServer.players.get(connection.getID()));
			    				
			    				break;
			    			}
			    		}
			    		
			    	
			    		//if no room found, create a new room for player
			    		if(BomberFightServer.players.get(connection.getID()).getRoomNumber() == -1){
			  
			    			System.out.println("create room for player id:" + connection.getID());
			    			
			    			Room room = new Room(BomberFightServer.waitingRoomList.size() + 1, jg.mapName, Network.MapInfo.mapInfo.get(jg.mapName),jg.numPlayers);
			    			room.addPlayer(BomberFightServer.players.get(connection.getID()));
			    			
			    			//add room to waiting room list
			    			BomberFightServer.waitingRoomList.add(room);
			    		}
			    		
			    	}
			    	
			    	
			    	
			    	
			    	
			    	
			    	
			    	
			}
		      
		});
		
	    
	}
}

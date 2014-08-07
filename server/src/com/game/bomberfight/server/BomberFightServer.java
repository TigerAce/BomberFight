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
import com.game.bomberfight.server.Room.RoomState;






public class BomberFightServer{


	public static ArrayList<Room> roomList = new ArrayList<Room>();
	public static HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	public static final Server server = new Server();
	
	
	
	
	
	
	/**
	 * this function is used do send the initialization information to client 
	 * including:
	 * assign the player positions
	 * 
	 * the client will need to send back a CompleteInitGame signal to do the final confirm before start the game
	 * @param room
	 */
	public static void initGame(Room room){
	
		//distribute player born position  & send player position to each client
		for(int i = 0; i < room.getPlayerInRoom().size(); i++){

			server.sendToTCP(room.getPlayerInRoom().get(i).getPlayerID(), new Network.AssignBornPosition((short)(i + 1)));
		}
		
		//set up player check list for room
		room.setPlayerChecker(room.getPlayerInRoom().size());
		
		
	
	}
	
	/**
	 * this function is used to send the start game signal to all the client in a room. after the confirmation check finish
	 * @param room
	 */
	public static void startGame(Room room){
		
		//signal all the player in the room to start game
		for(int i = 0; i < room.getPlayerInRoom().size(); i++){

			server.sendToTCP(room.getPlayerInRoom().get(i).getPlayerID(), new Network.StartGame());
		}
		
		//change room state to playing
		room.state = RoomState.PLAYING;
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
	    		BomberFightServer.players.put(connection.getID(),new Player(connection.getID(), null));
	    		System.out.println("connected player number:" + connection.getID());
	    	}
	    	
	    	public void disconnected(Connection connection){
	    		/*
	    		 * TODO: if client disconnected, remove relative player form server
	    		 */
	    		
	    	}
	    	
	    	public void received (Connection connection, Object object) {
			

		    	/********************************************************************************
		    	 * 			     ACTIONS BEFORE GAME START                                      *
		    	 ********************************************************************************/
	    		
	    		/********************************
	    		 * if a player want to join game*
	    		 ********************************/
	    		
			    	if(object instanceof Network.JoinGame){
			    		Network.JoinGame jg = (Network.JoinGame)object;
			    		
			    		//check if there exist a game satisfy player's requirements
			    		for(Room room : BomberFightServer.roomList){
			    		
			    			if(room.state == RoomState.WAITING){
			    				if(jg.mapName.equals(room.getMapName()) && jg.numPlayers == room.getDesireNumPlayer()){
			    				
			    					System.out.println("found room for player id:" + connection.getID());
			    				
			    					//match the condition, add player to room
			    					room.addPlayer(BomberFightServer.players.get(connection.getID()));
			    				
			    					break;
			    				}
			    			}
			    		}
			    		
			    	
			    		//if no room found, create a new room for player
			    		if(BomberFightServer.players.get(connection.getID()).getInRoom() == null){
			  
			    			System.out.println("create room for player id:" + connection.getID());
			    			
			    			Room room = new Room(BomberFightServer.roomList.size() + 1, jg.mapName, Network.MapInfo.mapInfo.get(jg.mapName),jg.numPlayers);
			    			room.addPlayer(BomberFightServer.players.get(connection.getID()));
			    			
			    			//add room to waiting room list
			    			BomberFightServer.roomList.add(room);
			    		}
			    		
			    	}
			    	

		    		/********************************
		    		 * if player complete init game *
		    		 ********************************/
			    	if(object instanceof Network.CompleteInitGame){
			    		//find the room of the player
			    		Room r = players.get(connection.getID()).getInRoom();
			    		
			    		//tick for complete init game
			    		r.setPlayerChecker(r.getPlayerChecker() - 1);
			    		
			    		//if all the player complete the init of game, start game
			    		if(r.getPlayerChecker() <= 0){
			    			//start game for this room
			    			startGame(r);
			    		}
			    	}
			    	
			    	
			    	
			    	
			    	
			    	
			    	
			    	
			    	/********************************************************************************
			    	 * 			     ACTIONS AFTER GAME START                                       *
			    	 ********************************************************************************/
			    	
			    	
			    	
		    		/********************************
		    		 * if player moved              *
		    		 ********************************/
			    	if(object instanceof Network.StartMovePlayer){
			    		server.sendToAllExceptTCP(connection.getID(), object);
			    	}
			    	
			    	/********************************
		    		 * if player stop moved         *
		    		 ********************************/
			    	if(object instanceof Network.StopMovePlayer){
			    		server.sendToAllExceptTCP(connection.getID(), object);
			    	}
			    	
			    	/********************************
		    		 * player correct position      *
		    		 ********************************/
			    	if(object instanceof Network.CorrectPosition){
			    		server.sendToAllExceptTCP(connection.getID(), object);
			    	}
			}
		      
		});
		
	    
	}
}

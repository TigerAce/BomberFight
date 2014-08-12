package com.game.bomberfight.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.MapInfo;
import com.game.bomberfight.model.PlayerInfo;
import com.game.bomberfight.net.Network;
import com.game.bomberfight.net.Network.RequireJoinGame;
import com.game.bomberfight.net.Network.RequireUpdateBombPositionToOthers;
import com.game.bomberfight.net.Network.RequireUpdateDropItem;
import com.game.bomberfight.net.Network.RequireUpdateInputToOthers;
import com.game.bomberfight.net.Network.RequireUpdatePositionToOthers;
import com.game.bomberfight.net.Network.RespondJoinGame;
import com.game.bomberfight.net.Network.SignalBarrierDestroyed;
import com.game.bomberfight.net.Network.StartGame;
import com.game.bomberfight.net.Network.UpdateBombPosition;
import com.game.bomberfight.net.Network.UpdateDropItem;
import com.game.bomberfight.net.Network.UpdateInput;
import com.game.bomberfight.net.Network.UpdatePosition;
import com.game.bomberfight.server.Room.RoomState;

public class BomberFightServer {
	Server server;
	Array<Room> roomList;
	Map<Integer, Integer> connToRoomMap;


	public BomberFightServer() throws IOException {
		server = new Server();
		Network.register(server);
		
		roomList = new Array<Room>();
		connToRoomMap = new HashMap<Integer, Integer>();

		server.addListener(new Listener() {
			public void received(Connection c, Object object) {
				if (object instanceof RequireJoinGame) {
					RequireJoinGame requireJoinGame = (RequireJoinGame) object;
					joinGame(requireJoinGame.gameInfo);
				}
				
				if (object instanceof RequireUpdateInputToOthers) {
					RequireUpdateInputToOthers requireUpateInputToOthers = (RequireUpdateInputToOthers) object;
					Integer roomNumber = connToRoomMap.get(c.getID());
					if (roomNumber == null) {
						return;
					}
					Room room = roomList.get(roomNumber);
					UpdateInput updateInput = new UpdateInput();
					updateInput.conn = c.getID();
					updateInput.keyCode = requireUpateInputToOthers.keyCode;
					updateInput.keyState = requireUpateInputToOthers.keyState;
					sendToAllInRoomExcept(room, c.getID(), updateInput);
				}
				
				if (object instanceof RequireUpdatePositionToOthers) {
					RequireUpdatePositionToOthers requireUpdatePositionToOthers = (RequireUpdatePositionToOthers) object;
					Integer roomNumber = connToRoomMap.get(c.getID());
					if (roomNumber == null) {
						return;
					}
					Room room = roomList.get(roomNumber);
					UpdatePosition updatePosition = new UpdatePosition();
					updatePosition.conn = c.getID();
					updatePosition.x = requireUpdatePositionToOthers.x;
					updatePosition.y = requireUpdatePositionToOthers.y;
					sendToAllInRoomExcept(room, c.getID(), updatePosition);
				}
				
				if (object instanceof RequireUpdateBombPositionToOthers) {
					RequireUpdateBombPositionToOthers requireUpdateBombPositionToOthers = (RequireUpdateBombPositionToOthers) object;
					Integer roomNumber = connToRoomMap.get(c.getID());
					if (roomNumber == null) {
						return;
					}
					Room room = roomList.get(roomNumber);
					UpdateBombPosition updateBombPosition = new UpdateBombPosition();
					updateBombPosition.conn = c.getID();
					updateBombPosition.x = requireUpdateBombPositionToOthers.x;
					updateBombPosition.y = requireUpdateBombPositionToOthers.y;
					updateBombPosition.bombIndex = requireUpdateBombPositionToOthers.bombIndex;
					sendToAllInRoomExcept(room, c.getID(), updateBombPosition);
				}
				
				
				if (object instanceof RequireUpdateDropItem) {
					RequireUpdateDropItem request = (RequireUpdateDropItem)object;
					
				
					Room room = roomList.get(connToRoomMap.get(c.getID()));
					
					boolean proccessed = false;
				
					
					
					for (int id : room.destroyedGameObjectId) {
						
						if (id == request.id) {
							proccessed = true;
							break;
						}
					}
					
					if(!proccessed){
						
						room.destroyedGameObjectId.add(request.id);
						
						Random r = new Random();
						String itemName = null;
		    		
		    		
						if(request.name.equals(Network.CrateDropList.name)){
		    
							int rand = r.nextInt(Network.CrateDropList.dropProbability);
							if(rand == 1){
								//drop item
								int totalProbability = 0;
								for(Integer i :  Network.CrateDropList.ItemInfo.itemDropProbability.values()){
									totalProbability += i;
		    				}
		    				
		    				rand = r.nextInt(totalProbability);
		    				
		    				int counter = 0;
		    			
		    				for (Map.Entry<String, Integer> entry : Network.CrateDropList.ItemInfo.itemDropProbability.entrySet()) {
		    					
		    					if(rand >= counter && rand < counter + entry.getValue()){
		    					
		    						itemName = entry.getKey();
		    						break;
		    					}else counter += entry.getValue();

		    				}
		    				
		    				
		    				
		    			}
		    				
						}else if(request.name.equals(Network.BrickDropList.name)){
							int rand = r.nextInt(Network.BrickDropList.dropProbability);
							if(rand == 1){
								//drop item
								int totalProbability = 0;
								for(Integer i :  Network.BrickDropList.ItemInfo.itemDropProbability.values()){
									totalProbability += i;
								}
		    				
								rand = r.nextInt(totalProbability);
		    				
								int counter = 0;
		    			
		    				
								for (Map.Entry<String, Integer> entry : Network.BrickDropList.ItemInfo.itemDropProbability.entrySet()) {
		    					
									if(rand >= counter && rand < counter + entry.getValue()){
										itemName = entry.getKey();
										break;
									}else counter += entry.getValue();

								}
		    				
							}
						}
		    		
						//if item name not null drop item
						if(itemName != null){
		    			
		    		
							UpdateDropItem updateDropItem = new UpdateDropItem();
							updateDropItem.id = request.id;
							updateDropItem.name = itemName;
							updateDropItem.x = request.x;
							updateDropItem.y = request.y;
					
							sendToAllInRoom(room, updateDropItem);
		    			
						}else{
						
						//signal all the client in the room that crate or brick destroyed
						SignalBarrierDestroyed signal = new SignalBarrierDestroyed();
						signal.id = request.id;
						
						sendToAllInRoomExcept(room, c.getID(), signal);
						}
						
						
					}
		    	
					
					
				}
			}

			public void disconnected(Connection c) {
				Room room = roomList.get(connToRoomMap.get(c.getID()));
				for (PlayerInfo playerInfo : room.playerInfoList) {
					if (playerInfo.conn == c.getID()) {
						room.playerInfoList.removeValue(playerInfo, true);
						if (room.playerInfoList.size == 0) {
							System.out.println("room#"+room.number+" has been removed"+"\n");
							roomList.removeValue(room, true);
							connToRoomMap.remove(c.getID());
						}
					}
				}
			}
		});
		
		server.bind(Network.portTCP, Network.portUDP);
		server.start();
	}
	
	public void joinGame(GameInfo gameInfo) {
		Room room = findRoomBy(gameInfo.mapInfo, RoomState.waiting);
		if (room != null) {
			room.add(gameInfo.playerInfo);
			gameInfo.playerInfo.roomNumber = room.number;
			gameInfo.playerInfo.spawnPoint = room.playerInfoList.size - 1;
			
			connToRoomMap.put(gameInfo.playerInfo.conn, room.number);
			
			RespondJoinGame respondJoinGame = new RespondJoinGame();
			respondJoinGame.gameInfo = gameInfo;
			respondJoinGame.result = "joined room #" + room.number;
			System.out.println("connection"+gameInfo.playerInfo.conn+" joined room#"+room.number+"\n");
			server.sendToTCP(gameInfo.playerInfo.conn, respondJoinGame);
			
			if (room.playerInfoList.size == gameInfo.mapInfo.maxNumPlayer) {
				System.out.println("room#"+room.number+" start game"+"\n");
				startGame(room);
			}
		} else {
			room = new Room();
			roomList.add(room);
			room.number = roomList.size - 1;
			room.state = RoomState.waiting;
			room.playerInfoList.add(gameInfo.playerInfo);
			room.gameInfo = gameInfo;
			
			gameInfo.playerInfo.roomNumber = room.number;
			gameInfo.playerInfo.spawnPoint = room.playerInfoList.size - 1;
			
			connToRoomMap.put(gameInfo.playerInfo.conn, room.number);
			System.out.println("room#"+room.number+" has been created"+"\n");
			
			RespondJoinGame respondJoinGame = new RespondJoinGame();
			respondJoinGame.gameInfo = gameInfo;
			respondJoinGame.result = "created room #" + room.number;
			server.sendToTCP(gameInfo.playerInfo.conn, respondJoinGame);
		}
	}
	
	public Room findRoomBy(MapInfo mapInfo, RoomState state) {
		for (Room room : roomList) {
			if (room.gameInfo.mapInfo.equals(mapInfo) && room.state == state) {
				return room;
			}
		}
		return null;
	}
	
	public void startGame(Room room) {
		room.state = RoomState.playing;
		for (PlayerInfo playerInfo : room.playerInfoList) {
			StartGame GO = new StartGame();
			GO.playerInfoList = room.playerInfoList.toArray(PlayerInfo.class);
			server.sendToTCP(playerInfo.conn, GO);
		}
	}
	
	public void sendToAllInRoomExcept(Room room, int conn, Object object) {
		for (PlayerInfo playerInfo : room.playerInfoList) {
			if (playerInfo.conn != conn) {
				server.sendToTCP(playerInfo.conn, object);
			}
		}
	}
	
	public void sendToAllInRoom(Room room, Object object) {
		for (PlayerInfo playerInfo : room.playerInfoList) {
			server.sendToTCP(playerInfo.conn, object);
		}
	}

	public static void main(String[] args) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		new BomberFightServer();
	}
}

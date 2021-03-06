package com.game.bomberfight.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.MapInfo;
import com.game.bomberfight.model.PlayerInfo;
import com.game.bomberfight.net.Network;
import com.game.bomberfight.net.Network.ItemName;
import com.game.bomberfight.net.Network.RequireJoinGame;
import com.game.bomberfight.net.Network.RequireUpdateBombPositionToOthers;
import com.game.bomberfight.net.Network.RequireUpdateDropItem;
import com.game.bomberfight.net.Network.RequireUpdateHealthToOthers;
import com.game.bomberfight.net.Network.RequireUpdateInputToOthers;
import com.game.bomberfight.net.Network.RequireUpdatePositionToOthers;
import com.game.bomberfight.net.Network.RespondJoinGame;
import com.game.bomberfight.net.Network.SignalBarrierDestroyed;
import com.game.bomberfight.net.Network.StartGame;
import com.game.bomberfight.net.Network.UpdateBombPosition;
import com.game.bomberfight.net.Network.UpdateDropItem;
import com.game.bomberfight.net.Network.UpdateHealth;
import com.game.bomberfight.net.Network.UpdateInput;
import com.game.bomberfight.net.Network.UpdatePosition;
import com.game.bomberfight.server.Room.RoomState;

public class BomberFightServer {
	Server server;
	Map<Integer, Room> connToRoomMap;
	
	public static float serverTime;
	public static Timer serverTimer;


	public BomberFightServer() throws IOException {
		server = new Server();
		Network.register(server);
		
		connToRoomMap = new HashMap<Integer, Room>();

		server.addListener(new Listener() {
			public void received(Connection c, Object object) {
				if (object instanceof RequireJoinGame) {
					RequireJoinGame requireJoinGame = (RequireJoinGame) object;
					joinGame(requireJoinGame.gameInfo);
				}
				
				if (object instanceof RequireUpdateInputToOthers) {
					RequireUpdateInputToOthers requireUpateInputToOthers = (RequireUpdateInputToOthers) object;
					Room room = connToRoomMap.get(c.getID());
					if (room == null) {
						return;
					}
					UpdateInput updateInput = new UpdateInput();
					updateInput.conn = c.getID();
					updateInput.keyCode = requireUpateInputToOthers.keyCode;
					updateInput.keyState = requireUpateInputToOthers.keyState;
					sendToAllInRoomExcept(room, c.getID(), updateInput);
				}
				
				if (object instanceof RequireUpdatePositionToOthers) {
					RequireUpdatePositionToOthers requireUpdatePositionToOthers = (RequireUpdatePositionToOthers) object;
					Room room = connToRoomMap.get(c.getID());
					if (room == null) {
						return;
					}
					UpdatePosition updatePosition = new UpdatePosition();
					updatePosition.conn = c.getID();
					updatePosition.x = requireUpdatePositionToOthers.x;
					updatePosition.y = requireUpdatePositionToOthers.y;
					sendToAllInRoomExcept(room, c.getID(), updatePosition);
				}
				
				if (object instanceof RequireUpdateBombPositionToOthers) {
					RequireUpdateBombPositionToOthers requireUpdateBombPositionToOthers = (RequireUpdateBombPositionToOthers) object;
					Room room = connToRoomMap.get(c.getID());
					if (room == null) {
						return;
					}
					UpdateBombPosition updateBombPosition = new UpdateBombPosition();
					updateBombPosition.conn = c.getID();
					updateBombPosition.x = requireUpdateBombPositionToOthers.x;
					updateBombPosition.y = requireUpdateBombPositionToOthers.y;
					updateBombPosition.angle = requireUpdateBombPositionToOthers.angle;
					updateBombPosition.angularVelocity = requireUpdateBombPositionToOthers.angularVelocity;
					updateBombPosition.linearVelocityX = requireUpdateBombPositionToOthers.linearVelocityX;
					updateBombPosition.linearVelocityY = requireUpdateBombPositionToOthers.linearVelocityY;
					updateBombPosition.bombIndex = requireUpdateBombPositionToOthers.bombIndex;
					sendToAllInRoomExcept(room, c.getID(), updateBombPosition);
				}
				
				if(object instanceof RequireUpdateHealthToOthers){
					RequireUpdateHealthToOthers requireUpdateHealthToOthers = (RequireUpdateHealthToOthers) object;
					Room room = connToRoomMap.get(c.getID());
					if (room == null) {
						return;
					}
					UpdateHealth updateHealth = new UpdateHealth();
					updateHealth.conn = c.getID();
					updateHealth.health = requireUpdateHealthToOthers.health;
					sendToAllInRoomExcept(room, c.getID(), updateHealth);
				}
				
				if (object instanceof RequireUpdateDropItem) {
					RequireUpdateDropItem request = (RequireUpdateDropItem)object;
				
					Room room = connToRoomMap.get(c.getID());
					
					if (room == null) {
						return;
					}
					
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
						ItemName itemName = null;
		    		
		    		
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
		    			
		    				for (Map.Entry<ItemName, Integer> entry : Network.CrateDropList.ItemInfo.itemDropProbability.entrySet()) {
		    					
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
		    			
		    				
								for (Map.Entry<ItemName, Integer> entry : Network.BrickDropList.ItemInfo.itemDropProbability.entrySet()) {
		    					
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
							updateDropItem.name = itemName.toString();
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
				Room room = connToRoomMap.get(c.getID());
				
				if (room == null) {
					return;
				}
				
				for (PlayerInfo playerInfo : room.playerInfoList) {
					if (playerInfo.conn == c.getID()) {
						room.playerInfoList.removeValue(playerInfo, true);
						if (room.playerInfoList.size == 0) {
							System.out.println("room#"+room+" has been removed"+"\n");
							for(TimerTask task : room.timerTasks){
								task.cancel();
							}
							room.timerTasks.clear();
							//roomList.set(roomList.indexOf(room, true), null);
							//roomList.removeValue(room, true);
							connToRoomMap.remove(c.getID());
						}
					}
				}
			}
		});
		
		server.bind(Network.portTCP, Network.portUDP);
		server.start();
		serverTimer = new Timer();
		
		

	}
	
	public void joinGame(GameInfo gameInfo) {
		Room room = findRoomBy(gameInfo.mapInfo, RoomState.waiting);
		if (room != null) {
			room.addPlayer(gameInfo.playerInfo);
			gameInfo.playerInfo.roomNumber = connToRoomMap.size();
			gameInfo.playerInfo.spawnPoint = room.playerInfoList.size - 1;
			
			connToRoomMap.put(gameInfo.playerInfo.conn, room);
			
			RespondJoinGame respondJoinGame = new RespondJoinGame();
			respondJoinGame.gameInfo = gameInfo;
			respondJoinGame.result = "joined room #" + gameInfo.playerInfo.roomNumber;
			System.out.println("connection"+gameInfo.playerInfo.conn+" joined room#"+gameInfo.playerInfo.roomNumber+"\n");
			server.sendToTCP(gameInfo.playerInfo.conn, respondJoinGame);
			
			if (room.playerInfoList.size == gameInfo.mapInfo.maxNumPlayer) {
				System.out.println("room#"+gameInfo.playerInfo.roomNumber+" start game"+"\n");
				startGame(room);
			}
		} else {
			room = new Room();
			connToRoomMap.put(gameInfo.playerInfo.conn, room);
			room.state = RoomState.waiting;
			room.playerInfoList.add(gameInfo.playerInfo);
			room.gameInfo = gameInfo;
			
			gameInfo.playerInfo.roomNumber = connToRoomMap.size();
			gameInfo.playerInfo.spawnPoint = room.playerInfoList.size - 1;
			
			System.out.println("room#"+gameInfo.playerInfo.roomNumber+" has been created"+"\n");
			
			RespondJoinGame respondJoinGame = new RespondJoinGame();
			respondJoinGame.gameInfo = gameInfo;
			respondJoinGame.result = "created room #" + gameInfo.playerInfo.roomNumber;
			server.sendToTCP(gameInfo.playerInfo.conn, respondJoinGame);
		}
	}
	
	public Room findRoomBy(MapInfo mapInfo, RoomState state) {
		Iterator<Entry<Integer, Room>> iter = connToRoomMap.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			Room value = (Room) entry.getValue();
			
			if (value.gameInfo.mapInfo.equals(mapInfo) && value.state == state) {
				return value;
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
		
		//initialItemSpawnPoint(room);
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

	
	
	
//	/**
//	 * timer task functions
//	 * 
//	 */
//	public void initialItemSpawnPoint(Room room){
//	
//		for(int i = 0; i < room.gameInfo.mapInfo.itemSpawnPoint.size; i++){
//			ItemSpawnPoint itemSpawnPoint = room.gameInfo.mapInfo.itemSpawnPoint.get(i);
//			
//			final int spawnPointID = i;
//		
//			TimerTask task = new TimerTask(){
//
//				@Override
//				public void run() {
//					
//					refreshItemInSpawnPoint( room, spawnPointID);
//				}
//				
//			};
//			serverTimer.schedule(task, 300 * 1000, (long) itemSpawnPoint.refreshTime * 1000);
//			room.timerTasks.add(task);
//		}
//		
//	}
//	
//	
//	public void refreshItemInSpawnPoint(Room room, int spawnPointID){
//		Room room = roomList.get(roomID);
//		ItemSpawnPoint itemSpawnPoint = room.gameInfo.mapInfo.itemSpawnPoint.get(spawnPointID);
//		
//		String itemName = null;
//		if(itemSpawnPoint.itemName.equalsIgnoreCase("RANDOM")){
//			itemName = RandomEnum.randomEnum(ItemName.class).toString();
//		}else {
//			itemName = itemSpawnPoint.itemName;
//		}
//		
//		UpdateDropItem updateDropItem = new UpdateDropItem();
//		updateDropItem.id = -1;
//		updateDropItem.name = itemName;
//		updateDropItem.x = itemSpawnPoint.x;
//		updateDropItem.y = itemSpawnPoint.y;
//		
//		sendToAllInRoom(room, updateDropItem);
//	}
	
	public static void main(String[] args) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		new BomberFightServer();
	}
}

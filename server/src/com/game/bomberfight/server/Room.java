package com.game.bomberfight.server;

import java.util.ArrayList;

public class Room {
	private int roomID;
	private String mapName;
	private int maxNumPlayer;
	private int desireNumPlayer;
	ArrayList<Player> playerInRoom;
	
	
	public Room(int roomID, String mapName, int maxNumPlayer, int desireNumPlayer){
		playerInRoom = new ArrayList<Player>();
		this.mapName = mapName;
		this.maxNumPlayer = maxNumPlayer;
		this.desireNumPlayer = desireNumPlayer;
		this.roomID = roomID;
	}
	
	public void addPlayer(Player player){
		player.setRoomNumber(this.roomID);
		this.playerInRoom.add(player);
		
		//if player is full or reach require player number, call server start the game
		if(this.playerInRoom.size() == desireNumPlayer || this.playerInRoom.size() == maxNumPlayer){
			BomberFightServer.startGame(this);
		}
	}
	
	public void removePlayer(Player player){
		for(Player p : playerInRoom){
			if(p.getPlayerID() == player.getPlayerID()){
				p.setRoomNumber(-1);
				this.playerInRoom.remove(p.getPlayerID());
				break;
			}
		}
		
	}

	
	
	
	
	/**
	 * setter getter
	 * @return
	 */
	public String getMapName() {return mapName;}
	public void setMapName(String mapName) {this.mapName = mapName;}
	public int getMaxNumPlayer() {return maxNumPlayer;}
	public void setMaxNumPlayer(int maxNumPlayer) {this.maxNumPlayer = maxNumPlayer;}
	public int getDesireNumPlayer() {return desireNumPlayer;}
	public void setDesireNumPlayer(int desireNumPlayer) {this.desireNumPlayer = desireNumPlayer;}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public void setWaitingPlayer(ArrayList<Player> waitingPlayer) {
		this.playerInRoom = waitingPlayer;
	}

	public ArrayList<Player> getPlayerInRoom() {
		return playerInRoom;
	}

	public void setPlayerInRoom(ArrayList<Player> playerInRoom) {
		this.playerInRoom = playerInRoom;
	}
	
	
}

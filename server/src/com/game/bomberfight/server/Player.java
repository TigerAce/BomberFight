package com.game.bomberfight.server;

public class Player {
	private String playerName;
	private int playerID;
	private int roomNumber;

	
	
	
	public Player(int playerID, int roomNumber){
		this.playerID = playerID;
		this.roomNumber = roomNumber;
	}
	
	

	
	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
}

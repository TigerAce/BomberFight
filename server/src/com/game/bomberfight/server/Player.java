package com.game.bomberfight.server;

public class Player {
	private String playerName;
	private int playerID;
	private Room inRoom;
	private int gamePosition;

	
	
	
	public Player(int playerID, Room inRoom){
		this.playerID = playerID;
		this.inRoom = inRoom;
	}
	
	

	

	public Room getInRoom() {
		return inRoom;
	}


	public void setInRoom(Room inRoom) {
		this.inRoom = inRoom;
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





	public int getGamePosition() {
		return gamePosition;
	}





	public void setGamePosition(int gamePosition) {
		this.gamePosition = gamePosition;
	}





}

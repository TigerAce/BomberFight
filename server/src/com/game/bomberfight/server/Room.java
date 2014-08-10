package com.game.bomberfight.server;

import com.badlogic.gdx.utils.Array;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.PlayerInfo;

public class Room {
	public int number;
	public GameInfo gameInfo;
	public RoomState state;
	public Array<PlayerInfo> playerInfoList = new Array<PlayerInfo>();
	public Array<Integer> destroyedGameObjectId = new Array<Integer>();
	
	public enum RoomState {
		waiting, playing
	}
	
	public void add(PlayerInfo playerInfo) {
		playerInfoList.add(playerInfo);
	}
}

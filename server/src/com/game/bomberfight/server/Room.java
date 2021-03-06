package com.game.bomberfight.server;

import java.util.TimerTask;

import com.badlogic.gdx.utils.Array;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.PlayerInfo;

public class Room {
	public GameInfo gameInfo;
	public RoomState state;
	public Array<PlayerInfo> playerInfoList = new Array<PlayerInfo>();
	public Array<Integer> destroyedGameObjectId = new Array<Integer>();
	public Array<TimerTask> timerTasks = new Array<TimerTask>();
	public enum RoomState {
		waiting, playing
	}
	
	public void addPlayer(PlayerInfo playerInfo) {
		playerInfoList.add(playerInfo);
	}
}

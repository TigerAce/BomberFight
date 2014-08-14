package com.game.bomberfight.server;

import java.util.TimerTask;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.PlayerInfo;

public class Room {
	public int number;
	public GameInfo gameInfo;
	public RoomState state;
	public Array<PlayerInfo> playerInfoList = new Array<PlayerInfo>();
	public Array<Integer> destroyedGameObjectId = new Array<Integer>();
	public Array<TimerTask> timerTasks = new Array<TimerTask>();
	public enum RoomState {
		waiting, playing
	}
	
	public void add(PlayerInfo playerInfo) {
		playerInfoList.add(playerInfo);
	}
}

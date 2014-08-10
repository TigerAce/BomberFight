package com.game.bomberfight.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.model.MapInfo;
import com.game.bomberfight.model.PlayerInfo;

public class Network {
	public static final int portTCP = 54555;
	public static final int portUDP = 54777;

	public static void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();

		kryo.register(RequireJoinGame.class);
		kryo.register(RespondJoinGame.class);
		kryo.register(StartGame.class);
		kryo.register(RequireUpdateInputToOthers.class);
		kryo.register(UpdateInput.class);
		kryo.register(RequireUpdatePositionToOthers.class);
		kryo.register(UpdatePosition.class);
		kryo.register(RequireUpdateDropItem.class);
		kryo.register(UpdateDropItem.class);
		
		kryo.register(GameInfo.class);
		kryo.register(MapInfo.class);
		kryo.register(PlayerInfo.class);
		
		kryo.register(PlayerInfo[].class);
	}
	
	public static class RequireJoinGame {
		public GameInfo gameInfo;
	}
	
	public static class RespondJoinGame {
		public String result;
		public GameInfo gameInfo;
	}
	
	public static class StartGame {
		public PlayerInfo[] playerInfoList;
	}
	
	public static class RequireUpdateInputToOthers {
		public int keyCode;
		public boolean keyState;
	}
	
	public static class UpdateInput {
		public int keyCode;
		public boolean keyState;
		public int conn;
	}
	
	public static class RequireUpdatePositionToOthers {
		public float x;
		public float y;
	}
	
	public static class UpdatePosition {
		public float x;
		public float y;
		public int conn;
	}
	
	public static class RequireUpdateDropItem {
		public float x;
		public float y;
		public String name;
		public int id;
	}
	
	public static class UpdateDropItem {
		public float x;
		public float y;
		public String name;
		public int id;
	}

}
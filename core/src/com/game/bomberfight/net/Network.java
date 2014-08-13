package com.game.bomberfight.net;

import java.util.HashMap;

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
		kryo.register(RequireUpdateBombPositionToOthers.class);
		kryo.register(UpdateBombPosition.class);
		kryo.register(RequireUpdateDropItem.class);
		kryo.register(UpdateDropItem.class);
		kryo.register(RequireUpdateHealthToOthers.class);
		kryo.register(UpdateHealth.class);
		kryo.register(GameInfo.class);
		kryo.register(MapInfo.class);
		kryo.register(PlayerInfo.class);
		
		kryo.register(PlayerInfo[].class);
		
		kryo.register(SignalBarrierDestroyed.class);
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
	
	public static class RequireUpdateBombPositionToOthers{
		public int bombIndex;
		public float x;
		public float y;
	}
	
	public static class UpdateBombPosition{
		public int bombIndex;
		public float x;
		public float y;
		public int conn;
	}
	
	public static class RequireUpdateHealthToOthers{
		public float health;
	}
	
	public static class UpdateHealth{
		public float health;
		public int conn;
	}
	
	public static class SignalBarrierDestroyed{
		public int id;
	}
	
	public static class CrateDropList{
		public static String name = "CRATE";
		public static int dropProbability = 5;
		public static class ItemInfo{
			public static HashMap<String, Integer> itemDropProbability = new HashMap<String, Integer>();
			
			static{
				itemDropProbability.put("ANNULAR", 10);
				itemDropProbability.put("POWER_UP", 10);
				itemDropProbability.put("ADDBOMB", 10);
				itemDropProbability.put("HEAL", 5);
			}
		}
	}
	
	public static class BrickDropList{
		public static String name = "BRICK";
		public static int dropProbability = 4;
		public static class ItemInfo{
			public static HashMap<String, Integer> itemDropProbability = new HashMap<String, Integer>();
			
			static{
				itemDropProbability.put("ANNULAR", 10);
				itemDropProbability.put("POWER_UP", 30);
				itemDropProbability.put("ADDBOMB", 20);
				itemDropProbability.put("HEAL", 5);
			}
		}
	}

}
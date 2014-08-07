package com.game.bomberfight.server;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.game.bomberfight.net.Network.RemoteControl;
import com.game.bomberfight.net.Network.StartGame;
import com.game.bomberfight.server.Direction;



public class Network {
	public static final int portTCP = 54555;
	public static final int portUDP = 54777;
	
	public static void register (EndPoint endPoint) {
         Kryo kryo = endPoint.getKryo();
      
         kryo.register(RemoteControl.class);
         kryo.register(StopMovePlayer.class);
         kryo.register(CorrectPosition.class);
         kryo.register(JoinGame.class);
         kryo.register(Direction.class);
         kryo.register(Vector2.class);
         kryo.register(Ping.class);
         kryo.register(AssignBornPosition.class);
         kryo.register(CompleteInitGame.class);
         kryo.register(StartGame.class);
      
	}
	
	public static class StartGame{}
	
	public static class CompleteInitGame{}
	
	public static class AssignBornPosition{
		public short positionNumber;
		public AssignBornPosition(){}
		public AssignBornPosition(short positionNumber){
			this.positionNumber = positionNumber;
		}
	}
	
	public static class JoinGame{
		public String mapName;
		public short numPlayers;
		
		public JoinGame(){};
		public JoinGame(String mapName, short numPlayer){
			this.mapName = mapName;
			this.numPlayers = numPlayer;
		}
	}
	
	public static class CorrectPosition{
		public int playerID;
		public Vector2 pos;
		
		public CorrectPosition(){}
		public CorrectPosition(Vector2 pos, int playerID){
			this.pos = pos;
			this.playerID = playerID;
		}
	}
	
	public static class RemoteControl{
		public int playerID;
		public int keycode;
		public boolean upOrDown;
		
		public RemoteControl(){}
		public RemoteControl(int playerID, int keycode, boolean upOrDown){
			this.playerID = playerID;
			this.keycode = keycode;
			this.upOrDown = upOrDown;
		}
	}
	
	public static class StopMovePlayer{
	
	}
	
	public static class MapInfo{
		//map information stores map name and its maximum player
		public static HashMap<String, Integer> mapInfo = new HashMap<String, Integer>();
		
		static{
			mapInfo.put("map1", 2);
			mapInfo.put("map2", 2);
			mapInfo.put("map3", 2);
		}
	}
	
	
	
	
	
	
	
	
}

package com.game.bomberfight.server;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.game.bomberfight.server.Direction;



public class Network {
	public static final int portTCP = 54555;
	public static final int portUDP = 54777;
	
	public static void register (EndPoint endPoint) {
         Kryo kryo = endPoint.getKryo();
      
         kryo.register(StartMovePlayer.class);
         kryo.register(StopMovePlayer.class);
         kryo.register(CorrectPosition.class);
         kryo.register(JoinGame.class);
         kryo.register(Direction.class);
         kryo.register(Vector2.class);
         kryo.register(Ping.class);
      
	}
	
	public static class JoinGame{
		public String mapName;
		public int numPlayers;
		
		public JoinGame(){};
		public JoinGame(String mapName, int numPlayer){
			this.mapName = mapName;
			this.numPlayers = numPlayer;
		}
	}
	
	public static class CorrectPosition{
		public Vector2 pos;
		
		public CorrectPosition(){};
		public CorrectPosition(Vector2 pos){
			this.pos = pos;
		}
	}
	public static class StartMovePlayer{
		public Direction direction;
		
		public StartMovePlayer(){};
		public StartMovePlayer(Direction direction){
			this.direction = direction;
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

package com.game.bomberfight.server;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;



public class Network {
	public static final int portTCP = 54555;
	public static final int portUDP = 54777;
	
	public static void register (EndPoint endPoint) {
         Kryo kryo = endPoint.getKryo();
      
         kryo.register(StartMovePlayer.class);
         kryo.register(StopMovePlayer.class);
         kryo.register(CorrectPosition.class);
         kryo.register(Direction.class);
         kryo.register(Vector2.class);
         
      
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
	
	
	
	
	
	
	
	
	
}

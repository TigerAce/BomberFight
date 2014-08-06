package com.game.bomberfight.net;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.game.bomberfight.enums.Direction;



public class Network {
	public static final int portTCP = 54555;
	public static final int portUDP = 54777;
	
	public static void register (EndPoint endPoint) {
         Kryo kryo = endPoint.getKryo();
        
         kryo.register(StartMovePlayer.class);
         kryo.register(StopMovePlayer.class);
         kryo.register(Direction.class);
        
  
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

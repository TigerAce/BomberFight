package com.game.bomberfight.net;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.game.bomberfight.enums.Direction;




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
         kryo.register(BornPosition.class);
	}
	
	public static class BornPosition{
		public short positionNumber;
		public BornPosition(){}
		public BornPosition(short positionNumber){
			this.positionNumber = positionNumber;
		}
	}
	public static class JoinGame{
		public String mapName;
		public short numPlayers;
		
		public JoinGame(){}
		public JoinGame(String mapName, short numPlayer){
			this.mapName = mapName;
			this.numPlayers = numPlayer;
		}
	}
	
	public static class CorrectPosition{
		public Vector2 pos;
		
		public CorrectPosition(){}
		public CorrectPosition(Vector2 pos){
			this.pos = pos;
		}
	}
	
	public static class StartMovePlayer{
		public Direction direction;
		
		public StartMovePlayer(){}
		public StartMovePlayer(Direction direction){
			this.direction = direction;
		}
	}
	
	public static class StopMovePlayer{
		
	}
	
	
	
	
	
	
	
}

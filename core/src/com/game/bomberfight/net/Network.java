package com.game.bomberfight.net;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.core.PlayerGameAttributes;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.interfaces.Destructible;
import com.game.bomberfight.model.Explosion;
import com.game.bomberfight.model.GameObject;
import com.game.bomberfight.model.Player;

public class Network {
	public static final int portTCP = 54555;
	public static final int portUDP = 54777;
	
	public static void register (EndPoint endPoint) {
         Kryo kryo = endPoint.getKryo();
         kryo.register(Player.class);
         kryo.register(Player.Direction.class);
         kryo.register(Vector2.class);
         kryo.register(LinkedHashMap.class);
         kryo.register(GameObject.class);
         kryo.register(PlayerGameAttributes.class);
         kryo.register(Explosion.class);
         kryo.register(Explosion.Style.class);
         kryo.register(Destructible.class);
         kryo.register(Controllable.class);
         kryo.register(Body.class);
         kryo.register(Array.class);
         kryo.register(Object[].class);
         kryo.register(Fixture.class);
         kryo.register(Bomber.class);
         kryo.register(ArrayList.class);
	}
	
	public static class StartMovePlayer{
		
	}
	
	public static class StopMovePlayer{
		
	}
	
	
	
	
	
	
	
}

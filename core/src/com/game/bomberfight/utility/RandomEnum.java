package com.game.bomberfight.utility;

import java.util.Random;

public class RandomEnum {
	 public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
		 	Random random = new Random();
	        int x = random.nextInt(clazz.getEnumConstants().length);
	        return clazz.getEnumConstants()[x];
	    }
}

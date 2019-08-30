package com.delivery.deliveryservice.utils;

import java.util.Random;

public class Utils {

	public static Long getRandomNumberString() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    return Long.parseLong(String.format("%06d", number));
	}
	
}

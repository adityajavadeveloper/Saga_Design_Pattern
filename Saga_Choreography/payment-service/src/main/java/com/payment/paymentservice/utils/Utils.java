package com.payment.paymentservice.utils;

import java.util.Random;

public class Utils {

	public static Long getRandomNumberString() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(9999);
	    return Long.parseLong(String.format("%04d", number));
	}
	
}

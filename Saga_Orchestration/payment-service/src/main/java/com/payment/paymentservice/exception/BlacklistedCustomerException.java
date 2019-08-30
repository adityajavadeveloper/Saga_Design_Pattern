package com.payment.paymentservice.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BlacklistedCustomerException extends Exception {
	private static final long serialVersionUID = 1L;

	public BlacklistedCustomerException(String s){
		super(s);
	}
	
}

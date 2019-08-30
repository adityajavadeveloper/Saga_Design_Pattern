package com.payment.paymentservice.service;

import com.payment.paymentservice.exception.BlacklistedCustomerException;
import com.payment.paymentservice.model.PaymentRequest;
import com.payment.paymentservice.model.PaymentResponse;

public interface PaymentService {
	public PaymentResponse debitCustomer(PaymentRequest paymentRequest) throws BlacklistedCustomerException;
}

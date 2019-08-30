package com.payment.paymentservice.service;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.payment.paymentservice.constants.Constants;
import com.payment.paymentservice.exception.BlacklistedCustomerException;
import com.payment.paymentservice.log.LogConst;
import com.payment.paymentservice.log.LoggerSingleton;
import com.payment.paymentservice.model.PaymentRequest;
import com.payment.paymentservice.model.PaymentResponse;
import com.payment.paymentservice.utils.Utils;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final String CLASSNAME = "PaymentEventListener";
	
	@Override
	@HystrixCommand(fallbackMethod = "debitCustomerFallback", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000"),
			@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "30000")})
	public PaymentResponse debitCustomer(PaymentRequest paymentRequest) throws BlacklistedCustomerException {
		final String methodName = "debitCustomer";
		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setCustId(paymentRequest.getCustId());
		
		//Dummy logic if customer id is 1 then send fail response.
		if(Constants.BLACKLISTED_CUST_ID == paymentRequest.getCustId()) {
			throw new BlacklistedCustomerException("Blacklisted Customer Found with custId = " + paymentRequest.getCustId());
		}else {
			paymentResponse.setTotalPrice(Utils.getRandomNumberString());
			paymentResponse.setRespCode(Constants.SUCCESS_CODE);
			paymentResponse.setRespMsg(Constants.SUCCESS_DESC);
			
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Successful debited customer wallet paymentResponse = " + paymentResponse);
		}
		
		return paymentResponse;
	}
	
	public PaymentResponse debitCustomerFallback(PaymentRequest paymentRequest) throws BlacklistedCustomerException {
		final String methodName = "debitCustomerFallback";
		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setCustId(paymentRequest.getCustId());
		
		paymentResponse.setTotalPrice(Utils.getRandomNumberString());
		paymentResponse.setRespCode(Constants.FAILED_CODE);
		paymentResponse.setRespMsg(Constants.FAILED_DESC);
		
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Blacklisted Customer Found paymentResponse = " + paymentResponse);
		
		return paymentResponse;
	}

}

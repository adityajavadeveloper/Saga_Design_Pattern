package com.payment.paymentservice.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.paymentservice.config.ApplicationContextProvider;
import com.payment.paymentservice.constants.Constants;
import com.payment.paymentservice.log.LogConst;
import com.payment.paymentservice.log.LoggerSingleton;
import com.payment.paymentservice.model.OrderRequest;
import com.payment.paymentservice.utils.Utils;

public class PaymentEventListener implements MessageListener {
	
	private RabbitTemplate orderRabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean("orderRabbitTemplate", RabbitTemplate.class);
	private RabbitTemplate stockRabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean("stockRabbitTemplate", RabbitTemplate.class);
	private RabbitTemplate deliveryRabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean("deliveryRabbitTemplate", RabbitTemplate.class);

	private static final String CLASSNAME = "PaymentEventListener";
	
	@Override
	public void onMessage(Message message) {
		final String methodName = "onMessage";
		try {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "New message received = " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			
			OrderRequest orderRequest = objectMapper.readValue(new String(message.getBody()), OrderRequest.class);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Converted orderRequest = " + orderRequest);
			
			//Dummy logic if customer id is 1 then send fail response.
			if(Constants.BLACKLISTED_CUST_ID == orderRequest.getCustId()) {
				orderRequest.setTotalPrice(Utils.getRandomNumberString());
				orderRequest.setOrderStatus(Constants.FAILED_CODE);
				orderRequest.setPaymentRespCode(Constants.FAILED_CODE);
				orderRequest.setPaymentRespDesc(Constants.FAILED_DESC);
				
				orderRequest.setAction(Constants.REVERT_STOCK);
				
				stockRabbitTemplate.convertAndSend(orderRequest);
				orderRabbitTemplate.convertAndSend(orderRequest);
				
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Blacklisted Customer Found orderRequest = " + orderRequest);
			}else {
				orderRequest.setTotalPrice(Utils.getRandomNumberString());
				orderRequest.setOrderStatus(Constants.SUCCESS_CODE);
				orderRequest.setPaymentRespCode(Constants.SUCCESS_CODE);
				orderRequest.setPaymentRespDesc(Constants.SUCCESS_DESC);
				
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Successful debited customer wallet orderRequest = " + orderRequest);
				
				deliveryRabbitTemplate.convertAndSend(orderRequest);
			}
		} catch (Throwable t) {
			LoggerSingleton.error(CLASSNAME, methodName, "Technical Error Occured ", t);
		}
		
	}

}

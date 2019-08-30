package com.delivery.deliveryservice.listener;

import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.delivery.deliveryservice.config.ApplicationContextProvider;
import com.delivery.deliveryservice.constants.Constants;
import com.delivery.deliveryservice.log.LogConst;
import com.delivery.deliveryservice.log.LoggerSingleton;
import com.delivery.deliveryservice.model.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeliveryEventListener implements MessageListener {
	
	private RabbitTemplate orderRabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean("orderRabbitTemplate", RabbitTemplate.class);
	
	private static final String CLASSNAME = "DeliveryEventListener";

	@Override
	public void onMessage(Message message) {
		final String methodName = "onMessage";
		
		try {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "New message received = " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			
			OrderRequest orderRequest = objectMapper.readValue(new String(message.getBody()), OrderRequest.class);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Converted orderRequest = " + orderRequest);
			
			orderRequest.setSystemDeliveryNo(UUID.randomUUID().toString());
			orderRequest.setOrderStatus(Constants.SUCCESS_CODE);
			orderRequest.setDeliveryRespCode(Constants.SUCCESS_CODE);
			orderRequest.setDeliveryRespDesc(Constants.SUCCESS_DESC);
			
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Successfully initiated delivery orderRequest = " + orderRequest);
			
			orderRabbitTemplate.convertAndSend(orderRequest);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}

}

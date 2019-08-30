package com.order.orderservice.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.orderservice.config.ApplicationContextProvider;
import com.order.orderservice.dao.OrderResponseDao;
import com.order.orderservice.log.LogConst;
import com.order.orderservice.log.LoggerSingleton;
import com.order.orderservice.model.OrderResponse;

public class OrderPlacedEventListener implements MessageListener {
	
	private OrderResponseDao orderResponseDao = ApplicationContextProvider.getApplicationContext().getBean("orderResponseDao", OrderResponseDao.class);
	
	private static final String CLASSNAME = "OrderPlacedEventListener";

	@Override
	public void onMessage(Message message) {
		final String methodName = "onMessage";
		
		try {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "New message received = " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			
			OrderResponse orderResponse = objectMapper.readValue(new String(message.getBody()), OrderResponse.class);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Converted orderResponse = " + orderResponse);
			
			orderResponseDao.save(orderResponse);
		}catch (Throwable t) {
			LoggerSingleton.error(CLASSNAME, methodName, "Technical Error Occured ", t);
		}
	}

}

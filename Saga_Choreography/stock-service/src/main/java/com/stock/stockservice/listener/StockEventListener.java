package com.stock.stockservice.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stockservice.config.ApplicationContextProvider;
import com.stock.stockservice.constants.Constants;
import com.stock.stockservice.log.LogConst;
import com.stock.stockservice.log.LoggerSingleton;
import com.stock.stockservice.model.OrderRequest;

public class StockEventListener implements MessageListener {
	
	private RabbitTemplate paymentRabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean("paymentRabbitTemplate", RabbitTemplate.class);
	
	private static final String CLASSNAME = "StockEventListener";

	@Override
	public void onMessage(Message message) {
		final String methodName = "onMessage";
		
		try {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "New message received = " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			
			OrderRequest orderRequest = objectMapper.readValue(new String(message.getBody()), OrderRequest.class);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Converted orderRequest = " + orderRequest);
			
			if(Constants.BLOCK_STOCK.equals(orderRequest.getAction())) {
				orderRequest.setOrderStatus(Constants.SUCCESS_CODE);
				orderRequest.setStockRespCode(Constants.SUCCESS_CODE);
				orderRequest.setStockRespDesc(Constants.SUCCESS_DESC);
				
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Successfully block stock orderRequest = " + orderRequest);
				
				paymentRabbitTemplate.convertAndSend(orderRequest);
			}else {
				orderRequest.setOrderStatus(Constants.REVERT_STOCK_CODE);
				orderRequest.setStockRespCode(Constants.REVERT_STOCK_CODE);
				orderRequest.setStockRespDesc(Constants.REVERT_STOCK_DESC);
				
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Reverted the stock orderRequest = " + orderRequest);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}

}

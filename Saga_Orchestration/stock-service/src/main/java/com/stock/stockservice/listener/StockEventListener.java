package com.stock.stockservice.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stockservice.config.ApplicationContextProvider;
import com.stock.stockservice.constants.Constants;
import com.stock.stockservice.log.LogConst;
import com.stock.stockservice.log.LoggerSingleton;
import com.stock.stockservice.model.StockRequest;
import com.stock.stockservice.model.StockResponse;

public class StockEventListener implements MessageListener {
	
	private static final String CLASSNAME = "StockEventListener";

	@Override
	public void onMessage(Message message) {
		final String methodName = "onMessage";
		
		try {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "New message received = " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			
			StockRequest stockRequest = objectMapper.readValue(new String(message.getBody()), StockRequest.class);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Converted stockRequest = " + stockRequest);
			
			StockResponse stockResponse = new StockResponse();
			
			if(Constants.BLOCK_STOCK.equals(stockRequest.getAction())) {
				stockResponse.setRespCode(Constants.SUCCESS_CODE);
				stockResponse.setRespMsg(Constants.SUCCESS_DESC);
				
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Successfully block stock stockResponse = " + stockResponse);
			}else {
				stockResponse.setRespCode(Constants.REVERT_STOCK_CODE);
				stockResponse.setRespMsg(Constants.REVERT_STOCK_DESC);
				
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Reverted the stock stockResponse = " + stockResponse);
			}
			
			RabbitTemplate rabbitTemplate = new RabbitTemplate();
			rabbitTemplate.setConnectionFactory(ApplicationContextProvider.getApplicationContext().getBean(ConnectionFactory.class));
			rabbitTemplate.setExchange(message.getMessageProperties().getReplyToAddress().getExchangeName());
			rabbitTemplate.setRoutingKey(message.getMessageProperties().getReplyToAddress().getRoutingKey());
			rabbitTemplate.setMessageConverter(ApplicationContextProvider.getApplicationContext().getBean(MessageConverter.class));
			
			MessageProperties messageProperties = message.getMessageProperties();
			messageProperties.setType("StockResponse");
			
			Message newMessage = new Message(objectMapper.writeValueAsBytes(stockResponse), messageProperties);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Final response = " + newMessage);
			
			rabbitTemplate.send(newMessage);
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}

}

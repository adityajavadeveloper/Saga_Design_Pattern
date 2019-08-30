package com.delivery.deliveryservice.listener;

import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.delivery.deliveryservice.config.ApplicationContextProvider;
import com.delivery.deliveryservice.constants.Constants;
import com.delivery.deliveryservice.log.LogConst;
import com.delivery.deliveryservice.log.LoggerSingleton;
import com.delivery.deliveryservice.model.DeliveryRequest;
import com.delivery.deliveryservice.model.DeliveryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeliveryEventListener implements MessageListener {
	
	private static final String CLASSNAME = "DeliveryEventListener";

	@Override
	public void onMessage(Message message) {
		final String methodName = "onMessage";
		
		try {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "New message received = " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			
			DeliveryRequest deliveryRequest = objectMapper.readValue(new String(message.getBody()), DeliveryRequest.class);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Converted deliveryRequest = " + deliveryRequest);
			
			DeliveryResponse deliveryResponse = new DeliveryResponse();
			deliveryResponse.setOrderId(deliveryRequest.getOrderId());
			deliveryResponse.setSystemDeliveryNo(UUID.randomUUID().toString());
			deliveryResponse.setRespCode(Constants.SUCCESS_CODE);
			deliveryResponse.setRespMsg(Constants.SUCCESS_DESC);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Successfully initiated delivery deliveryResponse = " + deliveryResponse);
			
			RabbitTemplate rabbitTemplate = new RabbitTemplate();
			rabbitTemplate.setConnectionFactory(ApplicationContextProvider.getApplicationContext().getBean(ConnectionFactory.class));
			rabbitTemplate.setExchange(message.getMessageProperties().getReplyToAddress().getExchangeName());
			rabbitTemplate.setRoutingKey(message.getMessageProperties().getReplyToAddress().getRoutingKey());
			rabbitTemplate.setMessageConverter(ApplicationContextProvider.getApplicationContext().getBean(MessageConverter.class));
			
			MessageProperties messageProperties = message.getMessageProperties();
			messageProperties.setType("DeliveryResponse");
			
			Message newMessage = new Message(objectMapper.writeValueAsBytes(deliveryResponse), messageProperties);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Final response = " + newMessage);
			
			rabbitTemplate.send(newMessage);
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}

}

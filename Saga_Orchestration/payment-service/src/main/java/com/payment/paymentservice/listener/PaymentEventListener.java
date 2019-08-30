package com.payment.paymentservice.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.paymentservice.config.ApplicationContextProvider;
import com.payment.paymentservice.log.LogConst;
import com.payment.paymentservice.log.LoggerSingleton;
import com.payment.paymentservice.model.PaymentRequest;
import com.payment.paymentservice.model.PaymentResponse;
import com.payment.paymentservice.service.PaymentService;

public class PaymentEventListener implements MessageListener {
	
	private PaymentService paymentService = ApplicationContextProvider.getApplicationContext().getBean("paymentServiceImpl", PaymentService.class);

	private static final String CLASSNAME = "PaymentEventListener";
	
	@Override
	public void onMessage(Message message) {
		final String methodName = "onMessage";
		try {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "New message received = " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			
			PaymentRequest paymentRequest = objectMapper.readValue(new String(message.getBody()), PaymentRequest.class);
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Converted paymentRequest = " + paymentRequest);
			
			PaymentResponse paymentResponse = paymentService.debitCustomer(paymentRequest);
			
			RabbitTemplate rabbitTemplate = new RabbitTemplate();
			rabbitTemplate.setConnectionFactory(ApplicationContextProvider.getApplicationContext().getBean(ConnectionFactory.class));
			rabbitTemplate.setExchange(message.getMessageProperties().getReplyToAddress().getExchangeName());
			rabbitTemplate.setRoutingKey(message.getMessageProperties().getReplyToAddress().getRoutingKey());
			rabbitTemplate.setMessageConverter(ApplicationContextProvider.getApplicationContext().getBean(MessageConverter.class));
			
			MessageProperties messageProperties = message.getMessageProperties();
			messageProperties.setType("PaymentResponse");
			
			Message newMessage = new Message(objectMapper.writeValueAsBytes(paymentResponse), messageProperties);
			
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Final response = " + newMessage);
			
			rabbitTemplate.send(newMessage);
			
		} catch (Throwable t) {
			LoggerSingleton.error(CLASSNAME, methodName, "Technical Error Occured ", t);
		}
		
	}

}

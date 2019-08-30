package com.order.orderservice.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderservice.constants.Constants;
import com.order.orderservice.dao.OrderRequestDao;
import com.order.orderservice.log.LogConst;
import com.order.orderservice.log.LoggerSingleton;
import com.order.orderservice.model.ClientOrderRequest;
import com.order.orderservice.model.OrderRequest;

@RestController
public class OrderController {

	@Autowired
	@Qualifier("stockRabbitTemplate")
	private RabbitTemplate stockRabbitTemplate;
	
	@Autowired
	private OrderRequestDao orderRequestDao;
	
	private static final String CLASSNAME = "OrderController";
	
	@RequestMapping(value = "createOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String createOrder(ClientOrderRequest clientOrderRequest) {
		final String methodName = "createOrder";
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Incoming clientOrderRequest = " + clientOrderRequest);
		
		OrderRequest orderRequest = new OrderRequest(clientOrderRequest);
		OrderRequest savedOrderRequest = orderRequestDao.save(orderRequest);
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "After saving order database savedOrderRequest = " + savedOrderRequest);
		
		savedOrderRequest.setAction(Constants.BLOCK_STOCK);
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Block stock request = " + savedOrderRequest);
		stockRabbitTemplate.convertAndSend(savedOrderRequest);
		
		return "Order Initiated";
	}
	
}

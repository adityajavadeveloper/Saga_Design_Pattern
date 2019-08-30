package com.order.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderservice.dao.OrderServiceDao;
import com.order.orderservice.log.LogConst;
import com.order.orderservice.log.LoggerSingleton;
import com.order.orderservice.model.OrderRequest;
import com.order.orderservice.model.OrderResponse;
import com.order.orderservice.proxy.OrchestratorProxy;

@RestController
public class OrderController {

	@Autowired
	private OrchestratorProxy orchestratorProxy;
	
	@Autowired
	private OrderServiceDao orderServiceDao;
	
	private static final String CLASSNAME = "OrderController";
	
	@RequestMapping(value = "createOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OrderResponse orchestrate(OrderRequest orderRequest) {
		final String methodName = "orchestrate";
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Incoming orderRequest = " + orderRequest);
		
		OrderRequest savedOrderRequest = orderServiceDao.save(orderRequest);
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "After saving order database savedOrderRequest = " + savedOrderRequest);
		
		OrderResponse orderResponse = orchestratorProxy.orchestrate(savedOrderRequest);
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Final Response = " + orderResponse);
		
		return orderResponse;
	}
	
}

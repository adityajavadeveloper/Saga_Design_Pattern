package com.saga.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.saga.orchestrator.constants.Constants;
import com.saga.orchestrator.log.LogConst;
import com.saga.orchestrator.log.LoggerSingleton;
import com.saga.orchestrator.model.OrderRequest;
import com.saga.orchestrator.model.OrderResponse;
import com.saga.orchestrator.service.OrchestratorService;
import com.saga.orchestrator.utils.Utils;

@RestController
public class OrchestratorController {
	
	@Autowired
	private OrchestratorService orchestratorService;
	
	private static final String CLASSNAME = "OrchestratorController";
	
	@RequestMapping(value = "orchestrate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OrderResponse orchestrate(OrderRequest orderRequest) {
		final String methodName = "orchestrate";
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Incoming orderRequest = " + orderRequest);		
		
		OrderResponse orderResponse = null;
		
		try {
			orderResponse = orchestratorService.orchestrate(orderRequest);
		}catch(Throwable t) {
			orderResponse = Utils.toOrderResponse(orderRequest);
			orderResponse.setOrderStatus(Constants.TECHNICAL_ERROR_CODE);
			LoggerSingleton.error(CLASSNAME, methodName, "Technical Error Occured ", t);
		}
		
		return orderResponse;
	}
	
}
